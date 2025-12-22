package game.alias.common.message;

import org.springframework.stereotype.Component;

@Component
public class MessageMapper {

    public MessageDto toMessageDto(Message message) {
        if (message == null) {
            return null;
        }

        return new MessageDto(
                message.getId(),
                message.getSenderId(),
                message.getConversationId(),
                message.getContent(),
                message.getConversationType(),
                message.getMessageType(),
                message.getCreatedAt()
        );
    }
}
