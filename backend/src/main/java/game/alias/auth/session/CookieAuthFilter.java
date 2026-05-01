package game.alias.auth.session;

import java.io.IOException;
import java.util.Arrays;
import java.util.UUID;

import game.alias.auth.session.exceptions.AuthException;
import game.alias.common.ApiVersion;
import game.alias.config.properties.SessionConfigProperties;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import game.alias.auth.AuthUser;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerExceptionResolver;

@Component
@Slf4j
public class CookieAuthFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final RedisAuthUserService redisAuthService;
    private final HandlerExceptionResolver exceptionResolver;
    private final AuthCookieService authCookieService;
    private final SessionConfigProperties sessionConfig;

    public CookieAuthFilter(
            JwtService jwtService,
            RedisAuthUserService redisAuthService,
            @Qualifier("handlerExceptionResolver") HandlerExceptionResolver exceptionResolver,
            AuthCookieService authCookieService,
            SessionConfigProperties sessionConfig
    ) {
        this.jwtService = jwtService;
        this.redisAuthService = redisAuthService;
        this.exceptionResolver = exceptionResolver;
        this.authCookieService = authCookieService;
        this.sessionConfig = sessionConfig;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();

        return path.startsWith("/ws/")
                || path.startsWith("/swagger-ui/")
                || path.startsWith("/v3/api-docs/")
                || path.startsWith(ApiVersion.V1Public);
    }

    private String extractCookieFromRequest(@NonNull HttpServletRequest request) {
        String cookieName = sessionConfig.cookie().auth().name();

        if (request.getCookies() == null) {
            return null;
        }

        log.info("Cookies: " + Arrays.toString(request.getCookies()));

        for (var cookie : request.getCookies()) {
            if (cookie.getName().equals(cookieName)) {
                return cookie.getValue();
            }
        }

        return null;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        try {
            var token = extractCookieFromRequest(request);

            if (token == null) {
                log.info("No token recived");
                throw new AuthException("No token provided");
            }

            String sessionId = jwtService.validateAndGetSessionId(token);

            if (sessionId == null) {
                log.info("No session recived");
                return;
            }

            String refreshedToken = jwtService.generateSessionToken(UUID.fromString(sessionId));
            var refreshedCookie = authCookieService.create(refreshedToken);
            response.addHeader("Set-Cookie", refreshedCookie.toString());

            AuthUser user = redisAuthService.getUserFromSessionAndExtend(sessionId);

            if (user == null) {
                log.info("No user found");
                return;
            }

            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(user, null,
                    user.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(auth);

            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException e) {
            log.warn("ExpiredJwtException", e);
            exceptionResolver.resolveException(request, response, null, new AuthException("Token expired", e));
        } catch (JwtException e) {
            log.warn("JwtException", e);
            exceptionResolver.resolveException(request, response, null, new AuthException("Invalid token", e));
        } catch (AuthException e) {
            log.warn("AuthException", e);
            exceptionResolver.resolveException(request, response, null, e);
        } catch (Exception e) {
            log.error("Unexpected exception", e);
            exceptionResolver.resolveException(request, response, null, new AuthException("Unknown error", e));
        }
    }
}
