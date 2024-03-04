package pegsolitaire.game.core.board.impl;

import lombok.*;
import lombok.experimental.FieldDefaults;
import pegsolitaire.game.core.Color;
import pegsolitaire.game.core.pegs.Peg;


@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BoardCell {
    @Getter
    @AllArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
    public enum State {
        OCCUPIED(Color.BLACK_BG), EMPTY(Color.BRIGHT_BLACK_BG), DESTROYED(Color.RED_BG);
        Color color;
    }

    @NonNull State state;
    Peg peg;

    public BoardCell() {
        this.state = State.EMPTY;
    }

    public BoardCell(Peg peg) {
        this.peg = peg;
        this.state = State.OCCUPIED;
    }

    public void setPeg(Peg peg) {
        if (State.DESTROYED.equals(state)) {
            return;
        }

        this.peg = peg;
        if (this.peg == null) {
            this.state = State.EMPTY;
        } else {
            this.state = State.OCCUPIED;
        }
    }

    @Override
    public String toString() {
        var bg = this.state.getColor().getCode();
        if (this.peg != null) {
            return String.format(
                "%s%s %s %s", bg, this.peg.getColor().getCode(), this.peg, Color.RESET
            );
        }

        return bg + " . " + Color.RESET;
    }
}
