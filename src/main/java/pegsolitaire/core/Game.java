package pegsolitaire.core;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import pegsolitaire.core.board.Board;
import pegsolitaire.core.board.events.BoardEventManager;
import pegsolitaire.core.levels.LevelBuilder;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class Game {
    Board board;
    BoardEventManager eventManager;
    LevelBuilder levelBuilder;
    Integer[] selectedPegPosition;
}
