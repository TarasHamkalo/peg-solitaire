package sk.tuke.gamestudio.pegsolitaire.game;

import lombok.SneakyThrows;
import sk.tuke.gamestudio.configuration.impl.PostgresDataSourceConfiguration;
import sk.tuke.gamestudio.pegsolitaire.game.core.pegs.impl.PegFactoryImpl;
import sk.tuke.gamestudio.pegsolitaire.game.ui.InputHandler;
import sk.tuke.gamestudio.pegsolitaire.game.ui.impl.*;
import sk.tuke.gamestudio.service.impl.CommentServiceJDBC;
import sk.tuke.gamestudio.service.impl.RatingServiceJDBC;
import sk.tuke.gamestudio.service.impl.ScoreServiceJDBC;

public class Main {

    @SneakyThrows
    public static void main(String[] args) {
        var postgresConf = new PostgresDataSourceConfiguration();
        var dataSource = postgresConf.configureNewFromResource("application.properties");

        var scoreService = new ScoreServiceJDBC(dataSource);
        var serviceInputHandlers = InputHandler.link(
            new RatingCommandHandler(new RatingServiceJDBC(dataSource)),
            new CommentCommandHandler(new CommentServiceJDBC(dataSource)),
            new ScoreCommandHandler(scoreService)
        );


        var boardUI = new BoardUIImpl(scoreService);
        var prompt = PromptImpl.builder()
            .boardUI(boardUI)
            .pegFactory(new PegFactoryImpl())
            .additionalInputHandler(serviceInputHandlers)
            .build();

        prompt.begin();
    }

}