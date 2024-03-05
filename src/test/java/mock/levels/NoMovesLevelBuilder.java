package mock.levels;

import pegsolitaire.game.core.board.impl.BasicCell;
import pegsolitaire.game.core.levels.LevelBuilder;

public class NoMovesLevelBuilder extends LevelBuilder {
    @Override
    public BasicCell[][] build() {
        BasicCell[][] boardCells = new BasicCell[3][3];
        for (int i = 0; i < boardCells.length; i++) {
            for (int j = 0; j < boardCells[0].length; j++) {
                boardCells[i][j] = new BasicCell();
            }
        }

        return boardCells;
    }
}
