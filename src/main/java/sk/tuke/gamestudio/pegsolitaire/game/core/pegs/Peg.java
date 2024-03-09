package sk.tuke.gamestudio.pegsolitaire.game.core.pegs;

import sk.tuke.gamestudio.pegsolitaire.game.core.Color;
import sk.tuke.gamestudio.pegsolitaire.game.core.events.BoardEvent;


public interface Peg {
    void onMove(BoardEvent.Type type);
    BoardEvent.Type getMoveEvent();

    void onRemove(BoardEvent.Type type);
    BoardEvent.Type getRemoveEvent();

    Color getColor();
}
