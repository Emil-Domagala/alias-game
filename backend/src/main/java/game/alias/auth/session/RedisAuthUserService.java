package game.alias.auth.session;

import java.time.Duration;
import java.util.Objects;
import java.util.UUID;

import game.alias.auth.session.exceptions.RedisSessionException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import game.alias.auth.AuthUser;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RedisAuthUserService {
    private final StringRedisTemplate redis;
    private final ObjectMapper json;

    @Value("${session.cookie.auth.maxAge}")
    private Duration USER_TTL;

    private static final String SESSION_BASE_KEY = "session:user:";

    public AuthUser getUserFromSessionAndExtend(@NonNull String sessionId) throws RedisSessionException {
        final String key = SESSION_BASE_KEY + sessionId;

        String jsonUser = redis.opsForValue().getAndExpire(key, USER_TTL);
        if (jsonUser == null) {
            throw new RedisSessionException("No user found for session " + sessionId);
        }

        try {
            return json.readValue(jsonUser, AuthUser.class);
        } catch (Exception e) {
            redis.delete(key);
            throw new RedisSessionException("Failed to deserialize user from Redis", e);
        }

    }

    public void saveUser(@NonNull AuthUser user, @NonNull UUID sessionId) {
        try {
            String jsonUser = json.writeValueAsString(user);
            Objects.requireNonNull(jsonUser);

            redis.opsForValue().set(SESSION_BASE_KEY + sessionId, jsonUser, USER_TTL);
        } catch (Exception e) {
            throw new RedisSessionException("Failed to store user in Redis", e);
        }
    }

    public void removeUser(@NonNull String sessionId) {
        redis.delete(SESSION_BASE_KEY + sessionId);
    }

}
