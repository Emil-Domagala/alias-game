package game.alias.common;

import java.util.UUID;

public final class WebSocketDestinations {
    private WebSocketDestinations() {}

    public static final String APP_PREFIX = "/app";

    // Broadcast
    public static final String TOPIC_LOBBY = "/topic/lobby";
    public static final String TOPIC_ROOM = "/topic/room/";
    public static final String TOPIC_TEAM = "/topic/team/";

    // Private (user-scoped)
    public static final String USER_QUEUE_PRIVATE = "/queue/private";

    public static String roomTopic(UUID roomId) {
        return TOPIC_ROOM + roomId;
    }

    public static String teamTopic(UUID teamId) {
        return TOPIC_TEAM + teamId;
    }
}
