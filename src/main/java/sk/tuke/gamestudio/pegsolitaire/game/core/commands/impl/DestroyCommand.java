package sk.tuke.gamestudio.pegsolitaire.game.core.commands.impl;

import lombok.experimental.SuperBuilder;
import sk.tuke.gamestudio.pegsolitaire.game.core.commands.BoardCommand;

import static sk.tuke.gamestudio.pegsolitaire.game.core.board.BoardCell.State;

@SuperBuilder
public class DestroyCommand extends BoardCommand {

    @Override
    public boolean exec() {
        var cell = this.getBoard().getBoardCellAt(
            this.getInitialPosition()[0], this.getInitialPosition()[1]
        );

        if (cell == null) {
            return false;
        }

        this.getBoard().removePeg(this.getInitialPosition());

        cell.setState(State.DESTROYED);

        return true;
    }

    @Override
    public boolean undo() {
        var cell = this.getBoard().getBoardCellAt(
            this.getInitialPosition()[0], this.getInitialPosition()[1]
        );

        if (cell == null) {
            return false;
        }

        cell.setPeg(cell.getPeg());
        return true;
    }
}
