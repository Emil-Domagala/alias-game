package game.alias.common.message;

import java.util.UUID;

public record MessageSendConfirmationDto(
        UUID messageId,
        UUID sentMessageTempId,
        ConversationType conversationType,
        UUID conversationId
) {
}
