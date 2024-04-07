package sk.tuke.gamestudio.pegsolitaire.core.core.impl;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import mock.levels.HasMovesLevelBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import sk.tuke.gamestudio.pegsolitaire.core.board.BoardBuilder;
import sk.tuke.gamestudio.pegsolitaire.core.board.BoardCell;
import sk.tuke.gamestudio.pegsolitaire.core.board.impl.BoardImpl;
import sk.tuke.gamestudio.pegsolitaire.core.events.BoardEventManager;
import sk.tuke.gamestudio.pegsolitaire.core.events.impl.BoardEventManagerImpl;
import sk.tuke.gamestudio.pegsolitaire.core.game.Game;
import sk.tuke.gamestudio.pegsolitaire.core.game.impl.GameImpl;
import sk.tuke.gamestudio.pegsolitaire.core.levels.LevelBuilder;
import sk.tuke.gamestudio.pegsolitaire.core.pegs.PegFactory;
import sk.tuke.gamestudio.pegsolitaire.core.pegs.impl.PegFactoryImpl;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
        Mockito.when(levelBuilderMock.build()).thenReturn(boardCells);
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

        Mockito.verify(levelBuilderMock).build();
        verifyNoMoreInteractions(levelBuilderMock);

        Mockito.verify(boardBuilderSpy).eventManager(eventManager);
        Mockito.verify(boardBuilderSpy).boardCells(boardCells);
        Mockito.verify(boardBuilderSpy).build();
        verifyNoMoreInteractions(boardBuilderSpy);

        assertTrue(underTest.isStarted());
    }

    @Test
    void whenBoardCellIsEmptyThenNotSelectIt() {
        BoardCell emptyCellMock = Mockito.mock(BoardCell.class);
        Mockito.when(emptyCellMock.getState()).thenReturn(BoardCell.State.EMPTY);
        Mockito.when(boardMock.getBoardCellAt(ArgumentMatchers.anyInt(), ArgumentMatchers.anyInt())).thenReturn(emptyCellMock);

        // Perform the selection
        assertFalse(underTest.selectPeg(2, 2));

        // Verify that selectedPegPosition remains null
        assertFalse(underTest.isPegSelected());
    }

    @Test
    void whenBoardCellIsNullDoNothing() {
        Mockito.when(boardMock.getBoardCellAt(ArgumentMatchers.anyInt(), ArgumentMatchers.anyInt())).thenReturn(null);

        underTest.selectPeg(5, 5);

        assertFalse(underTest.isPegSelected());
    }

    @Test
    void whenValidBoardCellThenSelectIt() {
        BoardCell validCell = Mockito.mock(BoardCell.class);
        Mockito.when(validCell.getState()).thenReturn(BoardCell.State.OCCUPIED);
        Mockito.when(boardMock.getBoardCellAt(ArgumentMatchers.anyInt(), ArgumentMatchers.anyInt())).thenReturn(validCell);

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

        BoardCell occupiedCell = Mockito.mock(BoardCell.class);
        Mockito.when(occupiedCell.getState()).thenReturn(BoardCell.State.OCCUPIED);

        Mockito.when(boardMock.getBoardCellAt(from[0], from[1])).thenReturn(occupiedCell);
        Mockito.when(boardMock.makeMove(ArgumentMatchers.any(), ArgumentMatchers.any())).thenReturn(true);

        underTest.selectPeg(from[0], from[1]);
        underTest.makeMove(to[0], to[1]);

        Mockito.verify(boardMock).makeMove(AdditionalMatchers.aryEq(from), AdditionalMatchers.aryEq(to));
    }

    @Test
    void whenUndoIsCalledThenPegIsUnselected() {
        BoardCell occupiedCell = Mockito.mock(BoardCell.class);
        Mockito.when(occupiedCell.getState()).thenReturn(BoardCell.State.OCCUPIED);

        Mockito.when(boardMock.getBoardCellAt(ArgumentMatchers.anyInt(), ArgumentMatchers.anyInt())).thenReturn(occupiedCell);

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

        BoardCell occupiedCell = Mockito.mock(BoardCell.class);
        Mockito.when(occupiedCell.getState()).thenReturn(BoardCell.State.OCCUPIED);

        Mockito.when(boardMock.getBoardCellAt(ArgumentMatchers.anyInt(), ArgumentMatchers.anyInt())).thenReturn(occupiedCell);
        Mockito.when(boardMock.getPossibleMoves(ArgumentMatchers.anyInt(), ArgumentMatchers.anyInt())).thenReturn(expectedMoves);

        underTest.selectPeg(selectedPegPosition[0], selectedPegPosition[1]);

        List<int[]> provided = underTest.getPossibleMoves();

        Assertions.assertEquals(expectedMoves.size(), provided.size());
        Assertions.assertTrue(expectedMoves.containsAll(provided));
    }
}