package game.alias.room.domains;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import lombok.*;
import org.springframework.data.redis.core.RedisHash;

import game.alias.common.BaseRedisEntity;
import org.springframework.data.redis.core.index.Indexed;


@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@RedisHash("room")
public class Room extends BaseRedisEntity {

    private String name;

    @Indexed
    private UUID ownerId;

    private Set<UUID> playersId = new HashSet<>();
    private RoomStatus status = RoomStatus.WAITING;
    private int maxPlayers;
    private int minPlayers;
}
