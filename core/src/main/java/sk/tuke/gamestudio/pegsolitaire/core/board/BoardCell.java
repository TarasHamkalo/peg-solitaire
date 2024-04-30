package sk.tuke.gamestudio.pegsolitaire.core.board;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import sk.tuke.gamestudio.pegsolitaire.core.Color;
import sk.tuke.gamestudio.pegsolitaire.core.pegs.Peg;

public interface BoardCell {
  State getState();

  void setState(State state);

  Peg getPeg();

  void setPeg(Peg peg);

  boolean isEmpty();

  @Getter
  @AllArgsConstructor
  @FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
  enum State {
    OCCUPIED(Color.BLACK_BG), EMPTY(Color.BRIGHT_BLACK_BG), DESTROYED(Color.RED_BG);
    Color color;
  }
}
