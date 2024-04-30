package sk.tuke.gamestudio.server.game.events;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import sk.tuke.gamestudio.pegsolitaire.core.events.BoardEvent;
import sk.tuke.gamestudio.pegsolitaire.core.events.BoardEventHandler;

import java.util.Set;

import static sk.tuke.gamestudio.pegsolitaire.core.events.BoardEvent.Type.TRIVIAL_MOVE;
import static sk.tuke.gamestudio.pegsolitaire.core.events.BoardEvent.Type.TRIVIAL_REMOVE;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventsDetector implements BoardEventHandler {

  boolean eventPublished = false;

  @Override
  public void handle(BoardEvent event) {
    this.eventPublished =
      !Set.of(TRIVIAL_MOVE, TRIVIAL_REMOVE).contains(event.getEventType());
  }
}
