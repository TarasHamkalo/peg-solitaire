package pegsolitaire.game.core.board.impl;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import pegsolitaire.game.core.Direction;
import pegsolitaire.game.core.board.Board;
import pegsolitaire.game.core.board.BoardBuilder;
import pegsolitaire.game.core.board.BoardCell;
import pegsolitaire.game.core.commands.BoardCommand;
import pegsolitaire.game.core.commands.impl.MoveCommand;
import pegsolitaire.game.core.commands.impl.PutCommand;
import pegsolitaire.game.core.commands.impl.RemoveCommand;
import pegsolitaire.game.core.commands.impl.UndoMarkerCommand;
import pegsolitaire.game.core.events.BoardEventManager;
import pegsolitaire.game.core.pegs.Peg;

import java.util.*;

// TODO: destroy cell
@Data
@Builder
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BoardImpl implements Board {
    public static final int MOVE_DISTANCE = 2;
    @NonNull
    @Builder.Default
    @ToString.Exclude
    Stack<BoardCommand> history = new Stack<>();
    @NonNull
    BoardEventManager eventManager;
    @NonNull
    @NonFinal
    BoardCell[][] boardCells;

    public boolean makeMove(int[] from, int[] to) {
        // mark start of move
        execCommand(UndoMarkerCommand.builder()
            .board(this)
            .initialPosition(from)
            .finalPosition(to)
            .build()
        );


        return execCommand(MoveCommand.builder()
            .board(this)
            .initialPosition(from)
            .finalPosition(to)
            .build()
        );
    }

    public boolean putPeg(Peg peg, int[] onto) {
        return execCommand(PutCommand.builder()
            .board(this)
            .peg(peg)
            .initialPosition(onto)
            .finalPosition(onto)
            .build()
        );
    }

    public boolean removePeg(int[] from) {
        return execCommand(RemoveCommand.builder()
            .board(this)
            .initialPosition(from)
            .finalPosition(from)
            .build()
        );
    }

    public boolean undoMove() {
        while (!history.isEmpty()) {
            var command = history.pop();
            if (command instanceof UndoMarkerCommand) {
                break;
            }

            if (!command.undo()) {
                // TODO: logging
                history.clear();
                return false;
            }
        }

        return true;
    }

    public boolean destroyCell() {
        // no peg on the cell or maybe remove the cell from it also
        throw new IllegalStateException();
    }

    /**
     * @return if coordinates are valid, then nullable BasicCell on this position
     */
    public BoardCell getBoardCellAt(int x, int y) {
        if (x < 0 || y < 0 ||
            x >= boardCells[0].length || y >= boardCells.length) {
            return null;
        }

        return boardCells[y][x];
    }

    public List<int[]> getPossibleMoves(int x, int y) {
        var fromCell = getBoardCellAt(x, y);
        if (fromCell == null ||
            !fromCell.getState().equals(BoardCell.State.OCCUPIED)) {
            return Collections.emptyList();
        }

        List<int[]> moves = new ArrayList<>();
        for (Direction direction : Direction.values()) {
            int dx = x + direction.getX() * BoardImpl.MOVE_DISTANCE;
            int dy = y + direction.getY() * BoardImpl.MOVE_DISTANCE;
            int mx = (x + dx) / 2;
            int my = (y + dy) / 2;

            var boardCellDest = getBoardCellAt(dx, dy);
            var boardCellMiddle = getBoardCellAt(mx, my);
            if (boardCellDest == null || boardCellMiddle == null) {
                continue;
            }

            if (boardCellMiddle.getState().equals(BoardCell.State.OCCUPIED) &&
                boardCellDest.getState().equals(BoardCell.State.EMPTY)) {
                moves.add(new int[]{dx, dy});
            }
        }

        return moves;
    }

    public boolean isSolved() {
        return getPegsCount() == 1;
    }

    public long getPegsCount() {
        return Arrays.stream(this.boardCells)
            .flatMap(Arrays::stream)
            .parallel()
            .filter(Objects::nonNull)
            .filter(b -> b.getState().equals(BoardCell.State.OCCUPIED))
            .count();
    }

    public boolean hasAvailableMoves() {
        for (int i = 0; i < boardCells.length; i++) {
            for (int j = 0; j < boardCells[0].length; j++) {
                if (!getPossibleMoves(j, i).isEmpty()) {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean execCommand(@NonNull BoardCommand command) {
        if (command.exec()) {
            history.push(command);
            return true;
        }

        return false;
    }

    public void clearHistory() {
        this.history.clear();
    }

    public void setBoardCells(BoardCell[][] boardCells) {
        clearHistory();
        this.boardCells = boardCells;
    }

    public static class BoardImplBuilder implements BoardBuilder {
    }
}