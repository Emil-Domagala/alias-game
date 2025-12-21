package game.alias.room;

import game.alias.auth.AuthUser;
import game.alias.common.message.*;
import game.alias.room.domains.Room;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RoomWsServiceImpl implements RoomWsService{
    private final RoomService service;
    private final MessageService msgService;

    @Override
    public Message sendMessage(MessageRequest msgRequest, AuthUser user) {
        Room room = service.loadRoomOrThrow(msgRequest.conversationId());
        if(!room.getPlayersId().contains(user.getId()))
            throw new IllegalStateException("User is not a member of this room");

        return msgService.createUserMessage(user.getId(), room.getId(), ConversationType.ROOM, msgRequest.content());
    }
}
