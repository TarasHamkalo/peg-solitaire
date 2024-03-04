package pegsolitaire.game.core.board;

import pegsolitaire.game.core.board.impl.BoardCell;

import java.util.List;

public interface Board {
    boolean makeMove(int[] from, int[] to);

    boolean undoMove();

    List<int[]> getPossibleMoves(int x, int y);

    BoardCell getBoardCellAt(int x, int y);

    long getPegsCount();

    boolean hasAvailableMoves();

    boolean isSolved();

    BoardCell[][] getBoardCells();

    void setBoardCells(BoardCell[][] boardCells);

    void clearHistory();
}