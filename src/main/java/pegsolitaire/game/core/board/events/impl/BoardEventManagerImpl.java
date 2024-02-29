package pegsolitaire.game.core.board.events.impl;

import pegsolitaire.game.core.board.events.BoardEvent;
import pegsolitaire.game.core.board.events.BoardEventHandler;
import pegsolitaire.game.core.board.events.BoardEventManager;

public class BoardEventManagerImpl implements BoardEventManager {
    @Override
    public void subscribe(BoardEvent.Type type, BoardEventHandler handler) {
        throw new IllegalStateException();
    }

    @Override
    public void unsubscribe(BoardEvent.Type type, BoardEventHandler handler) {
        throw new IllegalStateException();
    }

    @Override
    public void publish(BoardEvent event) {
        throw new IllegalStateException();
    }
}
