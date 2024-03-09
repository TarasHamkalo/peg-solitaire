package sk.tuke.gamestudio.pegsolitaire.game;

import lombok.SneakyThrows;
import org.postgresql.ds.PGConnectionPoolDataSource;
import sk.tuke.gamestudio.pegsolitaire.game.core.pegs.impl.PegFactoryImpl;
import sk.tuke.gamestudio.pegsolitaire.game.ui.impl.ConsoleUIImpl;
import sk.tuke.gamestudio.pegsolitaire.game.ui.impl.PromptImpl;

import java.io.FileInputStream;
import java.util.Properties;

//import org.postgresql.
public class Main {

    @SneakyThrows
    public static void main(String[] args) {

        Properties properties = new Properties();

        System.getProperty("user.dir");

        System.out.println(Main.class.getResource("main/application.properties"));
//        properties.load(toURI()));
//        properties.forEach((k, v) -> System.out.println(k + ":" + v));
//        new PGConnectionPoolDataSource()
//        var ui = new ConsoleUIImpl();
//        var pegFactory = new PegFactoryImpl();
//        var prompt = new PromptImpl(ui, pegFactory);
//        prompt.begin();
    }

}