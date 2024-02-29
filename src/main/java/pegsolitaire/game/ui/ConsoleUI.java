package pegsolitaire.game.ui;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import pegsolitaire.game.core.Game;
import pegsolitaire.game.core.board.Board;
import pegsolitaire.game.core.board.BoardCell;
import pegsolitaire.game.core.board.Color;

import java.util.Arrays;


@Data
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ConsoleUI {
    public static final String CSI_SEQ = "\033[";
    public static final String CLEAR_SCREEN = "\033[2J";
    public static final String POSITION_FORMAT = "%d;%dH";
    private static final Color MAIN_CURSOR = Color.BLUE;
    private static final Color POSSIBLE_MOVE_CURSOR = Color.GREEN;
    private static final Color SELECTED_CURSOR = Color.BRIGHT_MAGENTA;
    private static final int BOARD_OFFSET_Y = 1;
    private static final int BOARD_OFFSET_X = 3;

    @NonNull Game game;
    @NonNull KeyboardListener keyboardListener;
    @NonFinal
    int x;
    @NonFinal
    int y;

    public ConsoleUI(@NonNull Game game) {
        this.keyboardListener = new KeyboardListener(this);
        this.game = game;
    }

    private static void positionAtScreen(int x, int y) {
        System.out.printf(CSI_SEQ + POSITION_FORMAT, y, x);
    }

    public void start() throws NativeHookException {
        GlobalScreen.registerNativeHook();
        GlobalScreen.addNativeKeyListener(this.keyboardListener);

        this.game.start();
        var boardCells = this.game.getBoard().getBoardCells();
        this.x = (boardCells[0].length + 1) / 2;
        this.y = 0;
//        boolean running = true;
//        while (running) {
//            setup();
//            play();
//        }
    }

    public void stop() throws NativeHookException {
        GlobalScreen.removeNativeKeyListener(this.keyboardListener);
        GlobalScreen.unregisterNativeHook();
    }

    private void setup() {
    }

    private void play() {
    }

    public void select() {
        if (this.game.isPegSelected()) {
            processMove();
        }

        if (this.game.selectPeg(this.x, this.y)) {
            this.game.getPossibleMoves().forEach(pos -> {
                var cell = this.game.getBoard().getBoardCellAt(pos[0], pos[1]);
                printCharsAroundCell(
                    pos[0], pos[1], '(', ')', cell.getState().getColor(), Color.GREEN
                );
            });

            positionLogically(this.x, this.y);
        }
    }

    private void processMove() {
        if (this.game.makeMove(this.x, this.y)) {
            // TODO: implement move
        }

    }

    public void adjustCursor(int x, int y) {
        var dx = this.x + x;
        var dy = this.y + y;
        var nextCell = this.game.getBoard().getBoardCellAt(dx, dy);
        if (nextCell != null) {
            var currentCell = this.game.getBoard().getBoardCellAt(this.x, this.y);
            printCellAt(currentCell, this.x, this.y);
            printCharsAroundCell(
                dx, dy, '[', ']', nextCell.getState().getColor(), MAIN_CURSOR
            );
            positionLogically(dx, dy);

            this.x = dx;
            this.y = dy;
        }
    }

    private int[] logicalXYToScreen(int x, int y) {
        return new int[]{
            3 * (x + 1) - 2 + BOARD_OFFSET_X, y + 1 + BOARD_OFFSET_Y
        };
    }

    private void positionLogically(int x, int y) {
        var pos = logicalXYToScreen(x, y);
        positionAtScreen(pos[0], pos[1]);
    }

    private void printCellAt(BoardCell cell, int x, int y) {
        positionLogically(x, y);
        printCell(cell);
        if (this.game.isPegSelected()) {
            var selectedPos = this.game.getSelectedPegPosition();

            if (selectedPos[0] == x && selectedPos[1] == y) {
                printCharsAroundCell(x, y, '{', '}', cell.getState().getColor(), SELECTED_CURSOR);
            } else {
                this.game.getPossibleMovesFromSelectedPosition().parallelStream()
                    .filter(pos -> pos[0] == x && pos[1] == y)
                    .findAny().ifPresent(pos -> {
                        printCharsAroundCell(
                            x, y, '(', ')', cell.getState().getColor(), POSSIBLE_MOVE_CURSOR
                        );
                    });

            }
        }
    }

    public void printCharsAroundCell(int x, int y, char left, char right, Color fg, Color bg) {
        var pos = logicalXYToScreen(x, y);
        System.out.print(bg.getCode() + fg.getCode());

        positionAtScreen(pos[0], pos[1]);
        System.out.print(left);
        positionAtScreen(pos[0] + 2, pos[1]);
        System.out.print(right);

        resetColor();
    }


    private void printCell(BoardCell cell) {
        if (cell == null) {
            System.out.print("   ");
            return;
        }

        var peg = cell.getPeg();
        if (peg != null) {
            System.out.print(
                cell.getState().getColor() + peg.getColor().toString() + " o " + Color.RESET
            );
        } else {
            System.out.print(cell.getState().getColor() + " . " + Color.RESET);
        }
    }

    public void printBoard() {
        if (!game.isStarted()) {
            // LOG
            return;
        }

        clearScreen();
        printColumnNumberLine();
        BoardCell[][] boardCells = game.getBoard().getBoardCells();
        for (int i = 0; i < boardCells.length; i++) {
            System.out.printf("%s%s", Color.BRIGHT_BLACK_BG, Color.BLUE);
            System.out.printf((i + 1) + "~>");
            resetColor();

            for (int j = 0; j < boardCells[i].length; j++) {
                printCell(boardCells[i][j]);
            }

            System.out.println();
        }

        resetColor();
    }

    private void printColumnNumberLine() {
        var colsAmount = this.game.getBoard().getBoardCells()[0].length;
        System.out.printf("%s%s   ", Color.BRIGHT_BLACK_BG, Color.BLUE);
        for (int i = 0; i < colsAmount; i++) {
            System.out.printf("/%d\\", i + 1);
        }

        System.out.println();
        resetColor();
    }


    private void clearScreen() {
        System.out.print(ConsoleUI.CLEAR_SCREEN);
        System.out.print(ConsoleUI.CSI_SEQ + "H");
    }

    private void resetColor() {
        System.out.print(Color.RESET);
    }
}
