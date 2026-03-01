package game.alias.room.event;

import java.util.UUID;

public record RoomPlayerLeftEvent(UUID roomId, UUID userId) {
}
