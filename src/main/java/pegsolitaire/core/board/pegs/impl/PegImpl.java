package pegsolitaire.core.board.pegs.impl;

import lombok.Builder;
import lombok.Value;
import pegsolitaire.core.board.events.BoardEvent;
import pegsolitaire.core.board.pegs.Peg;

import java.awt.*;

@Value
@Builder
public class PegImpl implements Peg {
    Color color;
    BoardEvent.Type moveEventType;
    BoardEvent.Type removeEventType;
}
