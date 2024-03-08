package pegsolitaire.game.core.game;

import lombok.NonNull;
import org.reflections.Reflections;
import pegsolitaire.game.core.levels.LevelBuilder;
import pegsolitaire.game.core.pegs.PegFactory;

import java.util.ArrayList;
import java.util.List;

public class GameUtility {
    private GameUtility() {
    }

    public static LevelBuilder getInstanceOfLevelBuilder(
            @NonNull Class<? extends LevelBuilder> levelBuilder, @NonNull PegFactory pegFactory) {
        try {
            var constructor = levelBuilder.getDeclaredConstructor(PegFactory.class);
            return constructor.newInstance(pegFactory);
        } catch (ReflectiveOperationException ignore) {
            return null;
        }
    }

    public static List<Class<? extends LevelBuilder>> getLevelBuilders() {
        var reflections = new Reflections("pegsolitaire.game.core.levels");
        return new ArrayList<>(
            reflections.getSubTypesOf(LevelBuilder.class)
        );
    }
}
