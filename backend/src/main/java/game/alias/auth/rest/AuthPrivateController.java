package game.alias.auth.rest;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import game.alias.auth.rest.services.AuthService;
import game.alias.auth.session.AuthCookieService;
import game.alias.common.ApiVersion;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(ApiVersion.V1Private + "/auth")
@RequiredArgsConstructor
public class AuthPrivateController {

    private final AuthService authService;
    private final AuthCookieService authCookieService;

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@CookieValue(name = "${session.cookie.auth.name}", required = true) String sessionId, HttpServletResponse res) {
        authService.logout(sessionId);

        return ResponseEntity.noContent().header(HttpHeaders.SET_COOKIE, authCookieService.clear().toString()).build();

    }
}
