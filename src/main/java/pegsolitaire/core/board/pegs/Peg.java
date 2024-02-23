package pegsolitaire.core.board.pegs;

import pegsolitaire.core.board.events.BoardEvent;

import java.awt.*;

public interface Peg {
    Peg clone(Peg src);

    void onMove(BoardEvent.Type type);
    BoardEvent.Type getMoveEvent();

    void onRemove(BoardEvent.Type type);
    BoardEvent.Type getRemoveEvent();

    Color getColor();
}
