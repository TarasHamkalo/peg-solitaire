package pegsolitaire.game.core.board.events.impl;

import pegsolitaire.game.core.board.commands.BoardCommand;
import pegsolitaire.game.core.board.events.BoardEventHandler;

public class LightningEventHandler implements BoardEventHandler {
    @Override
    public void handle(BoardCommand command) {
        throw new IllegalStateException("not impl");
    }
}
