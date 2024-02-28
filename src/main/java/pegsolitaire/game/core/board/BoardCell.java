package pegsolitaire.game.core.board;

import lombok.*;
import lombok.experimental.FieldDefaults;
import pegsolitaire.game.core.board.pegs.Peg;


@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BoardCell {
    Peg peg;
    State state;

    public BoardCell() {
        this.state = State.EMPTY;
    }

    public BoardCell(@NonNull Peg peg) {
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

    public enum State {OCCUPIED, EMPTY, DESTROYED}
}
