package game.alias.common.message;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private static final UUID SYSTEM_SENDER_ID = UUID.fromString("00000000-0000-0000-0000-000000000000");

    private final MessageRepository repository;

    @Override
    public Message createSystemMessage(
            UUID conversationId,
            ConversationType conversationType,
            String content
    ) {
        Message message = Message.builder()
                .senderId(SYSTEM_SENDER_ID)
                .conversationId(conversationId)
                .conversationType(conversationType)
                .messageType(MessageType.SYSTEM)
                .content(content)
                .build();

        return repository.save(message);
    }

    @Override
    public Message createUserMessage(UUID senderId, ConversationType conversationType, MessageRequest messageRequest) {
        Message message = Message.builder()
                .senderId(senderId)
                .conversationId(messageRequest.conversationId())
                .conversationType(conversationType)
                .messageType(MessageType.USER)
                .content(messageRequest.content())
                .targetUserId(messageRequest.targetUserId())
                .build();

        return repository.save(message);
    }
}
