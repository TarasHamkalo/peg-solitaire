package sk.tuke.gamestudio.pegsolitaire.game.core.game.impl;

import lombok.*;
import lombok.experimental.FieldDefaults;
import sk.tuke.gamestudio.pegsolitaire.game.core.board.Board;
import sk.tuke.gamestudio.pegsolitaire.game.core.board.BoardBuilder;
import sk.tuke.gamestudio.pegsolitaire.game.core.board.BoardCell;
import sk.tuke.gamestudio.pegsolitaire.game.core.events.BoardEvent;
import sk.tuke.gamestudio.pegsolitaire.game.core.events.BoardEventManager;
import sk.tuke.gamestudio.pegsolitaire.game.core.events.impl.StatisticsCollector;
import sk.tuke.gamestudio.pegsolitaire.game.core.game.Game;
import sk.tuke.gamestudio.pegsolitaire.game.core.levels.LevelBuilder;

import java.util.Arrays;
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

    @NonNull
    BoardBuilder boardBuilder;

    StatisticsCollector statisticsCollector;

    Board board;

    int[] selectedPegPosition;

    boolean started;

    public void start() {
        if (started) {
            stop();
        }

        startStatistic();

        initializeBoard();

        this.started = true;
    }

    private void initializeBoard() {
        var boardCells = this.levelBuilder.build();
        if (board == null) {
            this.board = boardBuilder
                .eventManager(eventManager)
                .boardCells(boardCells)
                .build();
        } else {
            board.setBoardCells(boardCells);
        }
    }

    private void startStatistic() {
        if (statisticsCollector == null) {
            this.statisticsCollector = new StatisticsCollector();
        }

        statisticsCollector.reset();
        Arrays.stream(BoardEvent.Type.values())
            .forEach(e -> eventManager.subscribe(e, statisticsCollector));
    }

    private void stopStatistic() {
        if (statisticsCollector != null) {
            statisticsCollector.reset();
            Arrays.stream(BoardEvent.Type.values())
                .forEach(e -> eventManager.unsubscribe(e, statisticsCollector));
        }
    }

    public void stop() {
        if (board != null) {
            this.board.setBoardCells(null);
        }

        stopStatistic();

        this.selectedPegPosition = null;
        this.started = false;
    }

    public boolean makeMove(int x, int y) {
        if (selectedPegPosition == null) {
            // TODO: log
            return false;
        }

        if (board.makeMove(selectedPegPosition, new int[]{x, y})) {
            this.selectedPegPosition = null;
            return true;
        }

        return false;
    }

    public boolean undoMove() {
        if (!isStarted()) {
            return false;
        }

        statisticsCollector.reset();
        this.selectedPegPosition = null;
        return board.undoMove();
    }

    public List<int[]> getPossibleMoves() {
        if (selectedPegPosition == null) {
            return Collections.emptyList();
        }

        return board.getPossibleMoves(selectedPegPosition[0], selectedPegPosition[1]);
    }

    /**
     * @return True if and only if peg on this position can be selected.
     */
    public boolean selectPeg(int x, int y) {
        if (!isStarted()) {
            return false;
        }

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

    @Override
    public long getScore() {
        if (statisticsCollector != null) {
            return statisticsCollector.getScore();
        }

        return 0;
    }
}