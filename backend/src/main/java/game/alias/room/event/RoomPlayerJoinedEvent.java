package game.alias.room.event;

import java.util.UUID;

public record RoomPlayerJoinedEvent(UUID roomId,UUID userId) {
}
