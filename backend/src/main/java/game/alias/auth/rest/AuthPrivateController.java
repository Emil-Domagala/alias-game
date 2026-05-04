package game.alias.auth.rest;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import game.alias.auth.rest.services.AuthService;
import game.alias.common.ApiVersion;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(ApiVersion.V1Private + "/auth")
@RequiredArgsConstructor
public class AuthPrivateController {

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest req) {
        req.getSession().invalidate();
        return ResponseEntity.noContent().build();

    }
}
