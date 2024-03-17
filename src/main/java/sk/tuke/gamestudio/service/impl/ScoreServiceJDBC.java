package sk.tuke.gamestudio.service.impl;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import sk.tuke.gamestudio.entity.Score;
import sk.tuke.gamestudio.exception.ScoreException;
import sk.tuke.gamestudio.service.ScoreService;

import javax.sql.ConnectionPoolDataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ScoreServiceJDBC implements ScoreService {
    public static final String SELECT = "SELECT game, player, points, playedOn FROM score WHERE game = ? ORDER BY points DESC LIMIT 10";

    public static final String DELETE = "DELETE FROM score";

    public static final String INSERT =
        "INSERT INTO score (game, player, points, playedOn) VALUES (?, ?, ?, ?)";

    /*
    public static final String INSERT = """
        MERGE INTO score s
        USING (SELECT   CAST(? AS varchar) game,
                        CAST(? AS varchar) player,
                        CAST(? AS int) points,
                        CAST(? AS timestamp) playedOn) as newData
        ON (s.game = newData.game AND s.player = newData.player)
        WHEN MATCHED THEN
            UPDATE
            SET points = newData.points, playedOn = newData.playedOn
        WHEN NOT MATCHED THEN
            INSERT (player, game, points, playedOn)
            VALUES (newData.player, newData.game, newData.points, newData.playedOn);
        """;
    */

    @NonNull
    ConnectionPoolDataSource connectionPoolDataSource;

    @Override
    public void addScore(Score score) {
        try (var connection = connectionPoolDataSource.getPooledConnection().getConnection();
             var preparedInsert = connection.prepareStatement(INSERT)) {
            preparedInsert.setString(1, score.getGame());
            preparedInsert.setString(2, score.getPlayer());
            preparedInsert.setInt(3, score.getPoints());
            preparedInsert.setTimestamp(4, new Timestamp(score.getPlayedOn().getTime()));
            preparedInsert.executeUpdate();
        } catch (SQLException e) {
            throw new ScoreException("Problem inserting score", e);
        }
    }

    @Override
    public List<Score> getTopScores(String game) {
        try (var connection = connectionPoolDataSource.getPooledConnection().getConnection();
             var statement = connection.prepareStatement(SELECT)) {
            statement.setString(1, game);
            try (ResultSet rs = statement.executeQuery()) {
                List<Score> scores = new ArrayList<>();
                while (rs.next()) {
                    scores.add(
                        new Score(rs.getString(1), rs.getString(2), rs.getInt(3), rs.getTimestamp(4))
                    );
                }

                return scores;
            }
        } catch (SQLException e) {
            throw new ScoreException("Problem selecting score", e);
        }
    }

    @Override
    public void reset() {
        try (var connection = connectionPoolDataSource.getPooledConnection().getConnection();
             var statement = connection.createStatement();
        ) {
            statement.executeUpdate(DELETE);
        } catch (SQLException e) {
            throw new ScoreException("Problem deleting score", e);
        }
    }
}
