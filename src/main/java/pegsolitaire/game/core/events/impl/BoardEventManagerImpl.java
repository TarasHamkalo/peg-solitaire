package pegsolitaire.game.core.events.impl;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import pegsolitaire.game.core.events.BoardEvent;
import pegsolitaire.game.core.events.BoardEventHandler;
import pegsolitaire.game.core.events.BoardEventManager;

import java.util.*;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BoardEventManagerImpl implements BoardEventManager {

    Map<BoardEvent.Type, Set<BoardEventHandler>> eventToHandlers;

    public BoardEventManagerImpl() {
        this.eventToHandlers = new EnumMap<>(BoardEvent.Type.class);
    }

    @Override
    public void subscribe(BoardEvent.Type type, BoardEventHandler handler) {
        if (this.eventToHandlers.getOrDefault(type, Collections.emptySet()).contains(handler)) {
            return;
        }

        this.eventToHandlers.computeIfAbsent(type, t -> new HashSet<>()).add(handler);
    }

    @Override
    public void unsubscribe(BoardEvent.Type type, BoardEventHandler handler) {
        this.eventToHandlers.computeIfAbsent(type, t -> new HashSet<>()).remove(handler);
    }

    @Override
    public void publish(BoardEvent event) {
        this.eventToHandlers
            .getOrDefault(event.getEventType(), Collections.emptySet())
            .forEach(handler -> handler.handle(event));
    }

    @Override
    public void clearAll() {
        this.eventToHandlers.clear();
    }
}
