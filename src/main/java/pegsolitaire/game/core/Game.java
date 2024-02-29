package pegsolitaire.game.core;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.reflections.Reflections;
import pegsolitaire.game.core.board.Board;
import pegsolitaire.game.core.board.BoardCell;
import pegsolitaire.game.core.board.events.BoardEventManager;
import pegsolitaire.game.core.levels.LevelBuilder;
import pegsolitaire.game.core.levels.impl.ClassicLevelBuilder;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
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

    public boolean makeMove(int[] to) {
        if (this.selectedPegPosition == null) {
            // TODO: log
            return false;
        }

        return this.board.makeMove(selectedPegPosition, to);
    }

    public boolean undoMove() {
        return this.board.undoMove();
    }

    /**
     * @param position Position of main peg
     * @return True if and only if peg on this position can be selected.
     */
    public boolean selectPeg(int[] position) {
        BoardCell boardCell = board.getBoardCellAt(position);
        if (boardCell != null &&
            boardCell.getState().equals(BoardCell.State.OCCUPIED)) {
            this.selectedPegPosition = position;
            return true;
        }

        return false;
    }

    public Set<Class<? extends LevelBuilder>> getLevelBuilders() {
        Reflections reflections = new Reflections("pegsolitaire.game.core.levels");
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
