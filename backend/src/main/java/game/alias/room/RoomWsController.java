package game.alias.room;

import game.alias.auth.AuthUser;
import game.alias.common.message.Message;
import game.alias.common.message.MessageRequest;
import game.alias.common.message.MessagesMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class RoomWsController {
    private final SimpMessagingTemplate template;
    private final RoomWsService service;
    private final MessagesMapper mapper;

    @MessageMapping(RoomWsDestinations.SEND_MESSAGE)
    public void sendMessage(@Valid @Payload MessageRequest message, @AuthenticationPrincipal AuthUser user ){
        Message msg = service.sendMessage(message, user);
        var msgDto = mapper.toMessageDto(msg);
        template.convertAndSend(msgDto.conversationId().toString(), msgDto);
    }
}
