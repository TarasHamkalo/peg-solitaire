package pegsolitaire.game.core.levels.impl;

import pegsolitaire.game.core.board.BoardCell;
import pegsolitaire.game.core.board.pegs.Peg;
import pegsolitaire.game.core.board.pegs.impl.PegImpl;
import pegsolitaire.game.core.levels.LevelBuilder;

// TODO: no events added
public class TriangleLevelBuilder extends LevelBuilder {
    @Override
    public BoardCell[][] build() {
        BoardCell[][] cells = new BoardCell[4][7];
        Peg peg = new PegImpl();
        cells[0][3] = new BoardCell();
        for (int i = 1; i < cells.length; i++) {
            int start = cells[0].length / 2 - i;
            for (int j = start; j < start + 2 * i + 1; j++) {
                if (j < cells[0].length) {
                    cells[i][j] = new BoardCell(peg.clonePeg());
                }
            }
        }

        return cells;
    }
}
