package sk.tuke.gamestudio.pegsolitaire.game.ui.impl;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import sk.tuke.gamestudio.exception.ScoreException;
import sk.tuke.gamestudio.pegsolitaire.game.ui.InputHandler;
import sk.tuke.gamestudio.service.ScoreService;

import java.util.regex.Pattern;

@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ScoreCommandHandler extends InputHandler {

    private static final Pattern SCORE_CMD = Pattern.compile("^score\\s(?<cmd>top|reset)$");

    ScoreService scoreService;

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
