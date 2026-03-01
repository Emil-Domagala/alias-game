package game.alias.team.domain.dto;

import game.alias.player.domains.dto.PlayerDto;
import game.alias.team.domain.TeamScore;

import java.util.Set;
import java.util.UUID;

public record TeamDto(
        UUID id,
        String name,
        Set<PlayerDto> players,
        TeamScore score,
        int requiredMinPlayers
){}
