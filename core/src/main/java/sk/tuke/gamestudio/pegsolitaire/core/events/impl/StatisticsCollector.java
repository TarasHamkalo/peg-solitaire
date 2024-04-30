package sk.tuke.gamestudio.pegsolitaire.core.events.impl;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import sk.tuke.gamestudio.pegsolitaire.core.events.BoardEvent;
import sk.tuke.gamestudio.pegsolitaire.core.events.BoardEventHandler;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StatisticsCollector implements BoardEventHandler {

  long score;

  long previousEventTime;

  public void reset() {
    this.previousEventTime = System.currentTimeMillis();
    this.score = 0;
  }

  @Override
  public void handle(BoardEvent event) {
    if (BoardEvent.Type.TRIVIAL_REMOVE.equals(event.getEventType())) {
      return;
    }

    long base = switch (event.getEventType()) {
      case BOMB:
        yield 4;
      case LIGHTNING:
        yield 5;
      case TRIVIAL_MOVE:
        yield 2;
      default:
        yield 0;
    };


    var timeBetweenEvents = System.currentTimeMillis() - previousEventTime;
    if (timeBetweenEvents == 0) {
      score += base * 1000;
    } else {
      score += base * 100 * 1000 / timeBetweenEvents;
    }

    this.previousEventTime = System.currentTimeMillis();
  }
}
