package sk.tuke.gamestudio.server.pegsolitaire.game.core.levels.impl;

import lombok.NonNull;
import sk.tuke.gamestudio.server.pegsolitaire.game.core.board.BoardCell;
import sk.tuke.gamestudio.server.pegsolitaire.game.core.board.impl.BasicCell;
import sk.tuke.gamestudio.server.pegsolitaire.game.core.levels.LevelBuilder;
import sk.tuke.gamestudio.server.pegsolitaire.game.core.pegs.PegFactory;

public class EuropeanLevelBuilder extends LevelBuilder {

    public EuropeanLevelBuilder(@NonNull PegFactory pegFactory) {
        super(pegFactory);
    }

    @Override
    public BoardCell[][] build() {
        BoardCell[][] boardCells = new BasicCell[7][7];
        for (int i = 0; i < 2; i++) {
            for (int j = 2 - i; j < boardCells[0].length - 2 + i; j++) {
                boardCells[i][j] = new BasicCell(getPegFactory().getRandomPeg());
            }
        }

        for (int i = 2; i < 5; i++) {
            for (int j = 0; j < boardCells[0].length; j++) {
                boardCells[i][j] = new BasicCell(getPegFactory().getRandomPeg());
            }
        }

        for (int i = 0; i < 2; i++) {
            for (int j = 1 + i; j < boardCells[0].length - 1 - i; j++) {
                boardCells[5 + i][j] = new BasicCell(getPegFactory().getRandomPeg());
            }
        }

        boardCells[2][3].setPeg(null);
        return boardCells;
    }
}
