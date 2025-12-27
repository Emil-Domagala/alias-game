package game.alias.team.domain;

import game.alias.common.BaseRedisEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@RedisHash("team")
public class Team extends BaseRedisEntity {
    private Set<UUID> playersId;

    @Indexed
    private UUID roomId;
    private int won;
    private int requiredMinPlayers;
}
