package pegsolitaire.game.core.board.events;

public interface BoardEventManager {
    void subscribe(BoardEvent.Type type, BoardEventHandler handler);
    void unsubscribe(BoardEvent.Type type, BoardEventHandler handler);
    void publish(BoardEvent event);
}
