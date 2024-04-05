package sk.tuke.gamestudio.client;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import sk.tuke.gamestudio.client.service.CommentService;
import sk.tuke.gamestudio.client.service.rest.CommentServiceRestClient;
import sk.tuke.gamestudio.client.ui.BoardUI;
import sk.tuke.gamestudio.client.ui.InputHandler;
import sk.tuke.gamestudio.client.ui.Prompt;
import sk.tuke.gamestudio.client.ui.impl.PromptImpl;
import sk.tuke.gamestudio.pegsolitaire.core.pegs.impl.PegFactoryImpl;

@SpringBootApplication
public class SpringClient {
    public static void main(String[] args) {
        new SpringApplicationBuilder(SpringClient.class).web(WebApplicationType.NONE).run(args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(Prompt prompt) {
        return strings -> prompt.begin();
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
    public InputHandler serviceInputHandlers(InputHandler commentCommandHandler, InputHandler... other) {
        return InputHandler.link(commentCommandHandler, other);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public CommentService commentService() {
        return new CommentServiceRestClient();
    }
}
