package pegsolitaire.game.core.levels;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import pegsolitaire.game.core.board.BoardCell;
import pegsolitaire.game.core.pegs.PegFactory;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public abstract class LevelBuilder {
    @NonNull
    PegFactory pegFactory;

    public abstract BoardCell[][] build();
}
