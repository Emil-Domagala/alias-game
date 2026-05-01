package game.alias.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app")
public record AppConfigProperties(
        Frontend frontend
) {
    public record Frontend(
            String url,
            String domain
    ) {}
}
