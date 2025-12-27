package game.alias.player.domains;


import game.alias.common.BaseRedisEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@RedisHash("player")
public class Player extends BaseRedisEntity {
    private String nick;
    @TimeToLive
    protected Long ttl;
}
