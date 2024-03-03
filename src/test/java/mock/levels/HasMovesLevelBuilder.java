package mock.levels;

import pegsolitaire.game.core.board.BoardCell;
import pegsolitaire.game.core.board.pegs.impl.PegImpl;
import pegsolitaire.game.core.levels.LevelBuilder;

public class HasMovesLevelBuilder extends LevelBuilder {

    @Override
    public BoardCell[][] build() {
        BoardCell[][] boardCells = new BoardCell[7][5];
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 5; j++) {
                boardCells[i][j] = new BoardCell();
            }
        }

        for (int i : new int[]{0, 6}) {
            for (int j = 0; j < 5; j++) {
                if (j != 2) {
                    boardCells[i][j].setPeg(new PegImpl());
                }
            }
        }

        for (int i : new int[]{1, 5}) {
            for (int j : new int[]{0, 4}) {
                boardCells[i][j].setPeg(new PegImpl());
            }
        }

        for (int i = 1; i < 4; i++) {
            boardCells[3][i] = new BoardCell(new PegImpl());
        }

        boardCells[2][2].setPeg(new PegImpl());
        boardCells[4][2].setPeg(new PegImpl());
        return boardCells;
    }
}
