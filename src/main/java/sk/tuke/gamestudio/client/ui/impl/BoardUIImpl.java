package sk.tuke.gamestudio.client.ui.impl;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import sk.tuke.gamestudio.commons.entity.Score;
import sk.tuke.gamestudio.commons.exception.ScoreException;
import sk.tuke.gamestudio.pegsolitaire.core.Color;
import sk.tuke.gamestudio.pegsolitaire.core.board.BoardCell;
import sk.tuke.gamestudio.pegsolitaire.core.game.Game;
import sk.tuke.gamestudio.client.ui.BoardUI;
import sk.tuke.gamestudio.commons.service.ScoreService;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BoardUIImpl implements BoardUI {

    private static final Color MAIN_CURSOR = Color.BLUE;

    private static final Color POSSIBLE_MOVE_CURSOR = Color.GREEN;

    private static final Color SELECTED_CURSOR = Color.BRIGHT_MAGENTA;

    private static final int POSSIBLE_MOVE_CODE = 1;

    private static final int SELECTED_CURSOR_CODE = 2;

    Game game;

    ScoreService scoreService;

    KeyboardListener keyboardListener;

    int[][] boardSelections;

    int x;

    int y;

    public BoardUIImpl(ScoreService scoreService) throws NativeHookException {
        GlobalScreen.registerNativeHook();
        this.scoreService = scoreService;
    }

    public void start(@NonNull Game game) {
        this.game = game;
        this.keyboardListener = new KeyboardListener(this);
        GlobalScreen.addNativeKeyListener(this.keyboardListener);

        this.game.start();
        var boardCells = this.game.getBoard().getBoardCells();

        this.boardSelections = new int[boardCells.length][boardCells[0].length];
        this.x = (boardCells[0].length) / 2;
        this.y = boardCells.length / 2;

        hideCursor();
        clearScreen();
        printBoard(boardCells);
        printScore();
        printCursor(x, y, MAIN_CURSOR);
    }

    @Override
    public void stop() {
        if (this.game != null && this.game.isStarted()) {
            this.game.stop();
        }

        GlobalScreen.removeNativeKeyListener(this.keyboardListener);
        showCursor();
    }

    @Override
    @SneakyThrows
    public void exit() {
        if (this.game != null && this.game.isStarted()) {
            stop();
        }

        GlobalScreen.unregisterNativeHook();
    }

    /*
        Cursor adjustment
    */
    @Override
    public void adjustCursor(int x, int y) {
        var dx = this.x + x;
        var dy = this.y + y;
        var nextCell = game.getBoard().getBoardCellAt(dx, dy);
        if (nextCell != null) {
            var currentCell = game.getBoard().getBoardCellAt(this.x, this.y);

            saveCursor();

            printCellAt(currentCell, this.x, this.y);
            printCursor(dx, dy, MAIN_CURSOR);

            restoreCursor();

            this.x = dx;
            this.y = dy;
        }
    }

    @Override
    public void select() {
        if (processMove()) {
            return;
        }

        if (game.selectPeg(x, y)) {
            boardSelections[y][x] = 2;
            saveCursor();

            printCursor(x, y, SELECTED_CURSOR);
            game.getPossibleMoves().forEach(pos -> {
                boardSelections[pos[1]][pos[0]] = 1;
                printCursor(pos[0], pos[1], POSSIBLE_MOVE_CURSOR);
            });

            restoreCursor();
        }
    }

    private boolean processMove() {
        if (!game.isPegSelected()) {
            return false;
        }

        var res = game.makeMove(x, y);

        var boardCells = game.getBoard().getBoardCells();
        this.boardSelections = new int[boardCells.length][boardCells[0].length];

        saveCursor();

        positionAtScreen(1, 1);
        printBoard(boardCells);
        printScore();
        if (!game.getBoard().hasAvailableMoves()) {
            printResult();
            saveScore();
            restoreCursor();
            stop();
            return true;
        }

        restoreCursor();
        return res;
    }

    private void saveScore() {
        try {
            scoreService.addScore(
                Score.builder()
                    .game("pegsolitaire")
                    .player(System.getProperty("user.name"))
                    .points((int) game.getScore())
                    .build()
            );

        } catch (ScoreException scoreException) {
            positionPrompt();
            System.out.println("\n" + scoreException.getMessage());
            System.out.println("Try to reset scores");
            positionPrompt();
        }
    }

    private void printScore() {
        int[] position = logicalXYToScreen(0, game.getBoard().getBoardCells().length + 2);
        positionAtScreen(position[0], position[1]);
        System.out.print("\033[0K");
        System.out.printf("Score: %s%d%s\n", Color.MAGENTA, game.getScore(), Color.RESET);
    }

    @Override
    public void undo() {
        if (this.game != null && this.game.isStarted()) {
            this.game.undoMove();
            var boardCells = this.game.getBoard().getBoardCells();
            this.boardSelections = new int[boardCells.length][boardCells[0].length];

            saveCursor();

            positionAtScreen(1, 1);
            printBoard(boardCells);
            printScore();
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

    private void printCellAt(@NonNull BoardCell cell, int x, int y) {
        int[] logicalXY = logicalXYToScreen(this.x, this.y);
        positionAtScreen(logicalXY[0], logicalXY[1]);
        System.out.print(cell);
        if (this.boardSelections[y][x] == POSSIBLE_MOVE_CODE) {
            printCursor(x, y, POSSIBLE_MOVE_CURSOR);
        } else if (this.boardSelections[y][x] == SELECTED_CURSOR_CODE) {
            printCursor(x, y, SELECTED_CURSOR);
        }
    }

    private void printCursor(int x, int y, @NonNull Color color) {
        int[] logicalXY = logicalXYToScreen(x, y);
        char[] brackets = {'[', ']'};
        var boardCell = this.game.getBoard().getBoardCellAt(x, y);

        System.out.print(boardCell.getState().getColor());
        System.out.print(color);

        if (POSSIBLE_MOVE_CURSOR.equals(color)) {
            brackets = new char[]{'(', ')'};
        } else if (SELECTED_CURSOR.equals(color)) {
            brackets = new char[]{'{', '}'};
        }

        positionAtScreen(logicalXY[0], logicalXY[1]);
        System.out.print(brackets[0]);
        positionAtScreen(logicalXY[0] + 2, logicalXY[1]);
        System.out.print(brackets[1] + Color.RESET.getCode());

    }

    private int[] logicalXYToScreen(int x, int y) {
        return new int[]{3 * (x + 1) - 2, y + 1};
    }

    private void printResult() {
        long pegsCount = this.game.getBoard().getPegsCount();
        if (pegsCount == 1) {
            System.out.println("You have solved the game.\n");
        } else {
            System.out.printf("You have failed with %d pegs left.\n", pegsCount);
        }

    }

    @Override
    public void drawPrompt() {
        positionPrompt();
        System.out.print("~> ");
    }

    @Override
    public void positionPrompt() {
        if (game != null && game.isStarted()) {
            int propmtY = game.getBoard().getBoardCells().length + 5;
            positionAtScreen(1, propmtY);
        } else {
            positionAtScreen(1, 1);
        }
    }

    private void positionAtScreen(int x, int y) {
        System.out.printf("\033[%d;%dH", y, x);
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
        System.out.print("\033[0H");
        System.out.print("\033[2J");
    }

}