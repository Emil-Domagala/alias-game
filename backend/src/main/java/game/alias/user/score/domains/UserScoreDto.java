package game.alias.user.score.domains;

public record UserScoreDto(
        Integer score,
        Integer gamesPlayed,
        Integer gamesWon,
        Integer gamesLost,
        Integer gamesTied
) {


}