package game.alias.room.domains;

import java.util.Set;
import java.util.UUID;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import game.alias.room.domains.dto.RoomDto;

@Mapper(componentModel = "spring")
public interface RoomMapper {

    @Mapping(target = "playersCount", source = "room.playersId", qualifiedByName = "countPlayers")
    RoomDto toDto(Room room);

    @Named("countPlayers")
    default int countPlayers(Set<UUID> playersId) {
        return playersId == null ? 0 : playersId.size();
    }

}
