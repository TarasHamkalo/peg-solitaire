package sk.tuke.gamestudio.client.ui.impl;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sk.tuke.gamestudio.client.service.RatingService;
import sk.tuke.gamestudio.client.service.exception.ServiceException;
import sk.tuke.gamestudio.client.ui.InputHandler;
import sk.tuke.gamestudio.server.api.rest.dto.RatingDto;

import java.util.regex.Pattern;

@Component
@EqualsAndHashCode(callSuper = false)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RatingCommandHandler extends InputHandler {

    private static final Pattern RATING_CMD = Pattern.compile(
        "^rating\\s(?<cmd>rate|get|avg)\\s?(?<val>\\d+)?(?<name>\\w+)?$"
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
                    case "get" -> printRatingByPlayerName(matcher.group("name"));
                    case "rate" -> rate(matcher.group("val"));
                }

            } catch (ServiceException e) {
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
        ratingService.setRating(
            RatingDto.builder()
                .game("pegsolitaire")
                .player(System.getProperty("user.name"))
                .stars(Math.round(fValue))
                .build()
        );

        System.out.println("Rating was added.");
    }
}
