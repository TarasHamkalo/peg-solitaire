package sk.tuke.gamestudio.pegsolitaire.game.core.board.impl;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import sk.tuke.gamestudio.pegsolitaire.game.core.Color;
import sk.tuke.gamestudio.pegsolitaire.game.core.board.BoardCell;
import sk.tuke.gamestudio.pegsolitaire.game.core.pegs.Peg;


@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BasicCell implements BoardCell {

    @NonNull
    State state;

    Peg peg;

    public BasicCell() {
        this.state = State.EMPTY;
    }

    public BasicCell(Peg peg) {
        this.peg = peg;
        this.state = State.OCCUPIED;
    }

    public void setPeg(Peg peg) {
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
