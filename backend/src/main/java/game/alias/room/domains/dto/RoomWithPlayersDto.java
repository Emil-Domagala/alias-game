package game.alias.room.domains.dto;

import game.alias.player.domains.dto.PlayerDto;

import java.util.List;

public record RoomWithPlayersDto(
        RoomDto room,
        List<PlayerDto> players
) {
}
