package sk.tuke.gamestudio.client.pegsolitaire.game.core.commands.impl;

import lombok.experimental.SuperBuilder;
import sk.tuke.gamestudio.client.pegsolitaire.game.core.commands.BoardCommand;
import sk.tuke.gamestudio.client.pegsolitaire.game.core.events.impl.BoardEventImpl;
import sk.tuke.gamestudio.client.pegsolitaire.game.core.pegs.Peg;

@SuperBuilder
public class MoveCommand extends BoardCommand {
    private static final int MOVE_DISTANCE = 2;

    @Override
    public boolean exec() {
        int[] from = this.getInitialPosition();
        int[] to = this.getFinalPosition();
        if (isInvalidMove(from, to)) {
            return false;
        }

        var movedPeg = swapPegs(from, to);
        if (movedPeg == null) {
            return false;
        }

        this.getBoard().offerEvent(
            BoardEventImpl.builder()
                .eventType(movedPeg.getMoveEvent())
                .triggerCommand(this)
                .build()
        );

        return true;
    }

    private boolean isInvalidMove(int[] from, int[] to) {
        int colsDiff = Math.abs(from[0] - to[0]);
        int rowsDiff = Math.abs(from[1] - to[1]);
        return !((colsDiff == 0 && rowsDiff == MOVE_DISTANCE) ||
            (rowsDiff == 0 && colsDiff == MOVE_DISTANCE));
    }

    private Peg swapPegs(int[] from, int[] to) {
        var board = this.getBoard();
        if (!board.removePeg(from)) {
            return null;
        }

        var movedPeg = board.peekHistory().getPeg();
        int[] middle = new int[]{
            (from[0] + to[0]) / 2, (from[1] + to[1]) / 2
        };

        if (!board.removePeg(middle) ||
            !board.putPeg(movedPeg, to)) {

            board.undoMove();
            return null;
        }

        return movedPeg;
    }

    @Override
    public boolean undo() {
        return true;
    }
}
