package sk.tuke.gamestudio.service.impl;

import lombok.AccessLevel;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import org.h2.tools.RunScript;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import sk.tuke.gamestudio.configuration.impl.H2DataSourceConfiguration;
import sk.tuke.gamestudio.entity.Rating;
import sk.tuke.gamestudio.service.RatingService;

import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@FieldDefaults(level = AccessLevel.PRIVATE)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RatingServiceTest {

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
        final var timeStamp = Timestamp.from(Instant.now());

        final var ratings = List.of(
            new Rating(1L, "Zuzka", "pegsolitaire", 5, timeStamp),
            new Rating(2L, "Katka", "pegsolitaire", 3, timeStamp),
            new Rating(3L, "Jaro", "pegsolitaire", 2, timeStamp),
            new Rating(4L, "Jaro", "pegsolitaire", 4, timeStamp)
        );

        ratings.forEach(ratingService::setRating);

        assertEquals(4, ratingService.getAverageRating("pegsolitaire"));
    }

    @Test
    void whenPlayerExistsGetRatingReturnsItsRating() {
        ratingService.reset();

        final var timeStamp = Timestamp.from(Instant.now());
        final var inserted = new Rating(1L, "Zuzka", "pegsolitaire", 5, timeStamp);

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