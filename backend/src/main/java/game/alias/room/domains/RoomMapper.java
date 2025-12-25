package game.alias.room.domains;

import game.alias.room.domains.dto.RoomDto;
import game.alias.room.domains.dto.RoomWithPlayersDto;
import game.alias.player.domains.Player;
import game.alias.player.domains.PlayerMapper;
import game.alias.player.domains.dto.PlayerDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class RoomMapper {
    private final PlayerMapper playerMapper;

    public RoomDto toRoomDto(Room room, Player owner) {
        if (room == null) {
            return null;
        }

        return new RoomDto(
                room.getId(),
                room.getName(),
                playerMapper.toPlayerDto(owner),
                room.getMaxPlayers(),
                room.getMinPlayers(),
                room.getPlayersId() == null ? 0 : room.getPlayersId().size(),
                room.getStatus()
        );
    }

    public RoomWithPlayersDto toRoomWithPlayersDto(Room room, Set<Player> players, Player owner) {
        if (room == null) {
            return null;
        }

        return new RoomWithPlayersDto(
                room.getId(),
                room.getName(),
                playerMapper.toPlayerDto(owner),
                room.getMaxPlayers(),
                room.getMinPlayers(),
                mapPlayers(players),
                room.getStatus()
        );
    }

    private Set<PlayerDto> mapPlayers(Set<Player> players) {
        if (players == null || players.isEmpty()) {
            return Collections.emptySet();
        }

        return players.stream()
                .map(playerMapper::toPlayerDto)
                .collect(Collectors.toSet());
    }


}
