package pegsolitaire.core.board.events;

import pegsolitaire.core.board.commands.BoardCommand;

public interface BoardEvent {
    BoardEvent.Type getEventType();

    BoardCommand getTriggerCommand();

    enum Type {
        BOMB, LIGHTNING, TRIVIAL_MOVE, TRIVIAL_REMOVE
    }
}
