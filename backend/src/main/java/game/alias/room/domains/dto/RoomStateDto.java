package game.alias.room.domains.dto;

import game.alias.player.domains.dto.PlayerDto;
import game.alias.room.domains.RoomStatus;
import game.alias.team.domain.dto.TeamDto;

import java.util.List;
import java.util.UUID;

public record RoomStateDto(
        UUID id,
        String name,
        PlayerDto owner,
        List<PlayerDto> players,
        int maxPlayers,
        int minPlayers,
        int playersCount,
        int numberOfTeams,
        List<TeamDto> teams,
        RoomStatus roomStatus
) {
}
