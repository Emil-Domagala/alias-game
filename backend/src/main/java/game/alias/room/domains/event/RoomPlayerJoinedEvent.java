package game.alias.room.domains.event;

import game.alias.user.User;

import java.util.UUID;

public record RoomPlayerJoinedEvent(UUID roomId,UUID userId) {
}
