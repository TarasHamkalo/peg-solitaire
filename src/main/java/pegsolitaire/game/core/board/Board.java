package pegsolitaire.game.core.board;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import pegsolitaire.game.core.board.commands.BoardCommand;
import pegsolitaire.game.core.board.commands.impl.MoveCommand;
import pegsolitaire.game.core.board.commands.impl.PutCommand;
import pegsolitaire.game.core.board.commands.impl.RemoveCommand;
import pegsolitaire.game.core.board.events.BoardEventManager;
import pegsolitaire.game.core.board.pegs.Peg;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

@Data
@Builder
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class Board {
    @NonNull
    @Builder.Default
    Stack<BoardCommand> history = new Stack<>();

    @NonFinal BoardCell[][] boardCells;

    BoardEventManager eventManager;

    public boolean makeMove(int[] from, int[] to) {
        // mark start of move
        execCommand(MoveCommand.builder()
            .board(this)
            .initialPosition(from)
            .finalPosition(to)
            .build()
        );

        if (!removePeg(from)) {
            return false;
        }

        if (!putPeg(history.peek().getPeg(), to)) {
            undoMove();
            return false;
        }

        return true;
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
            if (command instanceof MoveCommand) {
                break;
            }

            if (!command.undo()) {
                System.err.println("History can not be undone, restart the game");
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
     * @param position Array in format [x, y]
     * @return if position is valid, then nullable BoardCell on this position
     */
    public BoardCell getBoardCellAt(int[] position) {
        if (position.length != 2) {
            throw new IllegalArgumentException("Position has to be in format [x, y]");
        }

        if (position[0] <= 0 || position[1] <= 0 ||
            position[0] >= boardCells[0].length || position[1] >= boardCells.length) {
            return null;
        }

        return boardCells[position[1]][position[0]];
    }

    public List<int[]> getPossibleMoves(int[] from) {
        var fromCell = getBoardCellAt(from);
        if (fromCell == null ||
            !fromCell.getState().equals(BoardCell.State.OCCUPIED)) {
            return new ArrayList<>();
        }

        List<List<Integer>> distances = new ArrayList<>();
        distances.add(List.of(2, 0));
        distances.add(List.of(-2, 0));
        distances.add(List.of(0, -2));
        distances.add(List.of(0, 2));

        List<int[]> moves = new ArrayList<>();

        for (List<Integer> distance : distances) {
            var position = new int[] {distance.get(0) + from[0], distance.get(1) + from[1]};
            System.out.println(position[0] + position[1]);
            var bCellOn = getBoardCellAt(position);
            if (bCellOn != null && bCellOn.getState().equals(BoardCell.State.EMPTY)) {
                moves.add(position);
            }
        }

        return moves;
    }

    public boolean isSolved() {
        throw new IllegalStateException();
    }

    public boolean isValidMove(int[] from, int[] to) {
        throw new IllegalStateException();
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
