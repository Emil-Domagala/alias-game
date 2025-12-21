package game.alias.room.domains.event;

import java.util.UUID;

public record RoomPlayerLeftEvent(UUID roomId, UUID userId) {
}
