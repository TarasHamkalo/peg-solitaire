package sk.tuke.gamestudio.pegsolitaire.core.events;

import sk.tuke.gamestudio.pegsolitaire.core.commands.BoardCommand;

public interface BoardEvent {
    BoardEvent.Type getEventType();

    BoardCommand getTriggerCommand();

    enum Type {
        BOMB, LIGHTNING, TRIVIAL_MOVE, TRIVIAL_REMOVE
    }
}
