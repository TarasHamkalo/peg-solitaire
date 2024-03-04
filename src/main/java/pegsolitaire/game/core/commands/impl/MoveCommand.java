package pegsolitaire.game.core.commands.impl;

import lombok.experimental.SuperBuilder;
import pegsolitaire.game.core.board.impl.BoardImpl;
import pegsolitaire.game.core.commands.BoardCommand;

@SuperBuilder
public class MoveCommand extends BoardCommand {

    @Override
    public boolean exec() {
        int[] from = this.getInitialPosition();
        int[] to = this.getFinalPosition();
        var board = this.getBoard();
        if (Math.abs(from[0] - to[0]) != BoardImpl.MOVE_DISTANCE &&
            Math.abs(from[1] - to[1]) != BoardImpl.MOVE_DISTANCE) {
            return false;
        }

        int[] middle = new int[]{
            (from[0] + to[0]) / 2, (from[1] + to[1]) / 2
        };

        if (!board.removePeg(from) ||
            !board.removePeg(middle) ||
            !board.putPeg(board.getHistory().peek().getPeg(), to)) {

            board.undoMove();
            return false;
        }

        return true;
    }

    @Override
    public boolean undo() {
        return true;
    }
}
