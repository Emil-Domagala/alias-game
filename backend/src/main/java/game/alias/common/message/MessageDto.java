package game.alias.common.message;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public record MessageDto(
        UUID id,
        UUID senderId,
        UUID conversationId,
        String content,
        ConversationType conversationType,
        MessageType messageType,
        Instant createdAt
) {
}
