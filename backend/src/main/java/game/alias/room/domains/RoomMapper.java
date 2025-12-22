package game.alias.room.domains;

import game.alias.room.domains.dto.RoomDto;
import game.alias.room.domains.dto.RoomWithPlayersDto;
import game.alias.user.domains.Player;
import game.alias.user.domains.dto.PlayerDto;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class RoomMapper {

    public RoomDto toRoomDto(Room room) {
        if (room == null) {
            return null;
        }

        return new RoomDto(
                room.getId(),
                room.getName(),
                room.getOwnerId(),
                room.getMaxPlayers(),
                room.getMinPlayers(),
                room.getPlayersId() == null ? 0 : room.getPlayersId().size(),
                room.getStatus()
        );
    }

    public RoomWithPlayersDto toRoomWithPlayersDto(Room room, Set<Player> players) {
        if (room == null) {
            return null;
        }

        return new RoomWithPlayersDto(
                room.getId(),
                room.getName(),
                room.getOwnerId(),
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
                .map(this::toPlayerDto)
                .collect(Collectors.toSet());
    }

    private PlayerDto toPlayerDto(Player player) {
        if (player == null) {
            return null;
        }

        return new PlayerDto(player.getId(), player.getUsername());
    }
}
