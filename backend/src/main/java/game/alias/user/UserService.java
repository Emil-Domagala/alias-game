package game.alias.user;

import game.alias.user.domains.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {
    User loadOrThrow(UUID userId);
    List<User> findAllByIds(List<UUID> userIds);
}
