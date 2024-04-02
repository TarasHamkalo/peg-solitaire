package sk.tuke.gamestudio.client.pegsolitaire.game.core.pegs;

import sk.tuke.gamestudio.client.pegsolitaire.game.core.events.BoardEvent;
import sk.tuke.gamestudio.client.pegsolitaire.game.core.Color;


public interface Peg {
    void onMove(BoardEvent.Type type);
    BoardEvent.Type getMoveEvent();

    void onRemove(BoardEvent.Type type);
    BoardEvent.Type getRemoveEvent();

    Color getColor();
}
