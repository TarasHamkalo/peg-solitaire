package sk.tuke.gamestudio.pegsolitaire.core.core.impl;

import org.junit.jupiter.api.Test;
import sk.tuke.gamestudio.pegsolitaire.core.board.BoardCell;
import sk.tuke.gamestudio.pegsolitaire.core.board.impl.BasicCell;
import sk.tuke.gamestudio.pegsolitaire.core.pegs.impl.PegImpl;

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
