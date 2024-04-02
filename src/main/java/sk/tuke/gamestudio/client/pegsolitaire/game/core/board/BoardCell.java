package sk.tuke.gamestudio.client.pegsolitaire.game.core.board;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import sk.tuke.gamestudio.client.pegsolitaire.game.core.pegs.Peg;
import sk.tuke.gamestudio.client.pegsolitaire.game.core.Color;

public interface BoardCell {
    State getState();

    void setState(State state);

    Peg getPeg();

    void setPeg(Peg peg);

    @Getter
    @AllArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
    enum State {
        OCCUPIED(Color.BLACK_BG), EMPTY(Color.BRIGHT_BLACK_BG), DESTROYED(Color.RED_BG);
        Color color;
    }
}
