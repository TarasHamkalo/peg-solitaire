package sk.tuke.gamestudio.server.pegsolitaire.game.core.levels.impl;

import lombok.NonNull;
import sk.tuke.gamestudio.server.pegsolitaire.game.core.board.BoardCell;
import sk.tuke.gamestudio.server.pegsolitaire.game.core.board.impl.BasicCell;
import sk.tuke.gamestudio.server.pegsolitaire.game.core.levels.LevelBuilder;
import sk.tuke.gamestudio.server.pegsolitaire.game.core.pegs.PegFactory;

// THE ONLY LEVEL I MANAGED TO BEAT
public class SmallFrogLevelBuilder extends LevelBuilder {
    public SmallFrogLevelBuilder(@NonNull PegFactory pegFactory) {
        super(pegFactory);
    }

    @Override
    public BoardCell[][] build() {
        BoardCell[][] boardCells = new BoardCell[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                boardCells[i][j] = new BasicCell();
            }
        }

        boardCells[0][0].setPeg(this.getPegFactory().getRandomPeg());
        boardCells[0][1].setPeg(this.getPegFactory().getRandomPeg());
        boardCells[1][2].setPeg(this.getPegFactory().getRandomPeg());
        boardCells[2][1].setPeg(this.getPegFactory().getRandomPeg());

        return boardCells;
    }
}
