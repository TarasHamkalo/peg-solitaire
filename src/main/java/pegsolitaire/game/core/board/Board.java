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

import java.util.Stack;

@Data
@Builder
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class Board {
    @Builder.Default Stack<BoardCommand> history = new Stack<>();
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

        if (position[0] >= boardCells[0].length || position[1] >= boardCells.length) {
            return null;
        }

        return boardCells[position[1]][position[0]];
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
    @Override
    public String toString() {
        return " ";
    }
}
