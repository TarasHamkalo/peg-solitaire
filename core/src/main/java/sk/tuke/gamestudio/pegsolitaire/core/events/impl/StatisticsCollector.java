package sk.tuke.gamestudio.pegsolitaire.core.events.impl;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import sk.tuke.gamestudio.pegsolitaire.core.events.BoardEvent;
import sk.tuke.gamestudio.pegsolitaire.core.events.BoardEventHandler;

import java.util.concurrent.atomic.AtomicLong;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StatisticsCollector implements BoardEventHandler {

  AtomicLong score = new AtomicLong(0);
  AtomicLong previousEventTime = new AtomicLong(System.currentTimeMillis());

  public void reset() {
    previousEventTime.set(System.currentTimeMillis());
    score.set(0);
  }

  @Override
  public void handle(BoardEvent event) {
    if (BoardEvent.Type.TRIVIAL_REMOVE.equals(event.getEventType())) {
      return;
    }

    long base = switch (event.getEventType()) {
      case BOMB -> 4;
      case LIGHTNING -> 5;
      case TRIVIAL_MOVE -> 2;
      default -> 0;
    };

    long currentTime = System.currentTimeMillis();
    long timeBetweenEvents = currentTime - previousEventTime.getAndSet(currentTime);

    if (timeBetweenEvents == 0) {
      score.addAndGet(base * 100);
    } else {
      score.addAndGet(base * 10 * 1000 / timeBetweenEvents);
    }
  }

  public long getScore() {
    return score.get();
  }
}
