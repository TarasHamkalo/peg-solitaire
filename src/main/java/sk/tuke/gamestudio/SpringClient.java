package sk.tuke.gamestudio;

import jakarta.persistence.EntityManager;
import lombok.SneakyThrows;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.*;
import sk.tuke.gamestudio.pegsolitaire.game.core.pegs.impl.PegFactoryImpl;
import sk.tuke.gamestudio.pegsolitaire.game.ui.BoardUI;
import sk.tuke.gamestudio.pegsolitaire.game.ui.InputHandler;
import sk.tuke.gamestudio.pegsolitaire.game.ui.Prompt;
import sk.tuke.gamestudio.pegsolitaire.game.ui.impl.BoardUIImpl;
import sk.tuke.gamestudio.pegsolitaire.game.ui.impl.PromptImpl;
import sk.tuke.gamestudio.service.RatingService;
import sk.tuke.gamestudio.service.ScoreService;
import sk.tuke.gamestudio.service.impl.RatingServiceJPA;

import javax.swing.text.html.parser.Entity;

@SpringBootApplication
@Configuration
public class SpringClient {
    public static void main(String[] args) {
        SpringApplication.run(SpringClient.class, args);
    }

    @Bean
    @Profile("prod")
    public CommandLineRunner commandLineRunner(Prompt prompt) {
        return strings -> prompt.begin();
    }

    @Bean
    @Profile("prod")
    public Prompt prompt(BoardUI boardUI, InputHandler serviceInputHandlers) {
        return PromptImpl.builder()
            .additionalInputHandler(serviceInputHandlers)
            .pegFactory(new PegFactoryImpl())
            .boardUI(boardUI)
            .build();
    }

    @Bean
    @SneakyThrows
    @Profile("prod")
    public BoardUI boardUI(ScoreService scoreService) {
        return new BoardUIImpl(scoreService);
    }

    @Bean
    @Profile("prod")
    public InputHandler serviceInputHandlers(InputHandler commentCommandHandler, InputHandler... handlers) {
        return InputHandler.link(commentCommandHandler, handlers);
    }

    @Bean
    public RatingService ratingService(EntityManager entityManager) {
        return new RatingServiceJPA(entityManager);
    }
}
