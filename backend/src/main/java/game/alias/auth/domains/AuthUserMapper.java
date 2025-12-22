package game.alias.auth.domains;

import game.alias.auth.AuthUser;
import game.alias.auth.domains.dto.AuthUserDto;
import game.alias.user.domains.User;
import org.springframework.stereotype.Component;

@Component
public class AuthUserMapper {

    public AuthUserDto toAuthUserDto(User user) {
        if (user == null) {
            return null;
        }

        return new AuthUserDto(
                user.getEmail(),
                user.getUsername(),
                user.getRoles()
        );
    }

    public AuthUserDto toAuthUserDto(AuthUser authUser) {
        if (authUser == null) {
            return null;
        }

        return new AuthUserDto(
                authUser.getEmail(),
                authUser.getUsername(),
                authUser.getRoles()
        );
    }
}
