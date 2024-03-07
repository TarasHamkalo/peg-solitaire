package pegsolitaire.game.core.pegs.impl;

import lombok.*;
import pegsolitaire.game.core.events.BoardEvent;
import pegsolitaire.game.core.Color;
import pegsolitaire.game.core.pegs.Peg;


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
