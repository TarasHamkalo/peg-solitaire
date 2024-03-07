package pegsolitaire.game.core.levels.impl;

import pegsolitaire.game.core.board.BoardCell;
import pegsolitaire.game.core.board.impl.BasicCell;
import pegsolitaire.game.core.levels.LevelBuilder;
import pegsolitaire.game.core.pegs.impl.PegImpl;

// TODO: no events added
public class EuropeanLevelBuilder extends LevelBuilder {
    @Override
    public BoardCell[][] build() {
        BoardCell[][] boardCells = new BasicCell[7][7];
        for (int i = 0; i < 2; i++) {
            for (int j = 2 - i; j < boardCells[0].length - 2 + i; j++) {
                boardCells[i][j] = new BasicCell(new PegImpl());
            }
        }

        for (int i = 2; i < 5; i++) {
            for (int j = 0; j < boardCells[0].length; j++) {
                boardCells[i][j] = new BasicCell(new PegImpl());
            }
        }

        for (int i = 0; i < 2; i++) {
            for (int j = 1 + i; j < boardCells[0].length - 1 - i; j++) {
                boardCells[5 + i][j] = new BasicCell(new PegImpl());
            }
        }

        boardCells[2][3].setPeg(null);
        return boardCells;
    }
}
