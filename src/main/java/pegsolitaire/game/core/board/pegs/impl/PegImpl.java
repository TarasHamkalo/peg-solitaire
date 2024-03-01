package pegsolitaire.game.core.board.pegs.impl;

import lombok.*;
import pegsolitaire.game.core.board.events.BoardEvent;
import pegsolitaire.game.core.board.Color;
import pegsolitaire.game.core.board.pegs.Peg;


@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PegImpl implements Peg {
    @Getter
    @NonNull
    @Builder.Default
    Color color = Color.BRIGHT_YELLOW;

    @NonNull
    @Builder.Default
    BoardEvent.Type moveEventType = BoardEvent.Type.TRIVIAL_MOVE;

    @NonNull
    @Builder.Default
    BoardEvent.Type removeEventType =  BoardEvent.Type.TRIVIAL_REMOVE;

    private PegImpl(@NonNull Peg src) {
        this.color = src.getColor();
        this.moveEventType = src.getMoveEvent();
        this.removeEventType = src.getRemoveEvent();
    }

    @Override
    public Peg clonePeg() {
        return new PegImpl(this);
    }

    @Override
    public void onMove(BoardEvent.Type type) {
        this.moveEventType = (type == null) ? BoardEvent.Type.TRIVIAL_MOVE : type;
    }

    @Override
    public BoardEvent.Type getMoveEvent() {
        return this.moveEventType;
    }

    @Override
    public void onRemove(BoardEvent.Type type) {
        this.removeEventType = (type == null) ? BoardEvent.Type.TRIVIAL_REMOVE : type;
    }

    @Override
    public BoardEvent.Type getRemoveEvent() {
        return this.removeEventType;
    }

    @Override
    public String toString() {
        return "o";
    }
}
