package pegsolitaire.game.core;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import pegsolitaire.game.core.board.Board;
import pegsolitaire.game.core.board.events.BoardEventManager;
import pegsolitaire.game.core.levels.LevelBuilder;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class Game {
    Board board;
    BoardEventManager eventManager;
    LevelBuilder levelBuilder;
    int[] selectedPegPosition;
}
