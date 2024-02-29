package ui;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import pegsolitaire.game.core.Game;
import pegsolitaire.game.core.board.BoardCell;


@Data
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ConsoleUI {
    @NonNull Game game;

    public void start() throws NativeHookException {
        this.game.start();
        GlobalScreen.registerNativeHook();
        GlobalScreen.addNativeKeyListener(new KeyboardListener(this));
    }

    public void select() {
        System.out.println("select");
//        this.game.selectPeg(
    }

    public void moveUp() {
        System.out.println("up");
    }
    public void moveDown() {
        System.out.println("down");
    }
    public void moveLeft() {
        System.out.println("left");
    }
    public void moveRight() {
        System.out.println("right");
    }

    public void printCursor() {
    }

    public void printBoard() {
        if (!game.isStarted()) {
            // LOG
            return;
        }

        BoardCell[][] boardCells = game.getBoard().getBoardCells();
        for (int i = 0; i < boardCells.length; i++) {
            for (int j = 0; j < boardCells[i].length; j++) {
                if (boardCells[i][j] == null) {
                    System.out.print("   ");
                    continue;
                }

                if (boardCells[i][j].getState().equals(BoardCell.State.OCCUPIED)) {
                    System.out.print(" * ");
                } else {
                    System.out.print(" . ");
                }
            }

            System.out.print("\n");
        }
    }
}
