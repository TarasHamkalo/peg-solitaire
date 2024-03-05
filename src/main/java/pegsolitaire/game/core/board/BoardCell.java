package pegsolitaire.game.core.board;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import pegsolitaire.game.core.Color;
import pegsolitaire.game.core.pegs.Peg;

public interface BoardCell {
    @Getter
    @AllArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
    enum State {
        OCCUPIED(Color.BLACK_BG), EMPTY(Color.BRIGHT_BLACK_BG), DESTROYED(Color.RED_BG);
        Color color;
    }

    State getState();

    void setState(State state);

    Peg getPeg();

    void setPeg(Peg peg);
}
