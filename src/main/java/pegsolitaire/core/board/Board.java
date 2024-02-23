package pegsolitaire.core.board;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import pegsolitaire.core.board.commands.BoardCommand;
import pegsolitaire.core.board.events.BoardEventManager;

import java.util.Stack;

@Data
@Builder
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class Board {
    BoardEventManager eventManager;
    Stack<BoardCommand> history;
    @NonFinal BoardCell[][] boardCells;
}
