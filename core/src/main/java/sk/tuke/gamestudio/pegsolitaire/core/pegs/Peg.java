package sk.tuke.gamestudio.pegsolitaire.core.pegs;

import sk.tuke.gamestudio.pegsolitaire.core.Color;
import sk.tuke.gamestudio.pegsolitaire.core.events.BoardEvent;


public interface Peg {
  void onMove(BoardEvent.Type type);

  BoardEvent.Type getMoveEvent();

  void onRemove(BoardEvent.Type type);

  BoardEvent.Type getRemoveEvent();

  Color getColor();
}
