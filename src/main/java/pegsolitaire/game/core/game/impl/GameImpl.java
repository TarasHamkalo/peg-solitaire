package pegsolitaire.game.core.game.impl;

import lombok.*;
import lombok.experimental.FieldDefaults;
import pegsolitaire.game.core.board.Board;
import pegsolitaire.game.core.board.impl.BoardCell;
import pegsolitaire.game.core.board.impl.BoardImpl;
import pegsolitaire.game.core.events.BoardEventManager;
import pegsolitaire.game.core.game.Game;
import pegsolitaire.game.core.levels.LevelBuilder;

import java.util.Collections;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GameImpl implements Game {
    @NonNull
    BoardEventManager eventManager;
    @NonNull
    LevelBuilder levelBuilder;
    Board board;
    int[] selectedPegPosition;
    boolean started;


    public void start() {
        if (this.started) {
            stop();
        }

        var boardCells = this.levelBuilder.build();
        if (this.board == null) {
            this.board = BoardImpl.builder()
                .boardCells(boardCells)
                .eventManager(this.eventManager)
                .build();
        } else {
            this.board.setBoardCells(boardCells);
        }

        this.started = true;
    }

    public void stop() {
        if (this.board != null) {
            this.board.clearHistory();
            this.board.setBoardCells(null);
        }

        this.selectedPegPosition = null;
        this.started = false;
    }

    public boolean makeMove(int x, int y) {
        if (this.selectedPegPosition == null) {
            // TODO: log
            return false;
        }

        if (this.board.makeMove(selectedPegPosition, new int[]{x, y})) {
            this.selectedPegPosition = null;
            return true;
        }

        return false;
    }

    public boolean undoMove() {
        this.selectedPegPosition = null;
        return this.board.undoMove();
    }

    public List<int[]> getPossibleMoves() {
        if (this.selectedPegPosition == null) {
            return Collections.emptyList();
        }

        return this.board.getPossibleMoves(selectedPegPosition[0], selectedPegPosition[1]);
    }

    /**
     * @return True if and only if peg on this position can be selected.
     */
    public boolean selectPeg(int x, int y) {
        var boardCell = board.getBoardCellAt(x, y);
        if (boardCell != null &&
            boardCell.getState().equals(BoardCell.State.OCCUPIED)) {
            this.selectedPegPosition = new int[]{x, y};
            return true;
        }

        return false;
    }

    public boolean isPegSelected() {
        return selectedPegPosition != null;
    }
}