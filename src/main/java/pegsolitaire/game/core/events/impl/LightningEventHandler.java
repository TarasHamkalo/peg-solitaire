package pegsolitaire.game.core.events.impl;

import pegsolitaire.game.core.events.BoardEvent;
import pegsolitaire.game.core.events.BoardEventHandler;

public class LightningEventHandler implements BoardEventHandler {
    @Override
    public void handle(BoardEvent event) {
        throw new IllegalStateException("not impl");
    }
}
