package game.alias.auth.session;

import game.alias.auth.AuthUser;
import game.alias.config.properties.SessionConfigProperties;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebSocketAuthInterceptor implements HandshakeInterceptor {

    private final JwtService jwtService;
    private final RedisAuthUserService redisAuthService;
    private final SessionConfigProperties sessionConfig;

    @Override
    public boolean beforeHandshake(ServerHttpRequest request,
                                   ServerHttpResponse response,
                                   WebSocketHandler wsHandler,
                                   Map<String, Object> attributes) {

        if (!(request instanceof ServletServerHttpRequest servletRequest)) {
            return false;
        }

        HttpServletRequest req = servletRequest.getServletRequest();

        String token = extractCookieFromRequest(req);
        if (token == null) return false;

        try {
            String sessionId = jwtService.validateAndGetSessionId(token);
            AuthUser authUser = redisAuthService.getUserFromSessionAndExtend(sessionId);

            if (authUser == null) return false;

            var auth = new UsernamePasswordAuthenticationToken(authUser, null, authUser.getAuthorities());

            attributes.put("SPRING.AUTHENTICATION", auth);
            return true;

        } catch (Exception e) {
            log.debug("WebSocket auth failed", e);
            return false;
        }
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, @Nullable Exception exception) {
        log.debug("Handshake completed");
    }

    private String extractCookieFromRequest(@NonNull HttpServletRequest request) {
        String authCookieName = sessionConfig.cookie().auth().name();

        if (request.getCookies() == null) {
            return null;
        }

        for (var cookie : request.getCookies()) {
            if (cookie.getName().equals(authCookieName)) {
                return cookie.getValue();
            }
        }

        return null;
    }
}