package sk.tuke.gamestudio.client.ui;

import lombok.NonNull;
import sk.tuke.gamestudio.client.pegsolitaire.game.core.board.BoardCell;
import sk.tuke.gamestudio.client.pegsolitaire.game.core.game.Game;

public interface BoardUI {
    void printBoard(@NonNull BoardCell[][] cells);

    void drawPrompt();

    void positionPrompt();

    void clearScreen();

    void start(@NonNull Game game);

    void stop();

    void exit();

    void adjustCursor(int dx, int dy);

    void select();

    void undo();
}
