package pegsolitaire.game;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import pegsolitaire.game.core.Game;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ConsoleUI {
    Game game;
}
