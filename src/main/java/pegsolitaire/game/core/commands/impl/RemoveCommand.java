package pegsolitaire.game.core.commands.impl;

import lombok.experimental.SuperBuilder;
import pegsolitaire.game.core.board.BoardCell;
import pegsolitaire.game.core.board.impl.BasicCell;
import pegsolitaire.game.core.commands.BoardCommand;

@SuperBuilder
public class RemoveCommand extends BoardCommand {
    @Override
    public boolean exec() {
        var removeFrom = this.getBoard().getBoardCellAt(
            this.getInitialPosition()[0], this.getInitialPosition()[1]
        );

        if (isInvalid(removeFrom)) {
            return false;
        }

        this.setPeg(removeFrom.getPeg());
        removeFrom.setPeg(null);
        return true;
    }

    @Override
    public boolean undo() {
        var putOnCell = this.getBoard().getBoardCellAt(
            this.getFinalPosition()[0], this.getFinalPosition()[1]
        );

        putOnCell.setPeg(this.getPeg());
        this.setPeg(null);
        return true;
    }


    private boolean isInvalid(BoardCell cell) {
        return cell == null || !cell.getState().equals(BasicCell.State.OCCUPIED);
    }
}
