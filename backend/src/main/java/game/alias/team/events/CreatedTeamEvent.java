package game.alias.team.events;

import java.util.UUID;

public record CreatedTeamEvent(UUID roomUUID, UUID teamUUID) {
}
