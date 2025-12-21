package game.alias.common.message;

import java.util.UUID;

public interface MessageService {
    Message createUserMessage(UUID senderId, UUID conversationId, ConversationType conversationType, String content);
    Message createSystemMessage(UUID conversationId, ConversationType conversationType, String content);
}
