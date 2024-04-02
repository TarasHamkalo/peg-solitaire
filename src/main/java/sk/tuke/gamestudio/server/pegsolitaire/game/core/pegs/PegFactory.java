package sk.tuke.gamestudio.server.pegsolitaire.game.core.pegs;

import lombok.NonNull;
import sk.tuke.gamestudio.server.pegsolitaire.game.core.events.BoardEvent;

import java.util.List;

public interface PegFactory {
    Peg getRandomPeg();

    void addIfNotPresent(@NonNull BoardEvent.Type pegEvent);

    List<BoardEvent.Type> getPegEvents();

    void clearPegEvents();
}
