package game.alias.team.events;

import java.util.UUID;

public record DeletedTeamEvent(UUID roomUUID, UUID teamUUID) {
}
