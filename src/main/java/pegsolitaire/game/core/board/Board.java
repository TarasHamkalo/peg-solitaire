package pegsolitaire.game.core.board;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import pegsolitaire.game.core.board.commands.BoardCommand;
import pegsolitaire.game.core.board.commands.impl.MoveCommand;
import pegsolitaire.game.core.board.commands.impl.PutCommand;
import pegsolitaire.game.core.board.commands.impl.RemoveCommand;
import pegsolitaire.game.core.board.commands.impl.UndoMarkerCommand;
import pegsolitaire.game.core.board.events.BoardEventManager;
import pegsolitaire.game.core.board.pegs.Peg;

import java.util.*;

// TODO: destroy cell
@Data
@Builder
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class Board {
    public static final int MOVE_DISTANCE = 2;

    @NonNull
    @Builder.Default
    Stack<BoardCommand> history = new Stack<>();

    @NonNull
    BoardEventManager eventManager;

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
     * @return if coordinates are valid, then nullable BoardCell on this position
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
            int dx = x + direction.getX() * Board.MOVE_DISTANCE;
            int dy = y + direction.getY() * Board.MOVE_DISTANCE;
            int mx = x + direction.getX() * Board.MOVE_DISTANCE / 2;
            int my = y + direction.getY() * Board.MOVE_DISTANCE / 2;

            var boardCellOn = getBoardCellAt(dx, dy);
            var boardCellMiddle = getBoardCellAt(mx, my);
            if (boardCellOn == null || boardCellMiddle == null) {
                continue;
            }

            if (boardCellMiddle.getState().equals(BoardCell.State.OCCUPIED) &&
                boardCellOn.getState().equals(BoardCell.State.EMPTY)) {
                moves.add(new int[]{dx, dy});
            }
        }

        return moves;
    }

    public boolean isSolved() {
        return Arrays.stream(this.boardCells)
            .flatMap(Arrays::stream)
            .parallel()
            .filter(b -> b.getState().equals(BoardCell.State.OCCUPIED))
            .count() == 1;
    }

    public boolean hasAvailableMoves() {
        for (int i = 0; i < boardCells.length; i++) {
            for (int j = 0; j < boardCells[0].length; j++) {
                if (!getPossibleMoves(i, j).isEmpty()) {
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
}
