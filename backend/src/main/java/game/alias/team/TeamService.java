package game.alias.team;

import game.alias.team.domain.Team;

import java.util.List;
import java.util.UUID;

public interface TeamService {
    Team loadOrThrow(UUID teamId);

    List<Team> findAllByIds(List<UUID> teamIds);

    Team findByOwnerOrThrow(UUID ownerId);

    List<Team> findByRoom(UUID roomId);

    Team save(Team team);

    Team update(Team team);

    Team delete(UUID teamId);
}
