package game.alias.common.message;

import game.alias.common.BaseRedisEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.redis.core.RedisHash;

import java.util.Optional;
import java.util.UUID;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@RedisHash("message")
public class Message extends BaseRedisEntity {
    private UUID senderId;
    private UUID conversationId;
    private UUID targetUserId;
    private String content;
    private ConversationType conversationType;
    private MessageType messageType;
}
