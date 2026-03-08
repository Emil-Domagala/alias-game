package game.alias.room.domains.dto;

import java.util.List;

import game.alias.room.domains.Room;
import game.alias.user.domains.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RoomWithUsers {

    private final Room room;
    private final List<User> users;
}