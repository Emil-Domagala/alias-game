package game.alias.room.domains;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import lombok.Builder;
import org.springframework.data.redis.core.RedisHash;

import game.alias.common.BaseRedisEntity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.redis.core.index.Indexed;


@Builder
@Getter
@Setter
@RedisHash("room")
public class Room extends BaseRedisEntity {

    private String name;

    @Indexed
    private UUID ownerId;

    private Set<UUID> playersId = new HashSet<>();
    private int maxPlayers;
    private int minPlayers;

}
