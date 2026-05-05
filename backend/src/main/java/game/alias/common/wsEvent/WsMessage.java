package game.alias.common.wsEvent;

import java.time.Instant;
import java.util.UUID;

public record WsMessage<T>(
        WsMessageType type,
        T payload,
        UUID correlationId,
        Instant createdAt
) {
    public WsMessage(WsMessageType type, T payload) {
        this(type, payload, null, Instant.now());
    }
}