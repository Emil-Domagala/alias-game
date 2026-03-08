package game.alias.room.domains;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import game.alias.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Formula;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Room extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private UUID ownerId;

    /**
     * IDs of Redis players
     */
    @ElementCollection
    @CollectionTable(
            name = "room_players",
            joinColumns = @JoinColumn(name = "room_id")
    )
    @Column(name = "player_id")
    @Builder.Default
    private List<UUID> playersId = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private RoomStatus status = RoomStatus.WAITING;

    @Column(nullable = false)
    private int maxPlayers;

    @Column( nullable = false)
    private int minPlayers;

    @Column(nullable = false)
    private int numberOfTeams;

    @Formula("(SELECT COUNT(rp.player_id) FROM room_players rp WHERE rp.room_id = id)")
    private int playersCount;
}
