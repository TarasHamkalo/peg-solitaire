import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import mock.levels.HasMovesLevelBuilder;
import mock.levels.NoMovesLevelBuilder;
import org.junit.jupiter.api.Test;
import pegsolitaire.game.core.board.Board;
import pegsolitaire.game.core.board.events.impl.BoardEventManagerImpl;

import static org.junit.jupiter.api.Assertions.*;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BoardTest {
    HasMovesLevelBuilder hasMovesLevelBuilder = new HasMovesLevelBuilder();
    NoMovesLevelBuilder noMovesLevelBuilder = new NoMovesLevelBuilder();

    @Test
    public void whenBoardHasNoMovesHasAvailableMovesReturnsFalse() {
        var eventManager = new BoardEventManagerImpl();
        var cells = noMovesLevelBuilder.build();
        var board = Board.builder()
            .eventManager(eventManager)
            .boardCells(cells)
            .build();
        assertFalse(board.hasAvailableMoves());
    }

    @Test
    public void whenBoardHasMovesHasAvailableMovesReturnsTrue() {
        var eventManager = new BoardEventManagerImpl();
        var cells = hasMovesLevelBuilder.build();
        var board = Board.builder()
            .eventManager(eventManager)
            .boardCells(cells)
            .build();
        assertTrue(board.hasAvailableMoves());
    }

    @Test
    public void onBoardWith12MovesCountHasToBeTheSame() {
        var eventManager = new BoardEventManagerImpl();
        var cells = hasMovesLevelBuilder.build();
        var board = Board.builder()
            .eventManager(eventManager)
            .boardCells(cells)
            .build();

        int movesCount = 0;
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[0].length; j++) {
                movesCount += board.getPossibleMoves(j, i).size();
            }
        }

        assertEquals(12, movesCount);
    }
}
