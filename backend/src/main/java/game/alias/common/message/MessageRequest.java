package game.alias.common.message;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record MessageRequest(
       @NotNull(message = "conversationId is required") UUID conversationId,
       @NotBlank(message = "Content is required") @Size(min = MessageConstraints.MIN_CONTENT_SIZE, max = MessageConstraints.MAX_CONTENT_SIZE) String content
) {
}
