package sk.tuke.gamestudio.pegsolitaire.core.board.impl;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import sk.tuke.gamestudio.pegsolitaire.core.board.Board;
import sk.tuke.gamestudio.pegsolitaire.core.board.BoardBuilder;
import sk.tuke.gamestudio.pegsolitaire.core.board.BoardCell;
import sk.tuke.gamestudio.pegsolitaire.core.commands.BoardCommand;
import sk.tuke.gamestudio.pegsolitaire.core.Direction;
import sk.tuke.gamestudio.pegsolitaire.core.commands.impl.*;
import sk.tuke.gamestudio.pegsolitaire.core.events.BoardEvent;
import sk.tuke.gamestudio.pegsolitaire.core.events.BoardEventManager;
import sk.tuke.gamestudio.pegsolitaire.core.pegs.Peg;

import java.util.*;

@Data
@Builder
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BoardImpl implements Board {

    private static final int MOVE_DISTANCE = 2;

    @ToString.Exclude
    Stack<BoardCommand> history = new Stack<>();

    @ToString.Exclude
    Queue<BoardEvent> eventsQueue = new LinkedList<>();

    @NonNull
    BoardEventManager eventManager;

    @NonNull
    @NonFinal
    BoardCell[][] boardCells;


    @Override
    public boolean makeMove(int[] from, int[] to) {
        // mark start of move
        execCommand(UndoMarkerCommand.builder()
            .board(this)
            .initialPosition(from)
            .finalPosition(to)
            .build()
        );


        return execCommand(MoveCommand.builder()
            .board(this)
            .initialPosition(from)
            .finalPosition(to)
            .build()
        );
    }

    @Override
    public boolean putPeg(@NonNull Peg peg, int[] onto) {
        return execCommand(PutCommand.builder()
            .board(this)
            .peg(peg)
            .initialPosition(onto)
            .finalPosition(onto)
            .build()
        );
    }

    @Override
    public boolean removePeg(int[] from) {
        return execCommand(RemoveCommand.builder()
            .board(this)
            .initialPosition(from)
            .finalPosition(from)
            .build()
        );
    }

    @Override
    public boolean destroyCell(int[] on) {
        return execCommand(DestroyCommand.builder()
            .board(this)
            .initialPosition(on)
            .finalPosition(on)
            .build()
        );
    }

    @Override
    @SneakyThrows
    public boolean undoMove() {
        while (!history.isEmpty()) {
            var command = history.pop();
            if (command instanceof UndoMarkerCommand) {
                break;
            }

            if (!command.undo()) {
                history.clear();
                return false;
            }
        }

        return true;
    }

    /**
     * @return if coordinates are valid, then nullable BasicCell on this position
     */
    @Override
    public BoardCell getBoardCellAt(int x, int y) {
        if (x < 0 || y < 0 ||
            x >= boardCells[0].length || y >= boardCells.length) {
            return null;
        }

        return boardCells[y][x];
    }

    @Override
    public List<int[]> getPossibleMoves(int x, int y) {
        var fromCell = getBoardCellAt(x, y);
        if (fromCell == null ||
            !fromCell.getState().equals(BoardCell.State.OCCUPIED)) {
            return Collections.emptyList();
        }

        List<int[]> moves = new ArrayList<>();
        for (Direction direction : Direction.values()) {
            int dx = x + direction.getX() * BoardImpl.MOVE_DISTANCE;
            int dy = y + direction.getY() * BoardImpl.MOVE_DISTANCE;
            int mx = (x + dx) / 2;
            int my = (y + dy) / 2;

            var boardCellDest = getBoardCellAt(dx, dy);
            var boardCellMiddle = getBoardCellAt(mx, my);
            if (boardCellDest == null || boardCellMiddle == null) {
                continue;
            }

            if (boardCellMiddle.getState().equals(BoardCell.State.OCCUPIED) &&
                boardCellDest.getState().equals(BoardCell.State.EMPTY)) {
                moves.add(new int[]{dx, dy});
            }
        }

        return moves;
    }

    @Override
    public boolean isSolved() {
        return getPegsCount() == 1;
    }

    @Override
    public long getPegsCount() {
        return Arrays.stream(this.boardCells)
            .flatMap(Arrays::stream)
            .parallel()
            .filter(Objects::nonNull)
            .filter(b -> b.getState().equals(BoardCell.State.OCCUPIED))
            .count();
    }

    @Override
    public boolean hasAvailableMoves() {
        for (int i = 0; i < boardCells.length; i++) {
            for (int j = 0; j < boardCells[0].length; j++) {
                if (!getPossibleMoves(j, i).isEmpty()) {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public void offerEvent(@NonNull BoardEvent event) {
        eventsQueue.offer(event);
    }

    private boolean execCommand(@NonNull BoardCommand command) {
        if (command.exec()) {
            history.push(command);
            if (!eventsQueue.isEmpty()) {
                eventManager.publish(eventsQueue.poll());
            }

            return true;
        }

        return false;
    }

    @Override
    public BoardCommand peekHistory() {
        if (history.isEmpty()) {
            return null;
        }

        return history.peek();
    }

    @Override
    public void clearHistory() {
        history.clear();
    }

    @Override
    public void setBoardCells(BoardCell[][] boardCells) {
        this.boardCells = boardCells;
        clearHistory();
    }

    public static class BoardImplBuilder implements BoardBuilder {
    }
}