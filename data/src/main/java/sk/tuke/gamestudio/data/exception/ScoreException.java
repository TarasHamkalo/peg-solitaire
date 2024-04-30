package sk.tuke.gamestudio.data.exception;

public class ScoreException extends RuntimeException {
  public ScoreException(String message) {
    super(message);
  }

  public ScoreException(String message, Throwable cause) {
    super(message, cause);
  }
}
