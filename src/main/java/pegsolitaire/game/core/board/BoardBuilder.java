package pegsolitaire.game.core.board;

import pegsolitaire.game.core.events.BoardEventManager;

public interface BoardBuilder {
    BoardBuilder eventManager(BoardEventManager eventManager);

    BoardBuilder boardCells(BoardCell[][] boardCells);

    Board build();
}
