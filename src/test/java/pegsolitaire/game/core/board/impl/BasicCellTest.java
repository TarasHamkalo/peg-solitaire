package pegsolitaire.game.core.board.impl;

import org.junit.jupiter.api.Test;
import pegsolitaire.game.core.board.BoardCell;
import pegsolitaire.game.core.pegs.impl.PegImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BasicCellTest {
    @Test
    void cellCreatedWithPegShouldHaveStateOccupied() {
        BoardCell boardCell = new BasicCell(new PegImpl());
        assertEquals(BoardCell.State.OCCUPIED, boardCell.getState());
    }

    @Test
    void whenPegIsSetOntoBoardCellStateShouldBeOccupied() {
        BoardCell boardCell = new BasicCell();
        assertEquals(BoardCell.State.EMPTY, boardCell.getState());

        boardCell.setPeg(new PegImpl());
        assertEquals(BoardCell.State.OCCUPIED, boardCell.getState());
    }
}
