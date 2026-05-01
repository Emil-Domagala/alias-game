package game.alias.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@ConfigurationProperties(prefix = "session")
public record SessionConfigProperties(
        Cookie cookie,
        Jwt jwt
) {

    public record Cookie(
            Auth auth
    ) {}

    public record Auth(
            String name,
            Duration maxAge
    ) {}

    public record Jwt(
            String secret
    ) {}
}