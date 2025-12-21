package game.alias.common;

import java.util.UUID;

public final class WebSocketDestinations {
    private WebSocketDestinations() {}

    public static final String APP_PREFIX = "/app";

    public static final String TOPIC_LOBBY = "/topic/lobby";
    public static final String TOPIC_ROOM = "/topic/room/";
    public static final String TOPIC_TEAM = "/topic/team/";

    public static String roomTopic(UUID roomId) {
        return TOPIC_ROOM + roomId;
    }

    public static String teamTopic(UUID teamId) {
        return TOPIC_TEAM + teamId;
    }
}
