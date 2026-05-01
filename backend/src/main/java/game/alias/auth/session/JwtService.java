package game.alias.auth.session;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Duration;
import java.util.Date;
import java.util.UUID;

import javax.crypto.SecretKey;

import game.alias.auth.session.exceptions.AuthException;
import game.alias.config.properties.SessionConfigProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class JwtService {
    private final SessionConfigProperties sessionConfig;

    public String generateSessionToken(UUID sessionId) {
        Duration ttl = sessionConfig.cookie().auth().maxAge();

        return Jwts.builder()
                .subject(sessionId.toString())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + ttl.toMillis()))
                .signWith(key())
                .compact();
    }

    private Key key() {
        String secret = sessionConfig.jwt().secret();
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String validateAndGetSessionId(String token) throws JwtException, Exception {
        log.info("Token: " + token);

        try {
            return Jwts.parser()
                    .verifyWith((SecretKey) key())
                    .build().parseSignedClaims(token)
                    .getPayload().getSubject();
        } catch (JwtException e) {
            log.warn("Token expired", e);
            throw e;
        } catch (Exception e) {
            log.warn("Unknown Error Occurred:", e);
            throw e;
        }
    }

}
