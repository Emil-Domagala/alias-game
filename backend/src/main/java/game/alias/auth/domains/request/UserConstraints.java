package game.alias.auth.domains.request;

public class UserConstraints {
    private UserConstraints() {
    }

    public static final int MIN_PASSWORD = 6;
    public static final int MAX_PASSWORD = 30;
    public static final int MIN_NICK = 3;
    public static final int MAX_NICK = 30;
}
