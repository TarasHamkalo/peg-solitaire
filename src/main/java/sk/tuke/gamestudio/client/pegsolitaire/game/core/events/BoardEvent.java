package sk.tuke.gamestudio.client.pegsolitaire.game.core.events;

import sk.tuke.gamestudio.client.pegsolitaire.game.core.commands.BoardCommand;

public interface BoardEvent {
    BoardEvent.Type getEventType();

    BoardCommand getTriggerCommand();

    enum Type {
        BOMB, LIGHTNING, TRIVIAL_MOVE, TRIVIAL_REMOVE
    }
}
