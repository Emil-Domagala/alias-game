package game.alias.room;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import game.alias.user.domains.User;
import org.springframework.data.jpa.repository.JpaRepository;

import game.alias.room.domains.Room;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomRepository extends JpaRepository<Room, UUID>, JpaSpecificationExecutor<Room> {

    Optional<Room> findByOwnerId(UUID ownerId);

    @Query(value = """
        SELECT u.*
        FROM users u
        JOIN room_players rp ON rp.player_id = u.id
        WHERE rp.room_id = :roomId
    """, nativeQuery = true)
    List<User> findUsersInRoom(UUID roomId);

    @Query("SELECT r FROM Room r JOIN r.playersId p WHERE p = :playerId")
    Optional<Room> findByPlayerId(@Param("playerId") UUID playerId);
}
