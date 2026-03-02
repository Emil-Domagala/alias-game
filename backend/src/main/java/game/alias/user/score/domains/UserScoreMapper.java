package game.alias.user.score.domains;

public final class UserScoreMapper {

    private UserScoreMapper() {}

    public static UserScoreDto toDto(UserScore score) {
        if (score == null) {
            return null;
        }

        return new UserScoreDto(
                score.getScore(),
                score.getGamesPlayed(),
                score.getGamesWon(),
                score.getGamesLost(),
                score.getGamesTied()
        );
    }
}