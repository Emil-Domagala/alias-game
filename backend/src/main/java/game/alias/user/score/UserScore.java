package game.alias.user.score;

import game.alias.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user_scores")
public class UserScore extends BaseEntity {

    @Builder.Default
    @Column(nullable = false)
    private Integer score = 0;

    @Builder.Default
    @Column(nullable = false)
    private Integer gamesPlayed = 0;

    @Builder.Default
    @Column(nullable = false)
    private Integer gamesWon = 0;

    @Builder.Default
    @Column(nullable = false)
    private Integer gamesLost = 0;

    @Builder.Default
    @Column(nullable = false)
    private Integer gamesTied = 0;

}
