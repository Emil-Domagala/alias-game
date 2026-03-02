package game.alias.user.domains.dto;

import game.alias.auth.domains.UserRole;
import game.alias.user.score.domains.UserScoreDto;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

public record UserDto(
        UUID id,
        String nick,
        String email,
        Set<UserRole>roles,
        Instant createdAt,
        Instant updatedAt,
        UserScoreDto score
) {
}
