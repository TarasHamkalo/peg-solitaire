package sk.tuke.gamestudio.pegsolitaire.game.core.commands.impl;

import lombok.experimental.SuperBuilder;
import sk.tuke.gamestudio.pegsolitaire.game.core.board.BoardCell;
import sk.tuke.gamestudio.pegsolitaire.game.core.board.impl.BasicCell;
import sk.tuke.gamestudio.pegsolitaire.game.core.commands.BoardCommand;

@SuperBuilder
public class PutCommand extends BoardCommand {
    @Override
    public boolean exec() {
        var putOntoCell = this.getBoard().getBoardCellAt(
            this.getFinalPosition()[0], this.getFinalPosition()[1]
        );

        if (isInvalid(putOntoCell)) {
            return false;
        }

        putOntoCell.setPeg(this.getPeg());
        return true;
    }

    @Override
    public boolean undo() {
        var putOntoCell = this.getBoard().getBoardCellAt(
            this.getFinalPosition()[0], this.getFinalPosition()[1]
        );

        putOntoCell.setPeg(null);
        return true;
    }


    private boolean isInvalid(BoardCell putOntoCell) {
        return putOntoCell == null || !putOntoCell.getState().equals(BasicCell.State.EMPTY);
    }
}
