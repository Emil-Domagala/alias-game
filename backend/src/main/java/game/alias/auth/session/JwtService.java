package game.alias.auth.session;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Duration;
import java.util.Date;
import java.util.UUID;

import javax.crypto.SecretKey;

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

    @Value("${session.cookie.auth.maxAge}")
    private Duration USER_TTL;

    @Value("${session.jwt.secret}")
    private String jwtSecret;

    public String generateSessionToken(UUID sessionId) {
        return Jwts.builder()
                .subject(sessionId.toString())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + USER_TTL.toMillis()))
                .signWith(key())
                .compact();
    }

    private Key key() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    public String validateAndGetSessionId(String token) {
        log.info("Token: " + token);

        try {
            return Jwts.parser()
                    .verifyWith((SecretKey) key())
                    .build().parseSignedClaims(token)
                    .getPayload().getSubject();
        } catch (ExpiredJwtException e) {
            log.error("ExpiredJwtException", e);
            return null;
        } catch (JwtException e) {
            log.error("JwtException", e);
            return null;
        } catch (IllegalArgumentException e) {
            log.error("IllegalArgumentException", e);
            return null;
        }
    }

}
