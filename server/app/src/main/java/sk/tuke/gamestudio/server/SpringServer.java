package sk.tuke.gamestudio.server;

import com.github.dozermapper.core.DozerBeanMapperBuilder;
import com.github.dozermapper.core.Mapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.annotation.SessionScope;
import sk.tuke.gamestudio.pegsolitaire.core.board.BoardBuilder;
import sk.tuke.gamestudio.pegsolitaire.core.board.impl.BoardImpl;
import sk.tuke.gamestudio.pegsolitaire.core.events.BoardEventManager;
import sk.tuke.gamestudio.pegsolitaire.core.events.impl.BoardEventManagerImpl;
import sk.tuke.gamestudio.pegsolitaire.core.game.Game;
import sk.tuke.gamestudio.pegsolitaire.core.game.GameUtility;
import sk.tuke.gamestudio.pegsolitaire.core.game.impl.GameImpl;
import sk.tuke.gamestudio.pegsolitaire.core.levels.LevelBuilder;
import sk.tuke.gamestudio.pegsolitaire.core.levels.impl.ClassicLevelBuilder;
import sk.tuke.gamestudio.pegsolitaire.core.pegs.PegFactory;
import sk.tuke.gamestudio.pegsolitaire.core.pegs.impl.PegFactoryImpl;

import java.util.List;

@Configuration
@SpringBootApplication
@EntityScan("sk.tuke.gamestudio.data.entity")
@ComponentScan(
    basePackages = {
        "sk.tuke.gamestudio.data.service.jpa",
        "sk.tuke.gamestudio.server"
    }
)
public class SpringServer {
    public static void main(String[] args) {
        SpringApplication.run(SpringServer.class, args);
    }

    @Bean
    public Mapper mapper() {
        return DozerBeanMapperBuilder.buildDefault();
    }

    @Bean
    @SessionScope
    public Game game() {
        return GameImpl.builder()
            .eventManager(eventManager())
            .boardBuilder(boardBuilder())
            .levelBuilder(levelBuilder())
            .build();
    }

    @Bean
    @SessionScope
    public LevelBuilder levelBuilder() {
        return new ClassicLevelBuilder(pegFactory());
    }

    @Bean
    public List<Class<? extends LevelBuilder>> levelBuilders() {
        return GameUtility.getLevelBuilders();
    }

    @Bean
    @SessionScope
    public PegFactory pegFactory() {
        return new PegFactoryImpl();
    }

    @Bean
    @SessionScope
    public BoardBuilder boardBuilder() {
        return BoardImpl.builder();
    }

    @Bean
    @SessionScope
    public BoardEventManager eventManager() {
        return new BoardEventManagerImpl();
    }
}
