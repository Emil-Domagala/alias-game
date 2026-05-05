package game.alias.room.event;

import game.alias.common.wsEvent.WsMessage;
import game.alias.common.wsEvent.WsMessageType;
import game.alias.room.domains.dto.RoomStateDto;
import game.alias.room.domains.dto.RoomSummaryDto;
import org.springframework.stereotype.Component;

@Component
public class RoomWsMapper {
    public WsMessage<RoomStateDto> toRoomUpdated(RoomStateDto roomStateDto) {
        return new WsMessage<>(WsMessageType.ROOM_UPDATED, roomStateDto);
    }
    public WsMessage<RoomSummaryDto> toRoomCreated(RoomSummaryDto roomSummaryDto) {
        return new WsMessage<>(WsMessageType.ROOM_CREATED, roomSummaryDto);
    }
    public WsMessage<RoomSummaryDto> toRoomDeleted(RoomSummaryDto roomSummaryDto) {
        return new WsMessage<>(WsMessageType.ROOM_DELETED, roomSummaryDto);
    }

}
