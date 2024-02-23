package pegsolitaire.core.board;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import pegsolitaire.core.board.pegs.Peg;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class BoardCell {
    public enum State { OCCUPIED, EMPTY, DESTROYED}

    Peg peg;
    BoardCell.State state;
}
