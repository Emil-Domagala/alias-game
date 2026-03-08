package game.alias.room.domains.dto;

import java.util.List;

import game.alias.player.domains.Player;
import game.alias.room.domains.Room;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RoomWithPlayers {

    private final Room room;
    private final List<Player> players;
}