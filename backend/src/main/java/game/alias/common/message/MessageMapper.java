package game.alias.common.message;

import game.alias.player.PlayerService;
import game.alias.player.domains.Player;
import game.alias.player.domains.PlayerMapper;
import game.alias.player.domains.dto.PlayerDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MessageMapper {
    private final PlayerService playerService;
    private final PlayerMapper playerMapper;

    public MessageDto toMessageDto(Message message) {
        if (message == null) {
            return null;
        }

        PlayerDto senderDto = null;
        if (message.getSenderId() != null) {
            Player sender = playerService.loadExistingPlayer(message.getSenderId());
            senderDto = playerMapper.toPlayerDto(sender);
        }

        return new MessageDto(
                message.getId(),
                senderDto,
                message.getConversationId(),
                message.getContent(),
                message.getConversationType(),
                message.getMessageType(),
                message.getCreatedAt()
        );
    }
}
