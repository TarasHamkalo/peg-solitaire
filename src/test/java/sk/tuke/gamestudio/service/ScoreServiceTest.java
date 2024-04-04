package sk.tuke.gamestudio.service;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import sk.tuke.gamestudio.commons.entity.Score;
import sk.tuke.gamestudio.commons.service.ScoreService;
import sk.tuke.gamestudio.server.SpringServer;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@EnableAutoConfiguration
@EntityScan("sk.tuke.gamestudio.commons.entity")
@FieldDefaults(level = AccessLevel.PRIVATE)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class ScoreServiceTest {

    @Autowired
    ScoreService scoreService;

    @Test
    void whenScoreIsAddedToEmptyTableTopScoresShouldContainIt() {
        scoreService.reset();

        final var expected = Score.builder()
            .player("Taras")
            .game("pegsolitaire")
            .points(100)
            .build();


        // when
        scoreService.addScore(expected);

        // then
        var retrieved = scoreService.getTopScores("pegsolitaire");
        assertEquals(1, retrieved.size());
        assertEquals(expected, retrieved.get(0));
    }

    @Test
    void whenTopScoresCalledScoresShouldBeRetrievedSortedByScore() {
        scoreService.reset();

        final var scoreInserted = List.of(
            new Score("pegsolitaire", "Zuzka", 180),//, date),
            new Score("pegsolitaire", "Katka", 150),//, date),
            new Score("pegsolitaire", "Jaro", 120)//, date)
        );

        scoreInserted.forEach(scoreService::addScore);

        var scoresRetrieved = scoreService.getTopScores("pegsolitaire");

        assertEquals(3, scoresRetrieved.size());
        for (int i = 0; i < scoreInserted.size(); i++) {
            assertEquals(scoreInserted.get(0), scoresRetrieved.get(0));
        }
    }

    @Test
    void afterResetIsCalledTopScoresShouldBeEmpty() {
        scoreService.reset();

        assertEquals(0, scoreService.getTopScores("pegsoliatire").size());
    }
}