package game.alias.user.domains;


import game.alias.common.BaseRedisEntity;
import lombok.*;
import org.springframework.data.redis.core.RedisHash;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@RedisHash("player")
public class Player extends BaseRedisEntity {
    private String username;
}
