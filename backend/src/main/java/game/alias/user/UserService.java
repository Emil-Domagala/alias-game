package game.alias.user;

import java.util.UUID;

public interface UserService {
    User loadOrThrow(UUID userId);
}
