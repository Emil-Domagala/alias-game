package game.alias.common;

import java.time.Instant;
import java.util.UUID;

import org.springframework.data.annotation.Id;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class BaseRedisEntity {
    @Id
    @EqualsAndHashCode.Include
    protected UUID id = UUID.randomUUID();

    protected Instant createdAt = Instant.now();
    protected Instant updatedAt = Instant.now();

    public void touch() {
        this.updatedAt = Instant.now();
    }

}
