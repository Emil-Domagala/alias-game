package game.alias.room.domains.dto;

import game.alias.room.domains.RoomStatus;
import game.alias.user.domains.dto.PlayerDto;

import java.util.Set;
import java.util.UUID;

public record RoomWithPlayersDto(
        UUID id,
        String name,
        UUID ownerId,
        int maxPlayers,
        int minPlayers,
        Set<PlayerDto> players,
        RoomStatus status
) {
}
