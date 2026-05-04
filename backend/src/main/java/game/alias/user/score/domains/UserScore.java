package game.alias.user.score.domains;

import game.alias.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

@Getter
@Setter
@Entity
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user_scores")
public class UserScore extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

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
    private Integer gamesTied = 0;

    @Builder.Default
    @Column(nullable = false)
    private Integer gamesLost = 0;

}
