package game.alias.auth.session;

import java.io.IOException;
import java.util.Arrays;

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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class CookieAuthFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final RedisAuthUserService redisAuthService;

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
        var token = extractCookieFromRequest(request);

        if (token == null) {
            filterChain.doFilter(request, response);
            log.info("No token recived");
            return;
        }

        String sessionId = jwtService.validateAndGetSessionId(token);

        if (sessionId == null) {
            filterChain.doFilter(request, response);
            log.info("No session recived");
            return;
        }

        AuthUser user = redisAuthService.getUserFromSessionAndExtend(sessionId);

        if (user == null) {
            log.info("No user found");
            filterChain.doFilter(request, response);
            return;
        }

        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(user, null,
                user.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(auth);

        filterChain.doFilter(request, response);
    }

}
