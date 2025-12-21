package game.alias.auth.domains;

import org.mapstruct.Mapper;

import game.alias.auth.AuthUser;
import game.alias.auth.domains.dto.AuthUserDto;
import game.alias.user.User;

@Mapper(componentModel = "spring")
public interface AuthUserMapper {

    AuthUserDto toAuthUserDto(User user);

    AuthUserDto toAuthUserDto(AuthUser user);

}