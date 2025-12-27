package game.alias.room.domains.dto;

import game.alias.room.domains.RoomStatus;
import game.alias.player.domains.dto.PlayerDto;

import java.util.Set;
import java.util.UUID;

public record RoomDto(
        UUID id,
        String name,
        PlayerDto owner,
        int maxPlayers,
        int minPlayers,
        int playersCount,
        RoomStatus roomStatus,
        int numberOfTeams) {
    public static final Set<String> ALLOWED_SORT_FIELDS = Set.of("id","name","ownerId","maxPlayers","minPlayers","playersCount");
    public static final String DEFAULT_SORT_FIELD = "playersCount";

}