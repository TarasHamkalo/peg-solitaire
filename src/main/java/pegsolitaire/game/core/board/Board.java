package pegsolitaire.game.core.board;

import pegsolitaire.game.core.commands.BoardCommand;
import pegsolitaire.game.core.pegs.Peg;

import java.util.List;
import java.util.Stack;

public interface Board {
    boolean makeMove(int[] from, int[] to);

    boolean removePeg(int[] from);

    boolean putPeg(Peg peg, int[] onto);

    boolean undoMove();

    List<int[]> getPossibleMoves(int x, int y);

    BoardCell getBoardCellAt(int x, int y);

    long getPegsCount();

    boolean hasAvailableMoves();

    boolean isSolved();

    BoardCell[][] getBoardCells();

    void setBoardCells(BoardCell[][] boardCells);

    void clearHistory();

    Stack<BoardCommand> getHistory();
}