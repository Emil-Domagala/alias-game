package game.alias.player;

import game.alias.auth.AuthUser;
import game.alias.player.domains.Player;
import game.alias.user.domains.User;

import java.util.ArrayList;
import java.util.List;
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

    /**
     * Finds all {@link Player} objects for the given list of user IDs.
     * <p>
     * This method first attempts to fetch the players from the Redis cache.
     * Any missing players are loaded from the database ({@link game.alias.user.UserRepository}),
     * converted into {@link Player} objects, and then cached in Redis with a TTL of 1 hour.
     * <p>
     * The returned list preserves the same order as the input UUID list.
     *
     * @param uuids the list of user IDs to fetch players for; must not be null
     * @return a list of {@link Player} objects corresponding to the given UUIDs.
     *         If a UUID does not exist in either Redis or the database, it is skipped.
     */
    List<Player> findAllByIds(ArrayList<UUID> uuids);
}
