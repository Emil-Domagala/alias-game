package game.alias.auth.rest;

import game.alias.auth.AuthUser;
import game.alias.auth.domains.request.UserLoginRequest;
import game.alias.auth.domains.request.UserRegisterRequest;
import game.alias.auth.rest.services.AuthService;
import game.alias.common.ApiVersion;
import game.alias.user.domains.User;
import game.alias.user.domains.UserMapper;
import game.alias.user.domains.dto.UserDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(ApiVersion.V1Public + "/auth")
@RequiredArgsConstructor
public class AuthPublicController {

    private final AuthService authService;
    // Inject SecurityContextRepository
    private final SecurityContextRepository securityContextRepository = new HttpSessionSecurityContextRepository();


    @PostMapping("/register")
    public ResponseEntity<UserDto> register(
            @RequestBody @Valid UserRegisterRequest userRegisterRequest,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        log.debug("Registering user {}", userRegisterRequest.email());
        User user = authService.register(userRegisterRequest);

        AuthUser authUser = new AuthUser(user.getId(), user.getEmail(), user.getNick(), user.getPassword(), user.getRoles());

        Authentication authentication = new UsernamePasswordAuthenticationToken(authUser, null, authUser.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);

        securityContextRepository.saveContext(SecurityContextHolder.getContext(), request, response);

        return ResponseEntity.ok(UserMapper.toDto(user));
    }

    @PostMapping("/login")
    public ResponseEntity<UserDto> login(
            @RequestBody @Valid UserLoginRequest req,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        User user = authService.login(req);

        AuthUser authUser = new AuthUser(user.getId(), user.getEmail(), user.getNick(), user.getPassword(), user.getRoles());

        Authentication authentication = new UsernamePasswordAuthenticationToken(authUser, null, authUser.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);

        securityContextRepository.saveContext(SecurityContextHolder.getContext(), request, response);

        return ResponseEntity.ok(UserMapper.toDto(user));
    }
}
