import lombok.SneakyThrows;
import ui.ConsoleUI;
import pegsolitaire.game.core.Game;
import pegsolitaire.game.core.board.Board;
import pegsolitaire.game.core.board.BoardCell;
import pegsolitaire.game.core.board.events.impl.BoardEventManagerImpl;
import pegsolitaire.game.core.board.pegs.impl.PegImpl;
import pegsolitaire.game.core.levels.impl.ClassicLevelBuilder;

import java.util.Arrays;


public class Main {
    @SneakyThrows
    public static void main(String[] args) {
        var game = Game.builder()
            .levelBuilder(new ClassicLevelBuilder())
            .eventManager(new BoardEventManagerImpl())
            .build();

        var console = new ConsoleUI(game);
        console.start();
        console.printBoard();
        game.getBoard().getPossibleMoves(new int[]{3, 1}).forEach(a -> System.out.println(Arrays.toString(a)));
//        game.getBoard().getPossibleMoves(new int[]{0, 3}).forEach(a -> System.out.println(Arrays.toString(a)));
        System.out.println();
    }

    private static void extracted() {
        PegImpl peg = PegImpl.builder().build();
        BoardCell[][] boardCells = new BoardCell[5][5];
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                boardCells[i][j] = new BoardCell(peg.clone());
            }
        }

        for (BoardCell b : boardCells[4]) {
            b.setPeg(null);
        }


        Board board = Board.builder()
            .boardCells(boardCells)
            .eventManager(null)
            .build();

        printCells(boardCells);
        System.out.print("\n");
        System.out.print("\n");

        var a = board.makeMove(new int[]{0, 0}, new int[]{0, 4});
        System.out.println(a);
        printCells(boardCells);
        System.out.print("\n");
        System.out.print("\n");

        System.out.println(board.getHistory().size());
        a = board.undoMove();
        System.out.println(board.getHistory().size());
        printCells(boardCells);
    }

    public static void printCells(BoardCell[][] cells) {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (cells[i][j].getState().equals(BoardCell.State.OCCUPIED)) {
                    System.out.print(" * ");
                } else {
                    System.out.print(" . ");
                }
            }
            System.out.print("\n");
        }
    }
}
