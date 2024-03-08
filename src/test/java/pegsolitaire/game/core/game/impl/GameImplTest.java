package pegsolitaire.game.core.game.impl;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import mock.levels.HasMovesLevelBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import pegsolitaire.game.core.board.BoardBuilder;
import pegsolitaire.game.core.board.BoardCell;
import pegsolitaire.game.core.board.impl.BoardImpl;
import pegsolitaire.game.core.events.BoardEventManager;
import pegsolitaire.game.core.events.impl.BoardEventManagerImpl;
import pegsolitaire.game.core.game.Game;
import pegsolitaire.game.core.levels.LevelBuilder;
import pegsolitaire.game.core.pegs.PegFactory;
import pegsolitaire.game.core.pegs.impl.PegFactoryImpl;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalMatchers.aryEq;
import static org.mockito.Mockito.*;

@FieldDefaults(level = AccessLevel.PRIVATE)
class GameImplTest {

    PegFactory pegFactory = new PegFactoryImpl();

    final BoardEventManager eventManager = new BoardEventManagerImpl();

    final BoardCell[][] boardCells = new HasMovesLevelBuilder(pegFactory).build();

    Game underTest;

    @Mock
    BoardImpl boardMock;

    @Spy
    BoardBuilder boardBuilderSpy;

    @Mock
    LevelBuilder levelBuilderMock;

    AutoCloseable mocksAutoClosable;

    @BeforeEach
    void setUp() {
        this.boardBuilderSpy = BoardImpl.builder();
        this.mocksAutoClosable = MockitoAnnotations.openMocks(this);
        when(levelBuilderMock.build()).thenReturn(boardCells);
        doReturn(boardMock).when(boardBuilderSpy).build();

        this.underTest = GameImpl.builder()
            .levelBuilder(levelBuilderMock)
            .boardBuilder(boardBuilderSpy)
            .eventManager(eventManager)
            .build();

        this.underTest.start();
    }

    @AfterEach
    void tearDown() throws Exception {
        this.mocksAutoClosable.close();
    }

    @Test
    void whenStartCalledFirstTimeThenBoardIsBuiltProperly() {

        verify(levelBuilderMock).build();
        verifyNoMoreInteractions(levelBuilderMock);

        verify(boardBuilderSpy).eventManager(eventManager);
        verify(boardBuilderSpy).boardCells(boardCells);
        verify(boardBuilderSpy).build();
        verifyNoMoreInteractions(boardBuilderSpy);

        assertTrue(underTest.isStarted());
    }

    @Test
    void whenBoardCellIsEmptyThenNotSelectIt() {
        BoardCell emptyCellMock = mock(BoardCell.class);
        when(emptyCellMock.getState()).thenReturn(BoardCell.State.EMPTY);
        when(boardMock.getBoardCellAt(anyInt(), anyInt())).thenReturn(emptyCellMock);

        // Perform the selection
        assertFalse(underTest.selectPeg(2, 2));

        // Verify that selectedPegPosition remains null
        assertFalse(underTest.isPegSelected());
    }

    @Test
    void whenBoardCellIsNullDoNothing() {
        when(boardMock.getBoardCellAt(anyInt(), anyInt())).thenReturn(null);

        underTest.selectPeg(5, 5);

        assertFalse(underTest.isPegSelected());
    }

    @Test
    void whenValidBoardCellThenSelectIt() {
        BoardCell validCell = mock(BoardCell.class);
        when(validCell.getState()).thenReturn(BoardCell.State.OCCUPIED);
        when(boardMock.getBoardCellAt(anyInt(), anyInt())).thenReturn(validCell);

        underTest.selectPeg(5, 5);

        assertTrue(underTest.isPegSelected());
    }

    @Test
    void whenPegIsNotSelectedDoNothing() {
        underTest.makeMove(0, 0);

        verifyNoInteractions(boardMock);
    }

    @Test
    void whenPegIsSelectedProceedInMove() {
        int[] from = {5, 5};
        int[] to = {5, 6};

        BoardCell occupiedCell = mock(BoardCell.class);
        when(occupiedCell.getState()).thenReturn(BoardCell.State.OCCUPIED);

        when(boardMock.getBoardCellAt(eq(from[0]), eq(from[1]))).thenReturn(occupiedCell);
        when(boardMock.makeMove(any(), any())).thenReturn(true);

        underTest.selectPeg(from[0], from[1]);
        underTest.makeMove(to[0], to[1]);

        verify(boardMock).makeMove(aryEq(from), aryEq(to));
    }

    @Test
    void whenUndoIsCalledThenPegIsUnselected() {
        BoardCell occupiedCell = mock(BoardCell.class);
        when(occupiedCell.getState()).thenReturn(BoardCell.State.OCCUPIED);

        when(boardMock.getBoardCellAt(anyInt(), anyInt())).thenReturn(occupiedCell);

        underTest.selectPeg(1, 1);
        underTest.undoMove();

        assertFalse(underTest.isPegSelected());
    }

    @Test
    void whenNoPegSelectedThenReturnsEmptyList() {
        assertTrue(underTest.getPossibleMoves().isEmpty());
    }

    @Test
    void whenPegSelectedThenReturnsPossibleMoves() {
        int[] selectedPegPosition = {3, 3};
        List<int[]> expectedMoves = List.of(new int[]{2, 3}, new int[]{4, 3});

        BoardCell occupiedCell = mock(BoardCell.class);
        when(occupiedCell.getState()).thenReturn(BoardCell.State.OCCUPIED);

        when(boardMock.getBoardCellAt(anyInt(), anyInt())).thenReturn(occupiedCell);
        when(boardMock.getPossibleMoves(anyInt(), anyInt())).thenReturn(expectedMoves);

        underTest.selectPeg(selectedPegPosition[0], selectedPegPosition[1]);

        List<int[]> provided = underTest.getPossibleMoves();

        assertEquals(expectedMoves.size(), provided.size());
        assertTrue(expectedMoves.containsAll(provided));
    }
}