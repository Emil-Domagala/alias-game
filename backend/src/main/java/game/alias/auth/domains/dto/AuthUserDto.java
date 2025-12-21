package game.alias.auth.domains.dto;

import java.util.Set;

import game.alias.auth.domains.UserRole;
import lombok.Data;

@Data
public class AuthUserDto {

    private String email;
    private String username;
    private Set<UserRole> roles;

}
