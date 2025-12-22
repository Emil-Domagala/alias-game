package game.alias.room.domains.event;

import java.util.UUID;

public record RoomPlayerJoinedEvent(UUID roomId,UUID userId) {
}
