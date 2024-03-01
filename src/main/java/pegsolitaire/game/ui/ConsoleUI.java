package pegsolitaire.game.ui;

import lombok.NonNull;
import pegsolitaire.game.core.Game;
import pegsolitaire.game.core.board.BoardCell;

// TODO: remove stop
public interface ConsoleUI {
    void printBoard(@NonNull BoardCell[][] cells);

    void positionPrompt();

    void clearScreen();

    void start(@NonNull Game game);

    void stop();

    void exit();

    void adjustCursor(int dx, int dy);

    void select();

    void undo();
}
