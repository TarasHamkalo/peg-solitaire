package pegsolitaire.game.core.events;

import pegsolitaire.game.core.commands.BoardCommand;

public interface BoardEvent {
    BoardEvent.Type getEventType();

    BoardCommand getTriggerCommand();

    enum Type {
        BOMB, LIGHTNING, TRIVIAL_MOVE, TRIVIAL_REMOVE
    }
}
