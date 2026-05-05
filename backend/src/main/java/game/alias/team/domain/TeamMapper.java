package game.alias.team.domain;

import game.alias.player.domains.Player;
import game.alias.player.domains.PlayerMapper;
import game.alias.player.domains.dto.PlayerDto;
import game.alias.team.domain.dto.TeamDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class TeamMapper {

    private final PlayerMapper playerMapper;

    public TeamDto toDto(Team team, Map<UUID, Player> playersById) {
        if (team == null) return null;

        Set<PlayerDto> players = mapPlayers(team.getPlayersId(), playersById);

        return new TeamDto(
                team.getId(),
                team.getName(),
                players,
                team.getScore(),
                team.getRequiredMinPlayers()
        );
    }

    private Set<PlayerDto> mapPlayers(Set<UUID> playerIds, Map<UUID, Player> playersById) {
        if (playerIds == null || playerIds.isEmpty()) {
            return Collections.emptySet();
        }

        return playerIds.stream()
                .map(playersById::get)
                .filter(Objects::nonNull)
                .map(playerMapper::toPlayerDto)
                .collect(Collectors.toSet());
    }
}