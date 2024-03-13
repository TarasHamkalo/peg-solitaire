package sk.tuke.gamestudio.pegsolitaire.game.ui.impl;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import sk.tuke.gamestudio.entity.Rating;
import sk.tuke.gamestudio.exception.RatingException;
import sk.tuke.gamestudio.pegsolitaire.game.ui.InputHandler;
import sk.tuke.gamestudio.service.RatingService;

import java.util.regex.Pattern;

@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RatingCommandHandler extends InputHandler {

    private static final Pattern RATING_CMD = Pattern.compile(
        "^rating\\s(?<cmd>rate|get|avg|reset)\\s?(?<val>\\d+)?(?<name>\\w+)?$"
    );

    RatingService ratingService;

    @Override
    public boolean handle(String line) {
        var matcher = RATING_CMD.matcher(line);
        if (matcher.matches()) {
            try {
                switch (matcher.group("cmd")) {
                    case "avg" -> printPegsolitaireRating();
                    case "reset" -> ratingService.reset();
                    case "get" -> printRatingByPlayerName(matcher.group("name"));
                    case "rate" -> rate(matcher.group("val"));
                }

                return true;
            } catch (RatingException e) {
                System.out.println(e.getMessage());
            }
        }

        return handleNext(line);
    }

    private void printPegsolitaireRating() {
        System.out.printf("Avg rating is {%d}\n", ratingService.getAverageRating("pegsolitaire"));
    }

    private void printRatingByPlayerName(String name) {
        if (name == null) {
            System.out.println("Player was not provided");
            return;
        }

        int rating = ratingService.getRating("pegsolitaire", name);
        System.out.println(name + " have rated pegsolitaire with " + rating + " stars");
    }

    private void rate(String value) {
        if (value == null) {
            System.out.println("Rating was not provided");
            return;
        }

        float fValue = Float.parseFloat(value);
        if (fValue < 0 || fValue > 5) {
            System.out.println("The rating has to be between 0 and 5");
            return;
        }

        ratingService.setRating(
            Rating.builder()
                .game("pegsolitaire")
                .player(System.getProperty("user.name"))
                .value(Math.round(fValue))
                .build()
        );

        System.out.println("Rating was added.");
    }
}
