package sk.tuke.gamestudio.server.game.exception;

import org.springframework.http.HttpStatusCode;
import org.springframework.web.server.ResponseStatusException;

public class PegSolitaireException extends ResponseStatusException {

  public PegSolitaireException(HttpStatusCode status) {
    super(status);
  }

  public PegSolitaireException(HttpStatusCode status, String reason) {
    super(status, reason);
  }
}
