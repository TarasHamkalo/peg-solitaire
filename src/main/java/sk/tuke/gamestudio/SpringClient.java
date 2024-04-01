package sk.tuke.gamestudio;

import lombok.SneakyThrows;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Profile;
import sk.tuke.gamestudio.pegsolitaire.game.core.pegs.impl.PegFactoryImpl;
import sk.tuke.gamestudio.pegsolitaire.game.ui.BoardUI;
import sk.tuke.gamestudio.pegsolitaire.game.ui.InputHandler;
import sk.tuke.gamestudio.pegsolitaire.game.ui.Prompt;
import sk.tuke.gamestudio.pegsolitaire.game.ui.impl.BoardUIImpl;
import sk.tuke.gamestudio.pegsolitaire.game.ui.impl.PromptImpl;
import sk.tuke.gamestudio.service.ScoreService;

@SpringBootApplication
@ComponentScan(excludeFilters = @ComponentScan.Filter(type = FilterType.REGEX, pattern = "sk.tuke.gamestudio.service.impl.jdbc.*"))
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
}
