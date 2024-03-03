package pegsolitaire.game.ui.impl;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import pegsolitaire.game.core.Game;
import pegsolitaire.game.core.board.BoardCell;
import pegsolitaire.game.core.board.Color;
import pegsolitaire.game.ui.ConsoleUI;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ConsoleUIImpl implements ConsoleUI {
    private static final Color MAIN_CURSOR = Color.BLUE;
    private static final Color POSSIBLE_MOVE_CURSOR = Color.GREEN;
    private static final Color SELECTED_CURSOR = Color.BRIGHT_MAGENTA;

    private static final int POSSIBLE_MOVE_CODE = 1;
    private static final int SELECTED_CURSOR_CODE = 2;
    private static final int BOARD_OFFSET_Y = 0;
    private static final int BOARD_OFFSET_X = 0;

    Game game;
    KeyboardListener keyboardListener;
    int[][] boardSelections;
    int x;
    int y;

    public ConsoleUIImpl() throws NativeHookException {
        GlobalScreen.registerNativeHook();
    }

    private static void positionAtScreen(int x, int y) {
        System.out.printf("\033[%d;%dH", y, x);
    }

    public void start(@NonNull Game game) {
        this.game = game;
        this.keyboardListener = new KeyboardListener(this);
        GlobalScreen.addNativeKeyListener(this.keyboardListener);

        this.game.start();

        var cells = this.game.getBoard().getBoardCells();

        this.boardSelections = new int[cells.length][cells[0].length];
        this.x = (cells[0].length) / 2;
        this.y = cells.length / 2;

        hideCursor();
        clearScreen();
        printBoard(cells);
        printCursor(x, y, MAIN_CURSOR);
    }

    @Override
    public void stop() {
        GlobalScreen.removeNativeKeyListener(this.keyboardListener);
        this.game.stop();
        clearScreen();
        showCursor();
    }

    @Override
    @SneakyThrows
    public void exit() {
        stop();
        GlobalScreen.unregisterNativeHook();
    }

    /*
        Cursor adjustment
    */
    @Override
    public void adjustCursor(int x, int y) {
        var dx = this.x + x;
        var dy = this.y + y;
        var nextCell = this.game.getBoard().getBoardCellAt(dx, dy);
        if (nextCell != null) {
            var currentCell = this.game.getBoard().getBoardCellAt(this.x, this.y);

            saveCursor();

            printCellAt(currentCell, this.x, this.y);
            printCursor(dx, dy, MAIN_CURSOR);

            restoreCursor();

            this.x = dx;
            this.y = dy;
        }
    }

    private void printCellAt(BoardCell cell, int x, int y) {
        int[] pos = logicalXYToScreen(this.x, this.y);
        positionAtScreen(pos[0], pos[1]);
        System.out.print(cell);
        if (this.boardSelections[y][x] == POSSIBLE_MOVE_CODE) {
            printCursor(x, y, POSSIBLE_MOVE_CURSOR);
        } else if (this.boardSelections[y][x] == SELECTED_CURSOR_CODE) {
            printCursor(x, y, SELECTED_CURSOR);
        }
    }

    @Override
    public void select() {
        if (processMove()) {
            return;
        }

        if (this.game.selectPeg(x, y)) {
            this.boardSelections[y][x] = 2;
            this.saveCursor();

            this.printCursor(x, y, SELECTED_CURSOR);
            this.game.getPossibleMoves().forEach(pos -> {
                this.boardSelections[pos[1]][pos[0]] = 1;
                this.printCursor(pos[0], pos[1], POSSIBLE_MOVE_CURSOR);
            });

            this.restoreCursor();
        }
    }

    private boolean processMove() {
        if (!this.game.isPegSelected()) {
            return false;
        }

        var boardCells = this.game.getBoard().getBoardCells();
        var res = this.game.makeMove(x, y);
        saveCursor();

        this.boardSelections = new int[boardCells.length][boardCells[0].length];
        System.out.print("\033[H");
        printBoard(boardCells);

        restoreCursor();
        return res;
    }

    @Override
    public void undo() {
        if (this.game != null && this.game.isStarted()) {
            this.game.undoMove();
            saveCursor();
            var boardCells = this.game.getBoard().getBoardCells();
            this.boardSelections = new int[boardCells.length][boardCells[0].length];
            System.out.print("\033[H");
            printBoard(boardCells);
            printCursor(this.x, this.y, MAIN_CURSOR);
            restoreCursor();
        }
    }

    /*
        Screen positioning and printing
    */
    public void printBoard(@NonNull BoardCell[][] cells) {

        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[i].length; j++) {
                System.out.print((cells[i][j] == null) ? "   " : cells[i][j]);
            }

            System.out.println(Color.RESET);
        }
    }

    private void printCursor(int x, int y, Color color) {
        int[] position = logicalXYToScreen(x, y);
        char[] brackets = {'[', ']'};
        var boardCell = this.game.getBoard().getBoardCellAt(x, y);
        System.out.print(boardCell.getState().getColor());
        System.out.print(color);
        if (POSSIBLE_MOVE_CURSOR.equals(color)) {
            brackets = new char[]{'(', ')'};
        } else if (SELECTED_CURSOR.equals(color)) {
            brackets = new char[]{'{', '}'};
        }

        positionAtScreen(position[0], position[1]);
        System.out.print(brackets[0]);
        positionAtScreen(position[0] + 2, position[1]);
        System.out.print(brackets[1] + Color.RESET.getCode());

    }

    private int[] logicalXYToScreen(int x, int y) {
        return new int[]{
            3 * (x + 1) - 2 + BOARD_OFFSET_X, y + 1 + BOARD_OFFSET_Y
        };
    }

    @Override
    public void positionPrompt() {
        if (this.game != null && this.game.isStarted()) {
            int propmtY = this.game.getBoard().getBoardCells().length + BOARD_OFFSET_Y + 2;
            positionAtScreen(0, propmtY);
        } else {
            positionAtScreen(0, 1);
        }
    }

    private void restoreCursor() {
        System.out.print("\033[u");
    }

    private void saveCursor() {
        System.out.print("\033[s");
    }

    public void hideCursor() {
        System.out.print("\033[?25l");
    }

    public void showCursor() {
        System.out.print("\033[?25h");
    }

    public void clearScreen() {
        System.out.print("\033[2J");
        System.out.print("\033[0H");
    }

}