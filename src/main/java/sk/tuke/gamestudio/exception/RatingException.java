package sk.tuke.gamestudio.exception;

public class RatingException extends RuntimeException {
    public RatingException(String message) {
        super(message);
    }

    public RatingException(String message, Throwable cause) {
        super(message, cause);
    }
}
