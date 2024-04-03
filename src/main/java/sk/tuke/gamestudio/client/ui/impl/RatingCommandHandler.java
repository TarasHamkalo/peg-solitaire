package sk.tuke.gamestudio.client.ui.impl;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sk.tuke.gamestudio.commons.entity.Rating;
import sk.tuke.gamestudio.commons.exception.RatingException;
import sk.tuke.gamestudio.client.ui.InputHandler;
import sk.tuke.gamestudio.commons.service.RatingService;

import java.util.regex.Pattern;

@Component
@EqualsAndHashCode(callSuper = false)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RatingCommandHandler extends InputHandler {

    private static final Pattern RATING_CMD = Pattern.compile(
        "^rating\\s(?<cmd>rate|get|avg|reset)\\s?(?<val>\\d+)?(?<name>\\w+)?$"
    );

    @NonNull
    RatingService ratingService;

    @Autowired
    public RatingCommandHandler(@NonNull RatingService ratingService) {
        this.ratingService = ratingService;
    }

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

            } catch (RatingException e) {
                System.out.println(e.getMessage());
            }

            return true;
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
//        if (fValue < 0 || fValue > 5) {
//            System.out.println("The rating has to be between 0 and 5");
//            return;
//        }

        ratingService.setRating(
            Rating.builder()
                .game("pegsolitaire")
                .player(System.getProperty("user.name"))
                .stars(Math.round(fValue))
                .build()
        );

        System.out.println("Rating was added.");
    }
}
