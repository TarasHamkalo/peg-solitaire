package sk.tuke.gamestudio.pegsolitaire.core.pegs;

import lombok.NonNull;
import sk.tuke.gamestudio.pegsolitaire.core.events.BoardEvent;

import java.util.List;

public interface PegFactory {
  Peg getRandomPeg();

  void addIfNotPresent(@NonNull BoardEvent.Type pegEvent);

  List<BoardEvent.Type> getSelectedEvents();

  void clearPegEvents();
}
