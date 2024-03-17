package sk.tuke.gamestudio.service.impl;

import lombok.AccessLevel;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import org.h2.tools.RunScript;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import sk.tuke.gamestudio.configuration.impl.H2DataSourceConfiguration;
import sk.tuke.gamestudio.entity.Score;
import sk.tuke.gamestudio.exception.ScoreException;
import sk.tuke.gamestudio.service.ScoreService;

import java.io.InputStreamReader;
import java.time.Instant;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@FieldDefaults(level = AccessLevel.PRIVATE)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ScoreServiceJDBCTest {

    ScoreService scoreService;

    @BeforeAll
    @SneakyThrows
    void setUp() {
        var h2DataSourceConfiguration = new H2DataSourceConfiguration();
        h2DataSourceConfiguration.configureNewFromResource("test.properties");

        var connection = h2DataSourceConfiguration.getDataSource()
            .getPooledConnection()
            .getConnection();

        var createSqlReader = new InputStreamReader(
            ClassLoader.getSystemResourceAsStream("create.sql")
        );

        RunScript.execute(connection, createSqlReader);
        this.scoreService = new ScoreServiceJDBC(h2DataSourceConfiguration.getDataSource());
    }

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
    void whenScoreWithGivenPlayerExistsAddShouldThrowScoreException() {
        scoreService.reset();

        final var existed = Score.builder()
            .player("Taras")
            .game("pegsolitaire")
            .points(100)
            .build();

        final var inserted = Score.builder()
            .player("Taras")
            .game("pegsolitaire")
            .points(120)
            .build();

        scoreService.addScore(existed);

        assertThrows(ScoreException.class, () -> scoreService.addScore(inserted));

        var retrieved = scoreService.getTopScores("pegsolitaire");
        assertEquals(1, retrieved.size());
        assertEquals(existed, retrieved.get(0));
    }

    @Test
    void whenTopScoresCalledScoresShouldBeRetrievedSortedByScore() {
        scoreService.reset();
        final var date = Date.from(Instant.now());

        final var scoreInserted = List.of(
            new Score("pegsolitaire", "Zuzka", 180, date),
            new Score("pegsolitaire", "Katka", 150, date),
            new Score("pegsolitaire", "Jaro", 120, date)
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