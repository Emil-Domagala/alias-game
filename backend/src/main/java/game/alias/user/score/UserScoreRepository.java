package game.alias.user.score;

import java.util.UUID;

import game.alias.user.score.domains.UserScore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserScoreRepository extends JpaRepository<UserScore, UUID> {

}
