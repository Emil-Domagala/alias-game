package game.alias.player;

import game.alias.auth.AuthUser;
import game.alias.player.domains.Player;
import game.alias.user.domains.User;

import java.util.Set;
import java.util.UUID;

public interface PlayerService {
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

    /**
     * Loads {@link Player} object for the given user ID.
     * <p>
     * The method attempts to resolve player from a fast-access cache first.
     * If missing player is loaded from the primary database and cached
     * for subsequent requests.
     *
     * @param userId set of user identifiers to resolve
     * @return mapping of user ID to resolved {@link Player} instances;
     *         if user do not exist {@link jakarta.persistence.EntityNotFoundException} is thrown
     */
    Player loadExistingPlayer(UUID userId);

    Player cashePlayer(AuthUser user);
}
