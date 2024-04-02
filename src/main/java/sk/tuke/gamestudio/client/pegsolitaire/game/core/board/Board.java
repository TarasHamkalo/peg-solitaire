package sk.tuke.gamestudio.client.pegsolitaire.game.core.board;

import lombok.NonNull;
import sk.tuke.gamestudio.client.pegsolitaire.game.core.commands.BoardCommand;
import sk.tuke.gamestudio.client.pegsolitaire.game.core.events.BoardEvent;
import sk.tuke.gamestudio.client.pegsolitaire.game.core.pegs.Peg;

import java.util.List;

public interface Board {
    boolean makeMove(int[] from, int[] to);

    boolean removePeg(int[] from);

    boolean putPeg(@NonNull Peg peg, int[] onto);

    boolean destroyCell(int[] on);

    boolean undoMove();

    List<int[]> getPossibleMoves(int x, int y);

    BoardCell getBoardCellAt(int x, int y);

    long getPegsCount();

    boolean hasAvailableMoves();

    boolean isSolved();

    BoardCell[][] getBoardCells();

    void setBoardCells(BoardCell[][] boardCells);

    void offerEvent(@NonNull BoardEvent event);

    void clearHistory();

    BoardCommand peekHistory();
}