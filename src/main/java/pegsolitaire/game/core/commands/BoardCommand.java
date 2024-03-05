package pegsolitaire.game.core.commands;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.experimental.SuperBuilder;
import pegsolitaire.game.core.board.Board;
import pegsolitaire.game.core.pegs.Peg;

@Data
@SuperBuilder
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
// TODO: add events from pegs
public abstract class BoardCommand {
    @NonNull
    Board board;
    @NonFinal
    Peg peg;
    int[] initialPosition;
    int[] finalPosition;

    /**
     * @return True if and only if the board was modified
     */
    public abstract boolean exec();

    /**
     * @return True state was restored
     */
    public abstract boolean undo();
}
