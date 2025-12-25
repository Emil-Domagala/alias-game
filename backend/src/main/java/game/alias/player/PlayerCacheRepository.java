package game.alias.player;

import game.alias.player.domains.Player;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PlayerCacheRepository extends CrudRepository<Player, UUID> {
}
