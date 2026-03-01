package game.alias.team.events;

import java.util.UUID;

public record PlayerLeftTeamEvent(UUID roomUUID, UUID teamUUID, UUID playerUUID) {
}
