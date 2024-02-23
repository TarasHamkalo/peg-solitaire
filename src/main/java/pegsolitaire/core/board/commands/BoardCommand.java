package pegsolitaire.core.board.commands;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import pegsolitaire.core.board.Board;
import pegsolitaire.core.board.pegs.Peg;

@Data
@Builder
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public abstract class BoardCommand {
    Board board;
    Peg peg;
    Integer[] initialPosition;
    Integer[] finalPosition;

    public abstract void exec();

    public abstract void undo();
}
