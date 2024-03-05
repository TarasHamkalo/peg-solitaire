package pegsolitaire.game.core.board;

import pegsolitaire.game.core.commands.BoardCommand;
import pegsolitaire.game.core.events.BoardEventManager;

import java.util.Stack;

public interface BoardBuilder {
    BoardBuilder history(Stack<BoardCommand> history);

    BoardBuilder eventManager(BoardEventManager eventManager);

    BoardBuilder boardCells(BoardCell[][] boardCells);

    Board build();
}
