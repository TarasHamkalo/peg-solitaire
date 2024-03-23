package sk.tuke.gamestudio.pegsolitaire.game.ui.impl;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sk.tuke.gamestudio.exception.ScoreException;
import sk.tuke.gamestudio.pegsolitaire.game.ui.InputHandler;
import sk.tuke.gamestudio.service.ScoreService;

import java.util.regex.Pattern;

@Component
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ScoreCommandHandler extends InputHandler {

    private static final Pattern SCORE_CMD = Pattern.compile("^score\\s(?<cmd>top|reset)$");

    @NonNull
    ScoreService scoreService;

    @Autowired
    public ScoreCommandHandler(@NonNull ScoreService scoreService) {
        this.scoreService = scoreService;
    }

    @Override
    public boolean handle(String line) {
        var matcher = SCORE_CMD.matcher(line);
        if (matcher.matches()) {
            try {
                if (matcher.group("cmd").equals("top")) {
                    scoreService.getTopScores("pegsolitaire").forEach(System.out::println);
                } else {
                    scoreService.reset();
                    System.out.println("Scores are removed");
                }
            } catch (ScoreException e) {
                System.out.println(e.getMessage());
            }

            return true;
        }

        return handleNext(line);
    }
}
