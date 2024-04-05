package sk.tuke.gamestudio.client.ui.impl;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sk.tuke.gamestudio.client.service.ScoreService;
import sk.tuke.gamestudio.client.service.exception.ServiceException;
import sk.tuke.gamestudio.client.ui.InputHandler;

import java.util.regex.Pattern;

@Component
@EqualsAndHashCode(callSuper = false)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ScoreCommandHandler extends InputHandler {

    private static final Pattern SCORE_CMD = Pattern.compile("^score\\stop$");

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
                scoreService.getTopScores("pegsolitaire").forEach(System.out::println);
            } catch (ServiceException e) {
                System.out.println(e.getMessage());
            }

            return true;
        }

        return handleNext(line);
    }
}
