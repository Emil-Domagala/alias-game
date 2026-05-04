package game.alias.auth.rest.services;

import game.alias.auth.AuthUser;
import game.alias.auth.domains.request.UserLoginRequest;
import game.alias.auth.domains.request.UserRegisterRequest;
import game.alias.user.domains.User;
import game.alias.user.domains.dto.UserDto;
import org.springframework.lang.NonNull;

public interface AuthService {
    User register(@NonNull UserRegisterRequest request);

    User login(@NonNull UserLoginRequest request);
}
