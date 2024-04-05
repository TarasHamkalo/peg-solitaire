package sk.tuke.gamestudio.service;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import sk.tuke.gamestudio.data.entity.Rating;
import sk.tuke.gamestudio.data.service.RatingService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

@EnableAutoConfiguration
@EntityScan("sk.tuke.gamestudio.data.entity")
@FieldDefaults(level = AccessLevel.PRIVATE)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class RatingServiceTest {

    @Autowired
    RatingService ratingService;

    @Test
    void whenRatingWithGivenPlayerDoesNotExistsShouldBeInserted() {
        ratingService.reset();
        final var inserted = Rating.builder()
            .game("pegsolitaire")
            .player("Taras")
            .stars(5)
            .build();

        assertDoesNotThrow(() -> ratingService.setRating(inserted));

        var retrieved = ratingService.getRating(inserted.getGame(), inserted.getPlayer());
        assertEquals(inserted.getStars(), retrieved);
    }

    @Test
    void whenRatingGivenPlayerExistsRatingShouldBeUpdated() {
        ratingService.reset();
        final var existed = Rating.builder()
            .game("pegsolitaire")
            .player("Taras")
            .stars(5)
            .build();

        final var inserted = Rating.builder()
            .game("pegsolitaire")
            .player("Taras")
            .stars(1)
            .build();

        ratingService.setRating(existed);

        ratingService.setRating(inserted);

        var retrieved = ratingService.getRating(inserted.getGame(), inserted.getPlayer());
        assertEquals(inserted.getStars(), retrieved);
    }

    @Test
    void whenInsertedAvgRatingShouldEqual4() {
        ratingService.reset();
        final var ratings = List.of(
            new Rating("Zuzka", "pegsolitaire", 5),
            new Rating("Katka", "pegsolitaire", 3),
            new Rating("Jaro", "pegsolitaire", 2),
            new Rating("Jaro", "pegsolitaire", 4)
        );

        ratings.forEach(ratingService::setRating);

        assertEquals(4, ratingService.getAverageRating("pegsolitaire"));
    }

    @Test
    void whenPlayerExistsGetRatingReturnsItsRating() {
        ratingService.reset();

        final var inserted = new Rating("Zuzka", "pegsolitaire", 5);

        ratingService.setRating(inserted);

        var retrieved = ratingService.getRating(inserted.getGame(), inserted.getPlayer());
        assertEquals(inserted.getStars(), retrieved);
    }

    @Test
    void afterResetIsCalledAvgRatingShouldBe0() {
        ratingService.reset();

        assertEquals(0, ratingService.getAverageRating("pegsolitaire"));
    }
}