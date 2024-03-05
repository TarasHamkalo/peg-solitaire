package pegsolitaire.game.core.game;

import org.reflections.Reflections;
import pegsolitaire.game.core.levels.LevelBuilder;

import java.util.ArrayList;
import java.util.List;

public class GameUtility {
    private GameUtility() {
    }

    public static LevelBuilder getInstanceOfLevelBuilder(
        Class<? extends LevelBuilder> levelBuilder) {
        try {
            return levelBuilder.getDeclaredConstructor().newInstance();
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
