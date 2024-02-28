package pegsolitaire.game.core.board.commands.impl;

import lombok.experimental.SuperBuilder;
import pegsolitaire.game.core.board.BoardCell;
import pegsolitaire.game.core.board.commands.BoardCommand;

@SuperBuilder
public class RemoveCommand extends BoardCommand {
    @Override
    public boolean exec() {
        var removeFrom = this.getBoard().getBoardCellAt(this.getInitialPosition());
        if (isInvalid(removeFrom)) {
            return false;
        }

        this.setPeg(removeFrom.getPeg());
        removeFrom.setPeg(null);
        return true;
    }

    @Override
    public boolean undo() {
        var putOnCell = this.getBoard().getBoardCellAt(this.getFinalPosition());
        putOnCell.setPeg(this.getPeg());
        this.setPeg(null);
        return true;
    }


    private boolean isInvalid(BoardCell cell) {
        return cell == null || !cell.getState().equals(BoardCell.State.OCCUPIED);
    }
}
