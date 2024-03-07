package pegsolitaire.game.core.pegs;

import pegsolitaire.game.core.Color;
import pegsolitaire.game.core.events.BoardEvent;


public interface Peg {
    void onMove(BoardEvent.Type type);
    BoardEvent.Type getMoveEvent();

    void onRemove(BoardEvent.Type type);
    BoardEvent.Type getRemoveEvent();

    Color getColor();
}
