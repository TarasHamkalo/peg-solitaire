import lombok.SneakyThrows;
import pegsolitaire.game.ui.ConsoleUI;
import pegsolitaire.game.ui.impl.ConsoleUIImpl;
import pegsolitaire.game.ui.impl.PromptImpl;

public class Main {
    @SneakyThrows
    public static void main(String[] args) {
        ConsoleUI ui = new ConsoleUIImpl();
        new PromptImpl(ui).play();
    }
}