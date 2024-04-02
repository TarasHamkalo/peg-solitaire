package sk.tuke.gamestudio.client.pegsolitaire.game.core.board;

import sk.tuke.gamestudio.client.pegsolitaire.game.core.events.BoardEventManager;

public interface BoardBuilder {
    BoardBuilder eventManager(BoardEventManager eventManager);

    BoardBuilder boardCells(BoardCell[][] boardCells);

    Board build();
}
