package sk.tuke.gamestudio.server.pegsolitaire.game.core.commands;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.experimental.SuperBuilder;
import sk.tuke.gamestudio.server.pegsolitaire.game.core.board.Board;
import sk.tuke.gamestudio.server.pegsolitaire.game.core.pegs.Peg;

@Data
@SuperBuilder
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public abstract class BoardCommand {

    @NonNull
    @ToString.Exclude
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
     * @return True if state was restored
     */
    public abstract boolean undo();
}
