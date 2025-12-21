package game.alias.room;


import game.alias.auth.AuthUser;
import game.alias.common.message.Message;
import game.alias.common.message.MessageRequest;

public interface RoomWsService {
    Message sendMessage(MessageRequest msgRequest, AuthUser user);
}
