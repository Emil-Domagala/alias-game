package game.alias.room.domains.dto;

import game.alias.room.domains.RoomStatus;
import game.alias.player.domains.dto.PlayerDto;

import java.util.Set;
import java.util.UUID;

public record RoomWithPlayersDto(
        UUID id,
        String name,
        PlayerDto owner,
        int maxPlayers,
        int minPlayers,
        Set<PlayerDto> players,
        RoomStatus status
) {
}
