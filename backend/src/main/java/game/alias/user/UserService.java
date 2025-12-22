package game.alias.user;

import game.alias.user.domains.Player;
import game.alias.user.domains.User;
import game.alias.user.domains.dto.PlayerDto;

import java.util.Set;
import java.util.UUID;

public interface UserService {
    User loadOrThrow(UUID userId);

    /**
     * Loads {@link Player} objects for the given user IDs.
     * <p>
     * The method attempts to resolve players from a fast-access cache first.
     * Any missing players are loaded from the primary database and cached
     * for subsequent requests.
     *
     * @param userIds set of user identifiers to resolve
     * @return mapping of user IDs to resolved {@link Player} instances;
     *         users that do not exist are omitted
     */
    Set<Player> loadExistingPlayers(Set<UUID> userIds);

    void cashePlayer(User user);
}
