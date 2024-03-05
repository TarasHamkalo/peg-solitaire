package pegsolitaire.game.core.levels.impl;

import pegsolitaire.game.core.board.BoardCell;
import pegsolitaire.game.core.board.impl.BasicCell;
import pegsolitaire.game.core.levels.LevelBuilder;
import pegsolitaire.game.core.pegs.Peg;
import pegsolitaire.game.core.pegs.impl.PegImpl;

// TODO: add events and types for pegs
public class ClassicLevelBuilder extends LevelBuilder {
    @Override
    public BoardCell[][] build() {
        Peg peg = new PegImpl();
        BoardCell[][] boardCells = new BasicCell[7][7];
        for (int i = 0; i < 7; i++) {
            if (i < 2 || i > 4) {
                for (int j = 2; j < 5; j++) {
                    boardCells[i][j] = new BasicCell(peg.clonePeg());
                }
            } else {
                for (int j = 0; j < 7; j++) {
                    boardCells[i][j] = new BasicCell(peg.clonePeg());
                }
            }
        }

        boardCells[3][3].setPeg(null);
        return boardCells;
    }
}