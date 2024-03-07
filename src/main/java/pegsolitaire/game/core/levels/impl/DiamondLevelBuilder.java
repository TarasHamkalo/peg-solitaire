package pegsolitaire.game.core.levels.impl;

import pegsolitaire.game.core.board.BoardCell;
import pegsolitaire.game.core.board.impl.BasicCell;
import pegsolitaire.game.core.levels.LevelBuilder;
import pegsolitaire.game.core.pegs.impl.PegImpl;

public class DiamondLevelBuilder extends LevelBuilder {

    @Override
    public BoardCell[][] build() {
        BoardCell[][] boardCells = new BoardCell[9][9];
        int left = 4;
        int right = 4;
        for (int i = 0; i < boardCells.length; i++) {
            for (int j = left; j <= right && j < boardCells[0].length; j++) {
                boardCells[i][j] = new BasicCell(new PegImpl());
            }

            if (i >= 4) {
                left++;
                right--;
            } else {
                right++;
                left--;
            }
        }

        boardCells[2][4].setPeg(null);
        return boardCells;
    }
}
