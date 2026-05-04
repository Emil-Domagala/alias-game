package game.alias.auth.rest.services;

import java.util.Set;

import game.alias.auth.domains.UserRole;
import game.alias.user.UserService;
import game.alias.user.domains.UserMapper;
import game.alias.user.domains.dto.UserDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import game.alias.auth.AuthUser;
import game.alias.auth.domains.request.UserLoginRequest;
import game.alias.auth.domains.request.UserRegisterRequest;
import game.alias.user.domains.User;
import game.alias.user.UserRepository;
import lombok.RequiredArgsConstructor;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UserService userService;

    @Override
    public User register(@NonNull UserRegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("Email already taken");
        }

        User user = User.builder()
                .email(request.email())
                .nick(request.nick())
                .password(passwordEncoder.encode(request.password()))
                .roles(Set.of(UserRole.USER))
                .build();

        if (user == null) {
            throw new IllegalArgumentException("User is null");
        }

        var sUser = userRepository.save(user);

        AuthUser authUser = new AuthUser(user.getId(), user.getEmail(), user.getNick(), user.getPassword(), user.getRoles());


        Authentication authentication = new UsernamePasswordAuthenticationToken(authUser,
                null,
                authUser.getAuthorities()
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        return sUser;
    }

    @Override
    public User login(@NonNull UserLoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.email(), request.password()));
        AuthUser authUser = (AuthUser) authentication.getPrincipal();

        SecurityContextHolder.getContext().setAuthentication(authentication);

        return userService.loadOrThrow(authUser.getId());
    }


}
