package sk.tuke.gamestudio.pegsolitaire.core.commands.impl;

import lombok.experimental.SuperBuilder;
import sk.tuke.gamestudio.pegsolitaire.core.commands.BoardCommand;

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
