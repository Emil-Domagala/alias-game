package game.alias.common;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;

import org.springframework.data.redis.core.TimeToLive;

@Getter
@Setter
@SuperBuilder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
public class BaseRedisEntity {
    @Id
    @EqualsAndHashCode.Include
    @Builder.Default
    protected UUID id = UUID.randomUUID();

    @Builder.Default
    protected Instant createdAt = Instant.now();

    @TimeToLive
    protected Duration ttl;
}
