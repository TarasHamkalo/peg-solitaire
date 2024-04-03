package sk.tuke.gamestudio.client;

import lombok.SneakyThrows;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.web.client.RestTemplate;
import sk.tuke.gamestudio.client.pegsolitaire.game.core.pegs.impl.PegFactoryImpl;
import sk.tuke.gamestudio.client.service.rest.ScoresServiceRestClient;
import sk.tuke.gamestudio.client.ui.BoardUI;
import sk.tuke.gamestudio.client.ui.InputHandler;
import sk.tuke.gamestudio.client.ui.Prompt;
import sk.tuke.gamestudio.client.ui.impl.BoardUIImpl;
import sk.tuke.gamestudio.client.ui.impl.PromptImpl;
import sk.tuke.gamestudio.commons.service.ScoreService;

@SpringBootApplication(
    exclude = {
        DataSourceAutoConfiguration.class,
        DataSourceTransactionManagerAutoConfiguration.class,
        HibernateJpaAutoConfiguration.class
    }
)
@EntityScan("sk.tuke.gamestudio.commons.entity")
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
    public BoardUI boardUI() {
        return new BoardUIImpl(scoreService());
    }

    @Bean
    @Profile("prod")
    public InputHandler serviceInputHandlers(InputHandler commentCommandHandler, InputHandler... other) {
        return InputHandler.link(commentCommandHandler, other);
    }

    @Bean
    @Profile("prod")
    public ScoreService scoreService() {
        return new ScoresServiceRestClient();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
