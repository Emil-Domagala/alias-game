package game.alias.user;

import game.alias.user.domains.User;

import java.util.UUID;

public interface UserService {
    User loadOrThrow(UUID userId);
}
