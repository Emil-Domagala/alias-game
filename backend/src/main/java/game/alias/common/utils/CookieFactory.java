package game.alias.common.utils;

import game.alias.config.properties.AppConfigProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public final class CookieFactory {
    private final boolean secure;
    private final String domainName;

    public CookieFactory(
            @Value("${spring.profiles.active}") String activeProfile,
            AppConfigProperties appConfigProperties) {

        boolean isLocal = "local".equals(activeProfile);
        this.secure = !isLocal;
        this.domainName = isLocal ? null : appConfigProperties.frontend().domain();
    }

    public ResponseCookie.ResponseCookieBuilder base(@NonNull String name, @NonNull String value) {
        return ResponseCookie.from(name, value)
                .httpOnly(true)
                .secure(secure)
                .domain(domainName)
                .sameSite("Lax");
    }

}
