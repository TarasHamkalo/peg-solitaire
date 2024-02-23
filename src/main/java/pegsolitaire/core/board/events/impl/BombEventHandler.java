package pegsolitaire.core.board.events.impl;

import pegsolitaire.core.board.commands.BoardCommand;
import pegsolitaire.core.board.events.BoardEventHandler;

public class BombEventHandler implements BoardEventHandler {
    @Override
    public void handle(BoardCommand command) {
        throw new IllegalStateException("not impl");
    }
}
