package game.alias.common.message;

import game.alias.common.BaseRedisEntity;
import lombok.*;
import org.springframework.data.redis.core.RedisHash;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@RedisHash("message")
public class Message extends BaseRedisEntity {
    private UUID senderId;
    private UUID conversationId;
    private String content;
    private ConversationType conversationType;
    private MessageType messageType;
}
