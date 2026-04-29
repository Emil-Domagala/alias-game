package game.alias.common.message;

import java.util.UUID;

public interface MessageService {
    Message createUserMessage(UUID senderId, ConversationType conversationType, MessageRequest messageRequest);
    Message createSystemMessage(UUID conversationId, ConversationType conversationType, String content);
}
