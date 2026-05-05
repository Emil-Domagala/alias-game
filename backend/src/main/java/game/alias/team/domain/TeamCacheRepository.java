package game.alias.team.domain;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TeamCacheRepository extends CrudRepository<Team, UUID> {

    Optional<Team> findByOwnerId(UUID ownerId);

    List<Team> findAllByRoomId(UUID roomId);
}