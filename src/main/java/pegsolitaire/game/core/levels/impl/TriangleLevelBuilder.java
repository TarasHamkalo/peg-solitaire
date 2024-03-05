package pegsolitaire.game.core.levels.impl;

import pegsolitaire.game.core.board.BoardCell;
import pegsolitaire.game.core.board.impl.BasicCell;
import pegsolitaire.game.core.pegs.impl.PegImpl;
import pegsolitaire.game.core.levels.LevelBuilder;

// TODO: no events added
public class TriangleLevelBuilder extends LevelBuilder {
    @Override
    public BoardCell[][] build() {
        BoardCell[][] cells = new BasicCell[4][5];
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[0].length; j++) {
                cells[i][j] = new BasicCell();
            }
        }

        int startInc = 2;
        for (int i = 1; i < cells.length; i++) {
            for (int j = 2 + startInc; j < cells[0].length; j++) {
                cells[i][j].setPeg(new PegImpl());
            }

            startInc--;
        }
        return cells;
    }
}
