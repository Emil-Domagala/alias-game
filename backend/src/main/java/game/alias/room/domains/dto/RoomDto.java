package game.alias.room.domains.dto;

import game.alias.room.domains.RoomStatus;

import java.util.Set;
import java.util.UUID;

public record RoomDto(
        UUID id,
        String name,
        UUID ownerId,
        int maxPlayers,
        int minPlayers,
        int playersCount,
        RoomStatus roomStatus) {
    public static final Set<String> ALLOWED_SORT_FIELDS = Set.of("id","name","ownerId","maxPlayers","minPlayers","playersCount");
    public static final String DEFAULT_SORT_FIELD = "playersCount";

}