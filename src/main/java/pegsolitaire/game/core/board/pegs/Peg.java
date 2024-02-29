package pegsolitaire.game.core.board.pegs;

import pegsolitaire.game.core.board.Color;
import pegsolitaire.game.core.board.events.BoardEvent;


public interface Peg {
    Peg clone();

    void onMove(BoardEvent.Type type);
    BoardEvent.Type getMoveEvent();

    void onRemove(BoardEvent.Type type);
    BoardEvent.Type getRemoveEvent();

    Color getColor();
}
