package pegsolitaire.core.board.events;

import pegsolitaire.core.board.commands.BoardCommand;

public interface BoardEventHandler {
    void handle(BoardCommand command);
}
