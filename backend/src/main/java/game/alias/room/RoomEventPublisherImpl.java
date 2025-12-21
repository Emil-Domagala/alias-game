package game.alias.room;

import game.alias.room.domains.Room;
import game.alias.room.domains.event.RoomCreatedEvent;
import game.alias.room.domains.event.RoomDeletedEvent;
import game.alias.room.domains.event.RoomPlayerJoinedEvent;
import game.alias.room.domains.event.RoomPlayerLeftEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class RoomEventPublisherImpl implements RoomEventPublisher {
    private final ApplicationEventPublisher publisher;
    @Override
    public void playerJoined(UUID roomId, UUID userId) {
        publisher.publishEvent(new RoomPlayerJoinedEvent(roomId, userId));
    }

    @Override
    public void playerLeft(UUID roomId, UUID userId) {
        publisher.publishEvent(new RoomPlayerLeftEvent(roomId, userId));
    }

    @Override
    public void roomCreated(Room room) {
        publisher.publishEvent(new RoomCreatedEvent(room.getId()));
    }

    @Override
    public void roomDeleted(Room room) {
        publisher.publishEvent(new RoomDeletedEvent(room.getId()));
    }
}
