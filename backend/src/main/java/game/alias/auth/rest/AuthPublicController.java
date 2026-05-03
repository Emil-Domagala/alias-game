package game.alias.auth.rest;

import game.alias.user.domains.dto.UserDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import game.alias.auth.AuthUser;
import game.alias.auth.domains.dto.AuthResponse;
import game.alias.auth.domains.dto.AuthUserDto;
import game.alias.auth.domains.request.UserLoginRequest;
import game.alias.auth.domains.request.UserRegisterRequest;
import game.alias.auth.rest.services.AuthService;
import game.alias.auth.session.AuthCookieService;
import game.alias.common.ApiVersion;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Slf4j
@RestController
@RequestMapping(ApiVersion.V1Public + "/auth")
@RequiredArgsConstructor
public class AuthPublicController {

    private final AuthService authService;
    private final AuthCookieService authCookieService;

    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@RequestBody @Valid UserRegisterRequest userRegisterRequest, HttpServletResponse res) {
        log.debug("Registering user {}", userRegisterRequest.email());

        AuthResponse authRes = authService.register(userRegisterRequest);

        var sessionCookie = authCookieService.create(authRes.sessionId());

        res.addHeader(HttpHeaders.SET_COOKIE, sessionCookie.toString());

        log.debug("authRes: {}", authRes.toString());


        return ResponseEntity.ok(authRes.UserDto());
    }

//    @PostMapping("/login")
//    public ResponseEntity<UserDto> login(@RequestBody @Valid UserLoginRequest userLoginRequest, HttpServletResponse res) {
//
//        AuthResponse authRes = authService.login(userLoginRequest);
//
//        var sessionCookie = authCookieService.create(authRes.sessionId().toString());
//
//        res.addHeader(HttpHeaders.SET_COOKIE, sessionCookie.toString());
//
//        return ResponseEntity.ok(authRes.UserDto());
//    }

    private final AuthenticationManager authManager;


    @PostMapping("/login")
    public void login(
            @RequestBody @Valid UserLoginRequest req,
            HttpServletRequest request
    ) {

        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        req.email(),
                        req.password()
                )
        );

        SecurityContext context =
                SecurityContextHolder.createEmptyContext();

        context.setAuthentication(auth);
        SecurityContextHolder.setContext(context);

        request.getSession(true); // creates Redis-backed session
    }

}
