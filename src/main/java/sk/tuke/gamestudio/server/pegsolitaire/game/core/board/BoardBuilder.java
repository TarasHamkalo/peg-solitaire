package sk.tuke.gamestudio.server.pegsolitaire.game.core.board;

import sk.tuke.gamestudio.server.pegsolitaire.game.core.events.BoardEventManager;

public interface BoardBuilder {
    BoardBuilder eventManager(BoardEventManager eventManager);

    BoardBuilder boardCells(BoardCell[][] boardCells);

    Board build();
}
