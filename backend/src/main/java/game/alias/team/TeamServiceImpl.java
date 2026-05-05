package game.alias.team;

import game.alias.team.domain.Team;
import game.alias.team.domain.TeamCacheRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TeamServiceImpl implements TeamService {
    private final TeamCacheRepository teamRepository;

    @Override
    public Team loadOrThrow(UUID teamId) {
        return teamRepository.findById(teamId).orElseThrow(() ->
                new EntityNotFoundException("Team with ID " + teamId + " not found"));
    }

    @Override
    public List<Team> findAllByIds(List<UUID> teamIds) {
        return (List<Team>) teamRepository.findAllById(teamIds);
    }

    @Override
    public Team findByOwnerOrThrow(UUID ownerId) {
        return teamRepository.findByOwnerId(ownerId).orElseThrow(()->
                new EntityNotFoundException("Team with owner ID " + ownerId + " not found"));
    }

    @Override
    public List<Team> findByRoom(UUID roomId) {
        return teamRepository.findAllByRoomId(roomId);
    }

    @Override
    public Team save(Team team) {
        return teamRepository.save(team);
    }

    @Override
    public Team update(Team team) {
        if (!teamRepository.existsById(team.getId())) {
            throw new EntityNotFoundException("Cannot update. Team with ID " + team.getId() + " not found");
        }
        return teamRepository.save(team);
    }

    @Override
    public Team delete(UUID teamId) {
        Optional<Team> team = teamRepository.findById(teamId);
        if (team.isEmpty()) {
            throw new EntityNotFoundException("Cannot delete. Team with ID " + teamId + " not found");
        }

        teamRepository.deleteById(teamId);
        return team.get();
    }
}
