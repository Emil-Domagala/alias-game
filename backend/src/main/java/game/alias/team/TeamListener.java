package game.alias.team;

import game.alias.room.event.RoomDeletedEvent;

public interface TeamListener {

    void onRoomDeleted(RoomDeletedEvent event);
    void onTeamCreated();
    void onTeamDeleted();
    void onPlayerJoinedTeam();
    void onPlayerLeftTeam();
    void onGameEnded();

}
