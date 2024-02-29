package pegsolitaire.game.core;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.reflections.Reflections;
import pegsolitaire.game.core.board.Board;
import pegsolitaire.game.core.board.BoardCell;
import pegsolitaire.game.core.board.events.BoardEventManager;
import pegsolitaire.game.core.levels.LevelBuilder;
import pegsolitaire.game.core.levels.impl.ClassicLevelBuilder;

import java.util.Collections;
import java.util.List;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Game {
    @NonNull BoardEventManager eventManager;
    @NonNull LevelBuilder levelBuilder;
    Board board;
    List<int[]> possibleMovesFromSelectedPosition;
    int[] selectedPegPosition;
    boolean started;

    public void start() {
        var cells = this.levelBuilder.build();
        this.board = Board.builder()
            .boardCells(cells)
            .eventManager(this.eventManager)
            .build();
        this.started = true;
    }

    public void stop() {
        if (this.board != null) {
            this.board.clearHistory();
            this.board.setBoardCells(this.levelBuilder.build());
            this.selectedPegPosition = null;
            this.started = false;
        }
    }

    public boolean makeMove(int x, int y) {
        if (this.selectedPegPosition == null) {
            // TODO: log
            return false;
        }

        if (this.board.makeMove(selectedPegPosition, new int[]{x, y})) {
            this.selectedPegPosition = new int[]{-1, -1};
            return true;
        }

        return false;
    }

    public boolean undoMove() {
        return this.board.undoMove();
    }

    public List<int[]> getPossibleMoves() {
        if (this.selectedPegPosition == null) {
            return Collections.emptyList();
        }

        if (this.possibleMovesFromSelectedPosition == null) {
            this.possibleMovesFromSelectedPosition =
                this.board.getPossibleMoves(selectedPegPosition[0], selectedPegPosition[1]);
        }

        return possibleMovesFromSelectedPosition;
    }
    /**
     * @return True if and only if peg on this position can be selected.
     */
    public boolean selectPeg(int x, int y) {
        BoardCell boardCell = board.getBoardCellAt(x, y);
        if (boardCell != null &&
            boardCell.getState().equals(BoardCell.State.OCCUPIED)) {
            this.possibleMovesFromSelectedPosition = null;
            this.selectedPegPosition = new int[]{x, y};
            return true;
        }

        return false;
    }

    public boolean isPegSelected() {
        if (this.selectedPegPosition == null) {
            return false;
        }

        return this.selectedPegPosition[0] != -1 && this.selectedPegPosition[1] != -1;
    }

    public Set<Class<? extends LevelBuilder>> getLevelBuilders() {
        var reflections = new Reflections("pegsolitaire.game.core.levels");
        return reflections.getSubTypesOf(LevelBuilder.class);
    }

    public void setLevelBuilder(Class<? extends LevelBuilder> levelBuilderClass) {
        try {
            this.levelBuilder = levelBuilderClass.getDeclaredConstructor().newInstance();
        } catch (Exception ignore) {
            // TODO: log
            System.err.println("Provided level builder does not have noArgsConstructor");
            this.levelBuilder = new ClassicLevelBuilder();
        }
    }
}
