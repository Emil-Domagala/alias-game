package game.alias.room.domains;

import game.alias.player.domains.Player;
import game.alias.player.domains.PlayerMapper;
import game.alias.player.domains.dto.PlayerDto;
import game.alias.room.domains.dto.RoomDto;
import game.alias.room.domains.dto.RoomWithPlayersDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class RoomMapper {

    private final PlayerMapper playerMapper;

    public RoomDto toRoomDto(Room room, Player owner) {
        if (room == null) return null;

        return new RoomDto(
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

    public RoomWithPlayersDto toRoomWithPlayersDto(Room room, List<Player> players, Player owner) {
        if (room == null) return null;

        List<PlayerDto> playerDtos = mapPlayers(players);

        return new RoomWithPlayersDto(
                toRoomDto(room, owner),
                playerDtos
        );
    }

    private List<PlayerDto> mapPlayers(List<Player> players) {
        if (players == null || players.isEmpty()) {
            return Collections.emptyList();
        }
        return players.stream()
                .map(playerMapper::toPlayerDto)
                .toList();
    }
}