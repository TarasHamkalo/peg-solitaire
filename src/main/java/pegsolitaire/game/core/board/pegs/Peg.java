package pegsolitaire.game.core.board.pegs;

import pegsolitaire.game.core.board.events.BoardEvent;

import java.awt.*;

public interface Peg {
    Peg clone();

    void onMove(BoardEvent.Type type);
    BoardEvent.Type getMoveEvent();

    void onRemove(BoardEvent.Type type);
    BoardEvent.Type getRemoveEvent();

    Color getColor();
}
