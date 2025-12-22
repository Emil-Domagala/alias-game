package game.alias.room;

import game.alias.room.domains.event.RoomCreatedEvent;
import game.alias.room.domains.event.RoomDeletedEvent;
import game.alias.room.domains.event.RoomPlayerJoinedEvent;
import game.alias.room.domains.event.RoomPlayerLeftEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RoomEventListenerImpl implements RoomEventListener{
    private final RoomRepository roomRepository;
    private final SimpMessagingTemplate template;

    @Override
    @EventListener(RoomPlayerJoinedEvent.class)
    public void onPlayerJoined(RoomPlayerJoinedEvent event) {

    }

    @Override
    @EventListener(RoomPlayerLeftEvent.class)
    public void onPlayerLeft(RoomPlayerLeftEvent event) {

    }

    @Override
    @EventListener(RoomCreatedEvent.class)
    public void onRoomCreated(RoomCreatedEvent event) {

    }

    @Override
    @EventListener(RoomDeletedEvent.class)
    public void onRoomDeleted(RoomDeletedEvent event) {

    }
}
