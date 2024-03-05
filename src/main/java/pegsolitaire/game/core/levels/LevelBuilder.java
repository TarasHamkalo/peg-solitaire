package pegsolitaire.game.core.levels;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import pegsolitaire.game.core.board.BoardCell;
import pegsolitaire.game.core.board.impl.BasicCell;
import pegsolitaire.game.core.events.BoardEvent;

import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public abstract class LevelBuilder {
    List<BoardEvent.Type> pegsEvents;

    public abstract BoardCell[][] build();
}
