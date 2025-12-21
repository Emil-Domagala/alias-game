package game.alias.common.message;

import game.alias.common.BaseRedisEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.redis.core.RedisHash;

import java.util.UUID;

@Getter
@Setter
@Builder
@RedisHash("message")
public class Message extends BaseRedisEntity {
    private UUID senderId;
    private UUID conversationId;
    private String content;
    private ConversationType conversationType;
    private MessageType messageType;
}
