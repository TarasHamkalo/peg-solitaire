package sk.tuke.gamestudio.pegsolitaire.core.levels.impl;

import lombok.NonNull;
import sk.tuke.gamestudio.pegsolitaire.core.board.BoardCell;
import sk.tuke.gamestudio.pegsolitaire.core.levels.LevelBuilder;
import sk.tuke.gamestudio.pegsolitaire.core.board.impl.BasicCell;
import sk.tuke.gamestudio.pegsolitaire.core.pegs.PegFactory;

public class ClassicLevelBuilder extends LevelBuilder {

    public ClassicLevelBuilder(@NonNull PegFactory pegFactory) {
        super(pegFactory);
    }

    @Override
    public BoardCell[][] build() {
        BoardCell[][] boardCells = new BasicCell[7][7];
        for (int i = 0; i < 7; i++) {

            if (i < 2 || i > 4) {
                for (int j = 2; j < 5; j++) {
                    boardCells[i][j] = new BasicCell(
                        this.getPegFactory().getRandomPeg()
                    );
                }
            } else {
                for (int j = 0; j < 7; j++) {
                    boardCells[i][j] = new BasicCell(
                        this.getPegFactory().getRandomPeg()
                    );
                }
            }
        }

        boardCells[3][3].setPeg(null);
        return boardCells;
    }
}