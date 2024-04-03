package sk.tuke.gamestudio.pegsolitaire.game.core.board.impl;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import mock.levels.HasMovesLevelBuilder;
import mock.levels.NoMovesLevelBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import sk.tuke.gamestudio.client.pegsolitaire.game.core.board.Board;
import sk.tuke.gamestudio.client.pegsolitaire.game.core.board.BoardCell;
import sk.tuke.gamestudio.client.pegsolitaire.game.core.board.impl.BoardImpl;
import sk.tuke.gamestudio.client.pegsolitaire.game.core.events.impl.BoardEventManagerImpl;
import sk.tuke.gamestudio.client.pegsolitaire.game.core.pegs.PegFactory;
import sk.tuke.gamestudio.client.pegsolitaire.game.core.pegs.impl.PegFactoryImpl;

import static org.junit.jupiter.api.Assertions.*;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
class BoardImplTest {

    PegFactory pegFactory = new PegFactoryImpl();

    HasMovesLevelBuilder hasMovesLevelBuilder = new HasMovesLevelBuilder(pegFactory);

    NoMovesLevelBuilder noMovesLevelBuilder = new NoMovesLevelBuilder(pegFactory);

    BoardEventManagerImpl eventManager = new BoardEventManagerImpl();

    @NonFinal
    Board underTest;

    @BeforeEach
    void setUpBoard() {
        var cells = hasMovesLevelBuilder.build();
        this.underTest = BoardImpl.builder()
            .eventManager(eventManager)
            .boardCells(cells)
            .build();
    }

    @Test
    void whenBoardHasNoMovesHasAvailableMovesReturnsFalse() {
        var cells = noMovesLevelBuilder.build();
        this.underTest.setBoardCells(cells);
        assertFalse(underTest.hasAvailableMoves());
    }

    @Test
    void whenBoardHasMovesHasAvailableMovesReturnsTrue() {
        assertTrue(underTest.hasAvailableMoves());
    }

    @Test
    void onBoardWith12MovesCountHasToBeTheSame() {
        int movesCount = 0;
        for (int i = 0; i < this.underTest.getBoardCells().length; i++) {
            for (int j = 0; j < this.underTest.getBoardCells()[0].length; j++) {
                movesCount += underTest.getPossibleMoves(j, i).size();
            }
        }

        assertEquals(12, movesCount);
    }

    @Test
    void makeMoveWithValidArgumentsShouldReturnTrueAndModifyBoard() {
        int[] from = new int[]{0, 0};
        int[] to = new int[]{0, 2};
        int[] middle = new int[]{0, 1};
        assertTrue(underTest.makeMove(from, to));
        assertEquals(
            BoardCell.State.EMPTY, underTest.getBoardCellAt(from[0], from[1]).getState()
        );
        assertEquals(
            BoardCell.State.EMPTY, underTest.getBoardCellAt(middle[0], middle[1]).getState()
        );
        assertEquals(
            BoardCell.State.OCCUPIED, underTest.getBoardCellAt(to[0], to[1]).getState()
        );
    }

    @CsvSource({
        "0, 0, 0, 0", // same position
        "0, 0, 1, 0", // distance < 2
        "0, 0, 0, 1", // distance < 2
        "0, 0, 0, 3", // distance > 2
        "0, 0, 3, 0", // distance > 2
    })
    @ParameterizedTest
    void makeMoveWithInValidArgumentsShouldNotModifyBoard(int fromX, int fromY, int toX, int toY) {
        assertFalse(underTest.makeMove(new int[]{fromX, fromY}, new int[]{toX, toY}));
    }

    @Test
    void whenUndoMoveCalledBoardStateHasToBeRestored() {
        int[] from = new int[]{0, 0};
        int[] to = new int[]{0, 2};
        int[] middle = new int[]{0, 1};
        underTest.makeMove(from, to);

        assertTrue(underTest.undoMove());

        assertEquals(
            BoardCell.State.OCCUPIED, underTest.getBoardCellAt(from[0], from[1]).getState()
        );
        assertEquals(
            BoardCell.State.OCCUPIED, underTest.getBoardCellAt(middle[0], middle[1]).getState()
        );
        assertEquals(
            BoardCell.State.EMPTY, underTest.getBoardCellAt(to[0], to[1]).getState()
        );
    }
}