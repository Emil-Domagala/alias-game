package game.alias.room;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import game.alias.room.domains.Room;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomCacheRepository extends CrudRepository<Room, UUID> {

    Optional<Room> findByOwnerId(UUID ownerId);
}
