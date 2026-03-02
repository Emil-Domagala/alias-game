package game.alias.user.domains;

import game.alias.auth.domains.UserRole;
import game.alias.user.domains.dto.UserDto;
import game.alias.user.score.domains.UserScoreMapper;

import java.util.Set;
import java.util.stream.Collectors;

public final class UserMapper {

    private UserMapper() {}

    public static UserDto toDto(User user) {
        if (user == null) {
            return null;
        }

        return new UserDto(
                user.getId(),
                user.getNick(),
                user.getEmail(),
                copyRoles(user.getRoles()),
                user.getCreatedAt(),
                user.getUpdatedAt(),
                UserScoreMapper.toDto(user.getScore())
        );
    }

    public static Set<UserDto> toDto(Set<User> users) {
        if (users == null) {
            return Set.of();
        }

        return users.stream()
                .map(UserMapper::toDto)
                .collect(Collectors.toSet());
    }

    private static Set<UserRole> copyRoles(Set<UserRole> roles) {
        return roles == null ? Set.of() : Set.copyOf(roles);
    }
}