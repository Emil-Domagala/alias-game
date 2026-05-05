package game.alias.common.wsEvent;

public enum WsMessageType {
    // ===== ROOM =====
    ROOM_CREATED,
    ROOM_UPDATED,
    ROOM_DELETED,

    // ===== TEAM =====
    TEAM_UPDATED,

    // ===== GAME =====
    GAME_STARTED,
    GAME_STATE_UPDATED,
    GAME_ENDED
}
