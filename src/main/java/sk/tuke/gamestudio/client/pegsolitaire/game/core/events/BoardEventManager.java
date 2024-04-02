package sk.tuke.gamestudio.client.pegsolitaire.game.core.events;

public interface BoardEventManager {
    void subscribe(BoardEvent.Type type, BoardEventHandler handler);

    void unsubscribe(BoardEvent.Type type, BoardEventHandler handler);

    void publish(BoardEvent event);

    void clearAll();
}
