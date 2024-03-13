package sk.tuke.gamestudio.service.impl;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import sk.tuke.gamestudio.entity.Rating;
import sk.tuke.gamestudio.exception.CommentException;
import sk.tuke.gamestudio.exception.RatingException;
import sk.tuke.gamestudio.service.RatingService;

import javax.sql.ConnectionPoolDataSource;
import java.sql.SQLException;

@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RatingServiceJDBC implements RatingService {

    public static final String SELECT_SPECIFIC = "SELECT stars FROM rating WHERE game = ? AND player = ?";

    public static final String SELECT_AVG = "SELECT avg(stars) from rating;";

    public static final String DELETE = "DELETE FROM rating";

    public static final String INSERT_UPDATE = """
        MERGE INTO rating r
        USING (SELECT CAST(? as varchar)   as game,
                      CAST(? as varchar)   as player,
                      CAST(? as int)      stars,
                      CAST(? AS timestamp) as ratedOn) AS newData
        ON (r.game = newData.game AND r.player = newData.player)
        WHEN MATCHED THEN
            UPDATE
            SET stars   = newData.stars,
                ratedOn = newData.ratedOn
        WHEN NOT MATCHED THEN
            INSERT (game, player, stars, ratedOn)
            VALUES (newData.game, newData.player, newData.stars, newData.ratedOn);
        """;

    @NonNull
    ConnectionPoolDataSource connectionPoolDataSource;

    @Override
    public void setRating(Rating rating) throws RatingException {
        try (
            var connection = connectionPoolDataSource.getPooledConnection().getConnection();
            var preparedInsert = connection.prepareStatement(INSERT_UPDATE);
        ) {
            preparedInsert.setString(1, rating.getGame());
            preparedInsert.setString(2, rating.getPlayer());
            preparedInsert.setInt(3, rating.getStars());
            preparedInsert.setTimestamp(4, rating.getRatedOn());
            preparedInsert.executeUpdate();
        } catch (SQLException e) {
            throw new RatingException("Was not able to add rating", e);
        }

    }

    @Override
    public int getAverageRating(String game) throws RatingException {
        try (
            var connection = connectionPoolDataSource.getPooledConnection().getConnection();
            var preparedSelect = connection.prepareStatement(SELECT_AVG);
        ) {
            var resultSet = preparedSelect.executeQuery();
            resultSet.next();
            return resultSet.getInt(1);
        } catch (SQLException e) {
            throw new RatingException("Was not able to get rating", e);
        }
    }

    @Override
    public int getRating(String game, String player) throws RatingException {
        try (
            var connection = connectionPoolDataSource.getPooledConnection().getConnection();
            var preparedSelect = connection.prepareStatement(SELECT_SPECIFIC);
        ) {

            preparedSelect.setString(1, game);
            preparedSelect.setString(2, player);
            var resultSet = preparedSelect.executeQuery();
            resultSet.next();
            return resultSet.getInt(1);
        } catch (SQLException e) {
            throw new RatingException("Was not able to get rating", e);
        }
    }

    @Override
    public void reset() throws RatingException {
        try (
            var connection = connectionPoolDataSource.getPooledConnection().getConnection();
            var preparedDelete = connection.prepareStatement(DELETE);
        ) {
            preparedDelete.executeUpdate();
        } catch (SQLException e) {
            throw new RatingException("Was not able delete ratings", e);
        }
    }
}
