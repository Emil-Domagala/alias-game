package game.alias.room;

import game.alias.common.WebSocketDestinations;
import game.alias.room.domains.Room;
import game.alias.room.domains.RoomException;
import game.alias.room.domains.RoomMapper;
import game.alias.room.event.RoomCreatedEvent;
import game.alias.room.event.RoomDeletedEvent;
import game.alias.room.event.RoomPlayerJoinedEvent;
import game.alias.room.event.RoomPlayerLeftEvent;
import game.alias.room.event.dto.RoomChangedEventDto;
import game.alias.room.event.dto.RoomDeletedEventDto;
import game.alias.room.event.dto.RoomPlayerLeftEventDto;
import game.alias.player.PlayerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class RoomEventListenerImpl implements RoomEventListener{
    private final RoomService roomService;;
    private final PlayerService playerService;
    private final SimpMessagingTemplate template;
    private final RoomMapper roomMapper;


    @Override
    @EventListener(RoomPlayerJoinedEvent.class)
    public void onPlayerJoined(RoomPlayerJoinedEvent event) {
        log.info("Player joined room. roomId={}, playerId={}", event.roomId(), event.userId());
        Room room = roomService.loadRoomOrThrow(event.roomId());

        template.convertAndSend(WebSocketDestinations.roomTopic(room.getId()),new RoomPlayerJoinedEvent(event.roomId(), event.userId()));
    }

    @Override
    @EventListener(RoomPlayerLeftEvent.class)
    public void onPlayerLeft(RoomPlayerLeftEvent event) {
        log.info("Player left room. roomId={}, playerId={}", event.roomId(), event.userId());
        Room room = roomService.loadRoomOrThrow(event.roomId());
        var owner = playerService.loadExistingPlayer(room.getOwnerId());
        var roomDto = roomMapper.toRoomDto(room, owner);

        template.convertAndSend(WebSocketDestinations.roomTopic(room.getId()),new RoomPlayerLeftEventDto(roomDto));
        template.convertAndSend(WebSocketDestinations.TOPIC_LOBBY,new RoomChangedEventDto(roomDto));
    }

    @Override
    @EventListener(RoomCreatedEvent.class)
    public void onRoomCreated(RoomCreatedEvent event) {
        Room room = roomService.loadRoomOrThrow(event.roomId());
        log.info("Room created. roomId={}, ownerId={}, maxPlayers={}, minPlayers={}", room.getId(), room.getOwnerId(), room.getMaxPlayers(), room.getMinPlayers());
        var owner = playerService.loadExistingPlayer(room.getOwnerId());
        var roomDto = roomMapper.toRoomDto(room, owner);

        template.convertAndSend(WebSocketDestinations.TOPIC_LOBBY,new RoomChangedEventDto(roomDto));
    }

    @Override
    @EventListener(RoomDeletedEvent.class)
    public void onRoomDeleted(RoomDeletedEvent event) {
        log.info("Room deleted. roomId={}", event.roomId());
        template.convertAndSend(WebSocketDestinations.roomTopic(event.roomId()),new RoomDeletedEventDto(event.roomId()));
        template.convertAndSend(WebSocketDestinations.TOPIC_LOBBY,new RoomDeletedEventDto(event.roomId()));
    }
}
