import lombok.SneakyThrows;
import pegsolitaire.game.core.pegs.impl.PegFactoryImpl;
import pegsolitaire.game.ui.ConsoleUI;
import pegsolitaire.game.ui.impl.ConsoleUIImpl;
import pegsolitaire.game.ui.impl.PromptImpl;

public class Main {

    @SneakyThrows
    public static void main(String[] args) {
        var ui = new ConsoleUIImpl();
        var pegFactory = new PegFactoryImpl();
        var prompt = new PromptImpl(ui, pegFactory);
        prompt.begin();
    }

}