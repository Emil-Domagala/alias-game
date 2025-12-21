package game.alias.room;

import game.alias.room.domains.event.RoomCreatedEvent;
import game.alias.room.domains.event.RoomDeletedEvent;
import game.alias.room.domains.event.RoomPlayerJoinedEvent;
import game.alias.room.domains.event.RoomPlayerLeftEvent;

public interface RoomEventListener {
    void onPlayerJoined(RoomPlayerJoinedEvent event);
    void onPlayerLeft(RoomPlayerLeftEvent event);
    void onRoomCreated(RoomCreatedEvent event);
    void onRoomDeleted(RoomDeletedEvent event);
}
