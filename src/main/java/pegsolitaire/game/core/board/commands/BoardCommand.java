package pegsolitaire.game.core.board.commands;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.experimental.SuperBuilder;
import pegsolitaire.game.core.board.Board;
import pegsolitaire.game.core.board.pegs.Peg;

@Data
@SuperBuilder
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
// TODO: add events from pegs
public abstract class BoardCommand {
    @NonNull Board board;
    @NonFinal Peg peg;
    @NonNull int[] initialPosition;
    @NonNull int[] finalPosition;

    /**
     * @return True if and only if the board was modified
     */
    public abstract boolean exec();

    /**
     * @return True state was restored
     */
    public abstract boolean undo();
}
