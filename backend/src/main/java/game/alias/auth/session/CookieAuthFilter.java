package game.alias.auth.session;

import java.io.IOException;
import java.util.Arrays;

import game.alias.auth.session.exceptions.AuthException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
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

    public CookieAuthFilter(
            JwtService jwtService,
            RedisAuthUserService redisAuthService,
            @Qualifier("handlerExceptionResolver") HandlerExceptionResolver exceptionResolver, AuthCookieService authCookieService
    ) {
        this.jwtService = jwtService;
        this.redisAuthService = redisAuthService;
        this.exceptionResolver = exceptionResolver;
        this.authCookieService = authCookieService;
    }

    @Value("${session.cookie.auth.name}")
    private String authCookieName;

    private String extractCookieFromRequest(@NonNull HttpServletRequest request) {
        if (request.getCookies() == null) {
            return null;
        }

        log.info("Cookies: " + Arrays.toString(request.getCookies()));

        for (var cookie : request.getCookies()) {
            if (cookie.getName().equals(authCookieName)) {
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

            var refreshedCookie = authCookieService.create(sessionId);
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
        }catch (Exception e){
            log.error("Error in CookieAuthFilter", e);
            exceptionResolver.resolveException(request, response, null, e);
        }
    }



}
