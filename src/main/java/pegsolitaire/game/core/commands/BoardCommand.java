package pegsolitaire.game.core.commands;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.experimental.SuperBuilder;
import pegsolitaire.game.core.board.impl.BoardImpl;
import pegsolitaire.game.core.pegs.Peg;

@Data
@SuperBuilder
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
// TODO: add events from pegs
public abstract class BoardCommand {
    @NonNull BoardImpl board;
    @NonFinal Peg peg;
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
