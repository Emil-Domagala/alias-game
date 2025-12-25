package game.alias.player.domains;


import game.alias.common.BaseRedisEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.redis.core.RedisHash;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@RedisHash("player")
public class Player extends BaseRedisEntity {
    private String nick;
}
