package game.alias.auth.domains.dto;

import java.util.Set;

import game.alias.auth.domains.UserRole;
import lombok.Data;

public record AuthUserDto(
        String email,
        String username,
        Set<UserRole> roles
) {
}
