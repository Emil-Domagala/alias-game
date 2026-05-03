package game.alias.room;

import game.alias.auth.AuthUser;
import game.alias.common.WebSocketDestinations;
import game.alias.common.currentUser.CurrentUser;
import game.alias.common.message.Message;
import game.alias.common.message.MessageMapper;
import game.alias.common.message.MessageRequest;
import game.alias.common.message.MessageSendConfirmationDto;
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
    private final MessageMapper mapper;

    @MessageMapping(RoomWsDestinations.SEND_MESSAGE)
    public void sendMessage(@Valid @Payload MessageRequest message, @CurrentUser AuthUser currentUser, @AuthenticationPrincipal AuthUser d){

        Message msg = service.sendMessage(message, currentUser);
        var msgDto = mapper.toMessageDto(msg);
        var messageSendConfirmationDto =  mapper.toMessageSendConfirmationDto(message.tempId(), msg);

        if(message.targetUserId() == null){
            template.convertAndSend(WebSocketDestinations.roomTopic(msgDto.conversationId()), msgDto);
        }else {
            template.convertAndSendToUser(message.targetUserId().toString(), WebSocketDestinations.USER_QUEUE_PRIVATE, msgDto);
        }
        template.convertAndSendToUser(currentUser.getId().toString(), WebSocketDestinations.USER_QUEUE_PRIVATE, messageSendConfirmationDto);
    }
}
