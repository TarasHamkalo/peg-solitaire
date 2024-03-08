package mock.levels;

import lombok.NonNull;
import pegsolitaire.game.core.board.impl.BasicCell;
import pegsolitaire.game.core.levels.LevelBuilder;
import pegsolitaire.game.core.pegs.PegFactory;

public class NoMovesLevelBuilder extends LevelBuilder {

    public NoMovesLevelBuilder(@NonNull PegFactory pegFactory) {
        super(pegFactory);
    }

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
