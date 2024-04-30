package sk.tuke.gamestudio.pegsolitaire.core.board;

import sk.tuke.gamestudio.pegsolitaire.core.events.BoardEventManager;

public interface BoardBuilder {
  BoardBuilder eventManager(BoardEventManager eventManager);

  BoardBuilder boardCells(BoardCell[][] boardCells);

  Board build();
}
