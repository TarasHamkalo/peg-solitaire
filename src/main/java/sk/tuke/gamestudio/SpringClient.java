package sk.tuke.gamestudio;

import lombok.SneakyThrows;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import sk.tuke.gamestudio.pegsolitaire.game.core.pegs.impl.PegFactoryImpl;
import sk.tuke.gamestudio.pegsolitaire.game.ui.BoardUI;
import sk.tuke.gamestudio.pegsolitaire.game.ui.InputHandler;
import sk.tuke.gamestudio.pegsolitaire.game.ui.Prompt;
import sk.tuke.gamestudio.pegsolitaire.game.ui.impl.BoardUIImpl;
import sk.tuke.gamestudio.pegsolitaire.game.ui.impl.PromptImpl;
import sk.tuke.gamestudio.service.ScoreService;


@SpringBootApplication
public class SpringClient {
    public static void main(String[] args) {
        SpringApplication.run(SpringClient.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(Prompt prompt) {
        return (strings) -> prompt.begin();
    }

    @Bean
    public Prompt prompt(BoardUI boardUI, InputHandler serviceInputHandlers) {
        return PromptImpl.builder()
            .additionalInputHandler(serviceInputHandlers)
            .pegFactory(new PegFactoryImpl())
            .boardUI(boardUI)
            .build();
    }

    @Bean
    @SneakyThrows
    public BoardUI boardUI(ScoreService scoreService) {
        return new BoardUIImpl(scoreService);
    }


    @Bean
    public InputHandler serviceInputHandlers(InputHandler commentCommandHandler, InputHandler... handlers) {
        return InputHandler.link(commentCommandHandler, handlers);
    }

}
