package pegsolitaire.game.core.board.commands.impl;

import lombok.experimental.SuperBuilder;
import pegsolitaire.game.core.board.BoardCell;
import pegsolitaire.game.core.board.commands.BoardCommand;

@SuperBuilder
public class PutCommand extends BoardCommand {
    @Override
    public boolean exec() {
        var pos = this.getFinalPosition();
        var putOntoCell = this.getBoard().getBoardCellAt(pos[0], pos[1]);
        if (isInvalid(putOntoCell)) {
            return false;
        }

        putOntoCell.setPeg(this.getPeg());
        return true;
    }

    @Override
    public boolean undo() {
        var pos = this.getFinalPosition();
        var putOntoCell = this.getBoard().getBoardCellAt(pos[0], pos[1]);
        putOntoCell.setPeg(null);
        return true;
    }


    private boolean isInvalid(BoardCell putOntoCell) {
        return putOntoCell == null || !putOntoCell.getState().equals(BoardCell.State.EMPTY);
    }
}
