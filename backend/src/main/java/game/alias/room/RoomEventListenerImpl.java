package game.alias.room;

import game.alias.common.WebSocketDestinations;
import game.alias.room.domains.RoomException;
import game.alias.room.domains.RoomMapper;
import game.alias.room.domains.event.RoomCreatedEvent;
import game.alias.room.domains.event.RoomDeletedEvent;
import game.alias.room.domains.event.RoomPlayerJoinedEvent;
import game.alias.room.domains.event.RoomPlayerLeftEvent;
import game.alias.room.domains.event.dto.RoomCreatedEventDto;
import game.alias.room.domains.event.dto.RoomDeletedEventDto;
import game.alias.room.domains.event.dto.RoomPlayerLeftEventDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class RoomEventListenerImpl implements RoomEventListener{
    private final RoomRepository roomRepository;
    private final SimpMessagingTemplate template;
    private final RoomMapper roomMapper;


    @Override
    @EventListener(RoomPlayerJoinedEvent.class)
    public void onPlayerJoined(RoomPlayerJoinedEvent event) {
        log.info("Player joined room. roomId={}, playerId={}", event.roomId(), event.userId());
        var room = roomRepository.findById(event.roomId()).orElseThrow(()-> new RoomException("Room not found"));
        template.convertAndSend(WebSocketDestinations.roomTopic(room.getId()),new RoomPlayerJoinedEvent(event.roomId(), event.userId()));
    }

    @Override
    @EventListener(RoomPlayerLeftEvent.class)
    public void onPlayerLeft(RoomPlayerLeftEvent event) {
        log.info("Player left room. roomId={}, playerId={}", event.roomId(), event.userId());
        var room = roomRepository.findById(event.roomId()).orElseThrow(()-> new RoomException("Room not found"));
        var roomDto = roomMapper.toRoomDto(room);
        template.convertAndSend(WebSocketDestinations.roomTopic(room.getId()),new RoomPlayerLeftEventDto(roomDto));
    }

    @Override
    @EventListener(RoomCreatedEvent.class)
    public void onRoomCreated(RoomCreatedEvent event) {
        var room = roomRepository.findById(event.roomId()).orElseThrow(() -> new RoomException("Room not found"));
        log.info("Room created. roomId={}, ownerId={}, maxPlayers={}, minPlayers={}", room.getId(), room.getOwnerId(), room.getMaxPlayers(), room.getMinPlayers());
        template.convertAndSend(WebSocketDestinations.roomTopic(room.getId()),new RoomCreatedEventDto(room.getId()));
    }

    @Override
    @EventListener(RoomDeletedEvent.class)
    public void onRoomDeleted(RoomDeletedEvent event) {
        log.info("Room deleted. roomId={}", event.roomId());
        template.convertAndSend(WebSocketDestinations.roomTopic(event.roomId()),new RoomDeletedEventDto(event.roomId()));
    }
}
