package sk.tuke.gamestudio.pegsolitaire.core.game;

import sk.tuke.gamestudio.pegsolitaire.core.levels.LevelBuilder;
import sk.tuke.gamestudio.pegsolitaire.core.board.Board;

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

    long getScore();
}
