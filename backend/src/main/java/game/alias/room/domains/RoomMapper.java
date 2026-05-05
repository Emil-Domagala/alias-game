package game.alias.room.domains;

import game.alias.player.domains.Player;
import game.alias.player.domains.PlayerMapper;
import game.alias.player.domains.dto.PlayerDto;
import game.alias.room.domains.dto.RoomStateDto;
import game.alias.room.domains.dto.RoomSummaryDto;
import game.alias.team.domain.Team;
import game.alias.team.domain.TeamMapper;
import game.alias.team.domain.dto.TeamDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class RoomMapper {

    private final PlayerMapper playerMapper;
    private final TeamMapper teamMapper;


    public RoomSummaryDto toRoomSummaryDto(Room room, Player owner) {
        if (room == null) return null;

        return new RoomSummaryDto(
                room.getId(),
                room.getName(),
                playerMapper.toPlayerDto(owner),
                room.getMaxPlayers(),
                room.getMinPlayers(),
                room.getPlayersId() == null ? 0 : room.getPlayersId().size(),
                room.getStatus(),
                room.getNumberOfTeams()
        );
    }

    public RoomStateDto toState(Room room, Player owner, List<Player> players, List<Team> teams) {
        if (room == null) return null;

        Map<UUID, Player> playersById = players.stream().collect(Collectors.toMap(Player::getId, p -> p));

        List<PlayerDto> playerDtos = players.stream().map(playerMapper::toPlayerDto).toList();

        List<TeamDto> teamDtos = teams.stream().map(team -> teamMapper.toDto(team, playersById)).toList();

        return new RoomStateDto(
                room.getId(),
                room.getName(),
                playerMapper.toPlayerDto(owner),
                playerDtos,
                room.getMaxPlayers(),
                room.getMinPlayers(),
                playerDtos.size(),
                room.getNumberOfTeams(),
                teamDtos,
                room.getStatus()
        );
    }
}