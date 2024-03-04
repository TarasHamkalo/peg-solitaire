import lombok.SneakyThrows;
import pegsolitaire.game.ui.ConsoleUI;
import pegsolitaire.game.ui.impl.ConsoleUIImpl;
import pegsolitaire.game.ui.impl.PromptImpl;

import java.io.Console;

public class Main {
    @SneakyThrows
    public static void main(String[] args) {
        ConsoleUI ui = new ConsoleUIImpl();
//        System.out.print("\033[?1049h");
        new PromptImpl(ui).begin();
    }
}