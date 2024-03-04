package pegsolitaire.game.ui;

import lombok.NonNull;
import pegsolitaire.game.core.board.impl.BoardCell;
import pegsolitaire.game.core.game.Game;

public interface ConsoleUI {
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
