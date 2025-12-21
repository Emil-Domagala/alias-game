package game.alias.auth.session;

import java.time.Duration;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import game.alias.common.utils.CookieFactory;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthCookieService {

    @Value("${session.cookie.auth.name}")
    private String authCookieName;
    @Value("${session.cookie.auth.maxAge}")
    private Duration authCookieMaxAge;

    private final CookieFactory cookieFactory;

    public ResponseCookie create(@NonNull String token) {
        Objects.requireNonNull(authCookieName);
        Objects.requireNonNull(authCookieMaxAge);

        return cookieFactory.base(authCookieName, token)
                .path("/")
                .maxAge(authCookieMaxAge)
                .build();
    }

    public ResponseCookie clear() {
        Objects.requireNonNull(authCookieName);

        return cookieFactory.base(authCookieName, "")
                .path("/")
                .maxAge(0)
                .build();
    }
}
