package pegsolitaire.game.core.board.events;

import pegsolitaire.game.core.board.commands.BoardCommand;

public interface BoardEventHandler {
    void handle(BoardCommand command);
}
