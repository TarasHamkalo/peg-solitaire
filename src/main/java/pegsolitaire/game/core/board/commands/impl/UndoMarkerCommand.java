package pegsolitaire.game.core.board.commands.impl;

import lombok.experimental.SuperBuilder;
import pegsolitaire.game.core.board.commands.BoardCommand;

@SuperBuilder
public class UndoMarkerCommand extends BoardCommand {

    @Override
    public boolean exec() {
        return true;
    }

    @Override
    public boolean undo() {
        return true;
    }
}
