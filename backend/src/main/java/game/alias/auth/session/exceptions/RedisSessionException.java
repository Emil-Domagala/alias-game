package game.alias.auth.session.exceptions;

public class RedisSessionException extends RuntimeException {
  public RedisSessionException(String message) {
    super(message);
  }
    public RedisSessionException(String message, Throwable cause) {
        super(message, cause);
    }
}
