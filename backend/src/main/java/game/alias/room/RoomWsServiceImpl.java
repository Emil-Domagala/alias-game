package game.alias.room;

import game.alias.auth.AuthUser;
import game.alias.common.message.*;
import game.alias.room.domains.Room;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoomWsServiceImpl implements RoomWsService{
    private final RoomService service;
    private final MessageService msgService;

    @Override
    @Transactional
    public Message sendMessage(MessageRequest msgRequest, AuthUser user) {
        Room room = service.loadRoomOrThrow(msgRequest.conversationId());
        if(!room.getPlayersId().contains(user.getId())) {
            throw new IllegalStateException("You are not a member of this room");
        }
        if (msgRequest.targetUserId() != null && !room.getPlayersId().contains(msgRequest.targetUserId())) {
            throw new IllegalStateException("Recipient is not in this room");
        }

        return msgService.createUserMessage(user.getId(), ConversationType.ROOM, msgRequest);
    }
}
