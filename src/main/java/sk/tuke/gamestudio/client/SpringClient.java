package sk.tuke.gamestudio.client;

import lombok.SneakyThrows;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Profile;
import sk.tuke.gamestudio.client.ui.BoardUI;
import sk.tuke.gamestudio.client.ui.InputHandler;
import sk.tuke.gamestudio.client.ui.Prompt;
import sk.tuke.gamestudio.client.ui.impl.BoardUIImpl;
import sk.tuke.gamestudio.client.ui.impl.PromptImpl;
import sk.tuke.gamestudio.commons.service.ScoreService;
import sk.tuke.gamestudio.server.pegsolitaire.game.core.pegs.impl.PegFactoryImpl;

@SpringBootApplication
@EntityScan("sk.tuke.gamestudio.commons.entity")
@ComponentScan(
    basePackages = {
        "sk.tuke.gamestudio.client.ui",
        "sk.tuke.gamestudio.commons.entity",
        "sk.tuke.gamestudio.commons.service.impl.jpa",
    }
)
public class SpringClient {
    public static void main(String[] args) {
        new SpringApplicationBuilder(SpringClient.class).web(WebApplicationType.NONE).run(args);
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
    public InputHandler serviceInputHandlers(InputHandler commentCommandHandler, InputHandler... other) {
        return InputHandler.link(commentCommandHandler, other);
    }

}
