package game.alias.common.message;

import game.alias.player.domains.dto.PlayerDto;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public record MessageDto(
        UUID id,
        PlayerDto sender,
        UUID conversationId,
        String content,
        ConversationType conversationType,
        MessageType messageType,
        Instant createdAt
) {
}
