package game.alias.team.events;

import java.util.UUID;

public record PlayerJoinedTeamEvent(UUID roomUUID, UUID teamUUID, UUID playerUUID) {
}
