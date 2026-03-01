package game.alias.room;

import game.alias.room.event.RoomCreatedEvent;
import game.alias.room.event.RoomDeletedEvent;
import game.alias.room.event.RoomPlayerJoinedEvent;
import game.alias.room.event.RoomPlayerLeftEvent;

public interface RoomEventListener {
    void onPlayerJoined(RoomPlayerJoinedEvent event);
    void onPlayerLeft(RoomPlayerLeftEvent event);
    void onRoomCreated(RoomCreatedEvent event);
    void onRoomDeleted(RoomDeletedEvent event);
}
