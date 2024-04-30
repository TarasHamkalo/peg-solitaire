package sk.tuke.gamestudio.pegsolitaire.core.levels;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import sk.tuke.gamestudio.pegsolitaire.core.board.BoardCell;
import sk.tuke.gamestudio.pegsolitaire.core.pegs.PegFactory;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public abstract class LevelBuilder {
  @NonNull
  PegFactory pegFactory;

  public abstract BoardCell[][] build();
}
