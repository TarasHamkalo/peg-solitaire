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
import sk.tuke.gamestudio.service.ScoreService;

import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@FieldDefaults(level = AccessLevel.PRIVATE)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ScoreServiceJDBCTest {

    ScoreService scoreService;

    private static void assertScore(Score expected, Score actual) {
        assertEquals(expected, actual);
        assertEquals(expected.getPlayedOn().getTime(), actual.getPlayedOn().getTime());
    }

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
        assertScore(expected, retrieved.get(0));
    }

    @Test
    void getTopScores() {
        scoreService.reset();
        final var timeStamp = Timestamp.from(Instant.now());

        final var scoreInserted = List.of(
            new Score("pegsolitaire", "Zuzka", 180, timeStamp),
            new Score("pegsolitaire", "Katka", 150, timeStamp),
            new Score("pegsolitaire", "Jaro", 120, timeStamp),
            new Score("pegsolitaire", "Jaro", 100, timeStamp)
        );

        scoreInserted.forEach(scoreService::addScore);

        var scoresRetrieved = scoreService.getTopScores("pegsolitaire");

        assertEquals(4, scoresRetrieved.size());
        for (int i = 0; i < scoreInserted.size(); i++) {
            assertScore(scoreInserted.get(0), scoresRetrieved.get(0));
        }
    }

    @Test
    void afterResetIsCalledTopScoresShouldBeEmpty() {
        scoreService.reset();

        assertEquals(0, scoreService.getTopScores("pegsoliatire").size());
    }
}