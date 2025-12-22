package game.alias.auth.rest.services;

import java.util.UUID;

import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import game.alias.auth.AuthUser;
import game.alias.auth.domains.AuthUserMapper;
import game.alias.auth.domains.dto.AuthResponse;
import game.alias.auth.domains.request.UserLoginRequest;
import game.alias.auth.domains.request.UserRegisterRequest;
import game.alias.auth.session.JwtService;
import game.alias.auth.session.RedisAuthUserService;
import game.alias.user.domains.User;
import game.alias.user.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RedisAuthUserService redisAuthService;
    private final AuthUserMapper authUserMapper;

    @Override
    public AuthResponse register(@NonNull UserRegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("Email already taken");
        }

        User user = User.builder()
                .email(request.email())
                .username(request.username())
                .password(passwordEncoder.encode(request.password()))
                .build();

        if (user == null) {
            throw new IllegalArgumentException("User is null");
        }

        var sUser = userRepository.save(user);

        AuthUser authUser = new AuthUser(sUser.getId(), sUser.getEmail(), sUser.getPassword(), sUser.getRoles());

        var sessionId = createSessionTokenAndSaveUserToRedis(authUser);

        return new AuthResponse(sessionId, authUserMapper.toAuthUserDto(authUser));
    }

    @Override
    public AuthResponse login(@NonNull UserLoginRequest request) {
        var auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()));

        var sessionId = createSessionTokenAndSaveUserToRedis((AuthUser) auth.getPrincipal());

        return new AuthResponse(sessionId, authUserMapper.toAuthUserDto((AuthUser) auth.getPrincipal()));
    }

    private String createSessionTokenAndSaveUserToRedis(@NonNull AuthUser user) {
        UUID sessionId = UUID.randomUUID();
        String token = jwtService.generateSessionToken(sessionId);
        redisAuthService.saveUser(user, sessionId);

        return token;
    }

    @Override
    public void logout(@NonNull String sessionId) {
        redisAuthService.removeUser(sessionId);
        return;
    }

}
