package sk.tuke.gamestudio.client.pegsolitaire.game.core.levels.impl;

import lombok.NonNull;
import sk.tuke.gamestudio.client.pegsolitaire.game.core.board.BoardCell;
import sk.tuke.gamestudio.client.pegsolitaire.game.core.board.impl.BasicCell;
import sk.tuke.gamestudio.client.pegsolitaire.game.core.levels.LevelBuilder;
import sk.tuke.gamestudio.client.pegsolitaire.game.core.pegs.PegFactory;

public class DiamondLevelBuilder extends LevelBuilder {

    public DiamondLevelBuilder(@NonNull PegFactory pegFactory) {
        super(pegFactory);
    }

    @Override
    public BoardCell[][] build() {
        BoardCell[][] boardCells = new BoardCell[9][9];
        int left = 4;
        int right = 4;
        for (int i = 0; i < boardCells.length; i++) {
            for (int j = left; j <= right && j < boardCells[0].length; j++) {
                boardCells[i][j] = new BasicCell(
                    this.getPegFactory().getRandomPeg()
                );
            }

            left += (i >= 4) ? 1 : -1;
            right += (i >= 4) ? -1 : 1;
        }

        boardCells[2][4].setPeg(null);
        return boardCells;
    }
}
