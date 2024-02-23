package pegsolitaire.core.levels;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import pegsolitaire.core.board.BoardCell;
import pegsolitaire.core.board.events.BoardEvent;

import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public abstract class LevelBuilder {
    List<BoardEvent.Type> pegsEvents;

    public abstract BoardCell[][] build();
}
