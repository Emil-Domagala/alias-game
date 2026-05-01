package game.alias.auth.session;

import java.time.Duration;

import org.springframework.http.ResponseCookie;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import game.alias.common.utils.CookieFactory;
import game.alias.config.properties.SessionConfigProperties;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthCookieService {

    private final SessionConfigProperties sessionConfig;
    private final CookieFactory cookieFactory;

    public ResponseCookie create(@NonNull String token) {
        String name = sessionConfig.cookie().auth().name();
        Duration maxAge = sessionConfig.cookie().auth().maxAge();

        return cookieFactory.base(name, token)
                .path("/")
                .maxAge(maxAge)
                .build();
    }

    public ResponseCookie clear() {
        String name = sessionConfig.cookie().auth().name();

        return cookieFactory.base(name, "")
                .path("/")
                .maxAge(Duration.ZERO)
                .build();
    }
}