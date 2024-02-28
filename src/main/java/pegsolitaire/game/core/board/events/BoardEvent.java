package pegsolitaire.game.core.board.events;

import pegsolitaire.game.core.board.commands.BoardCommand;

public interface BoardEvent {
    BoardEvent.Type getEventType();

    BoardCommand getTriggerCommand();

    enum Type {
        BOMB, LIGHTNING, TRIVIAL_MOVE, TRIVIAL_REMOVE
    }
}
