package sk.tuke.gamestudio.client.pegsolitaire.game.core.pegs.impl;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import sk.tuke.gamestudio.client.pegsolitaire.game.core.events.BoardEvent;
import sk.tuke.gamestudio.client.pegsolitaire.game.core.pegs.Peg;
import sk.tuke.gamestudio.client.pegsolitaire.game.core.pegs.PegFactory;
import sk.tuke.gamestudio.client.pegsolitaire.game.core.Color;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static sk.tuke.gamestudio.client.pegsolitaire.game.core.Color.*;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PegFactoryImpl implements PegFactory {

    Map<BoardEvent.Type, Color> typeColorMap = Map.ofEntries(
        Map.entry(BoardEvent.Type.TRIVIAL_MOVE, BRIGHT_YELLOW),
        Map.entry(BoardEvent.Type.TRIVIAL_REMOVE, BRIGHT_YELLOW),
        Map.entry(BoardEvent.Type.BOMB, BRIGHT_RED),
        Map.entry(BoardEvent.Type.LIGHTNING, BRIGHT_BLUE)
    );

    @Getter
    @NonNull
    List<BoardEvent.Type> pegEvents = new ArrayList<>();

    Random random = new SecureRandom();

    @Override
    public Peg getRandomPeg() {
        if (random.nextInt(7) == 1 && !pegEvents.isEmpty()) {
            var event = pegEvents.get(random.nextInt(pegEvents.size()));
            return PegImpl.builder()
                .moveEventType(event)
                .removeEventType(event)
                .color(typeColorMap.get(event))
                .build();
        }

        return new PegImpl();
    }

    @Override
    public void addIfNotPresent(@NonNull BoardEvent.Type pegEvent) {
        if (!pegEvents.contains(pegEvent)) {
            pegEvents.add(pegEvent);
        }
    }

    @Override
    public void clearPegEvents() {
        pegEvents.clear();
    }
}
