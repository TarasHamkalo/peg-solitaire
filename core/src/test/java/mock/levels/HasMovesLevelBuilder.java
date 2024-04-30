package mock.levels;

import lombok.NonNull;
import sk.tuke.gamestudio.pegsolitaire.core.board.impl.BasicCell;
import sk.tuke.gamestudio.pegsolitaire.core.levels.LevelBuilder;
import sk.tuke.gamestudio.pegsolitaire.core.pegs.PegFactory;
import sk.tuke.gamestudio.pegsolitaire.core.pegs.impl.PegImpl;

public class HasMovesLevelBuilder extends LevelBuilder {

  public HasMovesLevelBuilder(@NonNull PegFactory pegFactory) {
    super(pegFactory);
  }

  @Override
  public BasicCell[][] build() {
    BasicCell[][] boardCells = new BasicCell[7][5];
    for (int i = 0; i < 7; i++) {
      for (int j = 0; j < 5; j++) {
        boardCells[i][j] = new BasicCell();
      }
    }

    for (int i : new int[]{0, 6}) {
      for (int j = 0; j < 5; j++) {
        if (j != 2) {
          boardCells[i][j].setPeg(new PegImpl());
        }
      }
    }

    for (int i : new int[]{1, 5}) {
      for (int j : new int[]{0, 4}) {
        boardCells[i][j].setPeg(new PegImpl());
      }
    }

    for (int i = 1; i < 4; i++) {
      boardCells[3][i] = new BasicCell(new PegImpl());
    }

    boardCells[2][2].setPeg(new PegImpl());
    boardCells[4][2].setPeg(new PegImpl());
    return boardCells;
  }
}
