package mock.levels;

import lombok.NonNull;
import sk.tuke.gamestudio.pegsolitaire.core.board.impl.BasicCell;
import sk.tuke.gamestudio.pegsolitaire.core.levels.LevelBuilder;
import sk.tuke.gamestudio.pegsolitaire.core.pegs.PegFactory;

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
