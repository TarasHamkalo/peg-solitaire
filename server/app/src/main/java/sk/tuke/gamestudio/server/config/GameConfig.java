package sk.tuke.gamestudio.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.annotation.ApplicationScope;
import org.springframework.web.context.annotation.SessionScope;
import sk.tuke.gamestudio.pegsolitaire.core.board.BoardBuilder;
import sk.tuke.gamestudio.pegsolitaire.core.board.impl.BoardImpl;
import sk.tuke.gamestudio.pegsolitaire.core.events.BoardEvent;
import sk.tuke.gamestudio.pegsolitaire.core.events.BoardEventHandler;
import sk.tuke.gamestudio.pegsolitaire.core.events.BoardEventManager;
import sk.tuke.gamestudio.pegsolitaire.core.events.impl.BoardEventManagerImpl;
import sk.tuke.gamestudio.pegsolitaire.core.events.impl.BombEventHandler;
import sk.tuke.gamestudio.pegsolitaire.core.events.impl.LightningEventHandler;
import sk.tuke.gamestudio.pegsolitaire.core.game.Game;
import sk.tuke.gamestudio.pegsolitaire.core.game.GameUtility;
import sk.tuke.gamestudio.pegsolitaire.core.game.impl.GameImpl;
import sk.tuke.gamestudio.pegsolitaire.core.levels.LevelBuilder;
import sk.tuke.gamestudio.pegsolitaire.core.levels.impl.ClassicLevelBuilder;
import sk.tuke.gamestudio.pegsolitaire.core.pegs.PegFactory;
import sk.tuke.gamestudio.pegsolitaire.core.pegs.impl.PegFactoryImpl;
import sk.tuke.gamestudio.server.dto.SetupForm;
import sk.tuke.gamestudio.server.events.EventsDetector;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

@Configuration
public class GameConfig {

    @Bean
    @SessionScope
    public Game game() {
        return GameImpl.builder()
            .eventManager(boardEventManager())
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
    public SetupForm setupForm() {
        return new SetupForm();
    }

    @Bean
    @SessionScope
    public BoardEventManager boardEventManager() {
        var boardEventManager = new BoardEventManagerImpl();

        eventToHandlers().forEach(boardEventManager::subscribe);

        Arrays.stream(BoardEvent.Type.values())
            .forEach(t -> boardEventManager.subscribe(t, eventsDetector()));

        return boardEventManager;
    }

    @Bean
    @SessionScope
    public EventsDetector eventsDetector() {
        return new EventsDetector();
    }

    @Bean
    @ApplicationScope
    public List<Class<? extends LevelBuilder>> levelBuilders() {
        return GameUtility.getLevelBuilders();
    }

    @Bean
    @ApplicationScope
    public Map<BoardEvent.Type, BoardEventHandler> eventToHandlers() {
        return Map.ofEntries(
            Map.entry(BoardEvent.Type.BOMB, new BombEventHandler()),
            Map.entry(BoardEvent.Type.LIGHTNING, new LightningEventHandler()),
            Map.entry(BoardEvent.Type.TRIVIAL_MOVE, command -> {}),
            Map.entry(BoardEvent.Type.TRIVIAL_REMOVE, command -> {})
        );
    }

    @Bean
    @ApplicationScope
    public List<BoardEvent.Type> events() {
        final var exclude = List.of(
            BoardEvent.Type.TRIVIAL_MOVE, BoardEvent.Type.TRIVIAL_REMOVE
        );

        return Arrays.stream(BoardEvent.Type.values())
            .filter(Predicate.not(exclude::contains))
            .toList();
    }

}