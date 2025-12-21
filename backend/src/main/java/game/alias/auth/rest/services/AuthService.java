package game.alias.auth.rest.services;

import org.springframework.lang.NonNull;

import game.alias.auth.domains.dto.AuthResponse;
import game.alias.auth.domains.request.UserLoginRequest;
import game.alias.auth.domains.request.UserRegisterRequest;

public interface AuthService {
    AuthResponse register(@NonNull UserRegisterRequest request);

    AuthResponse login(@NonNull UserLoginRequest request);

    void logout(@NonNull String sessionId);

}
