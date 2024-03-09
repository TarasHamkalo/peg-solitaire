package sk.tuke.gamestudio.pegsolitaire.game.core.game;

import sk.tuke.gamestudio.pegsolitaire.game.core.board.Board;
import sk.tuke.gamestudio.pegsolitaire.game.core.levels.LevelBuilder;

import java.util.List;

public interface Game {
    void start();

    boolean makeMove(int x, int y);

    boolean undoMove();

    boolean selectPeg(int x, int y);

    boolean isPegSelected();

    void stop();

    boolean isStarted();

    List<int[]> getPossibleMoves();

    Board getBoard();

    void setLevelBuilder(LevelBuilder selectedLevel);
}
