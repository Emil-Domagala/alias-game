package game.alias.room;

import game.alias.room.domains.Room;

import java.util.UUID;

public interface RoomEventPublisher {
    void playerJoined(UUID roomId, UUID userId);
    void playerLeft(UUID roomId, UUID userId);
    void roomCreated(Room room);
    void roomDeleted(Room room);
}
