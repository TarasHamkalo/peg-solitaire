package sk.tuke.gamestudio.commons.service.impl.jdbc;

import lombok.AccessLevel;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import sk.tuke.gamestudio.commons.entity.Rating;
import sk.tuke.gamestudio.commons.exception.RatingException;
import sk.tuke.gamestudio.commons.service.RatingService;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.sql.Timestamp;

@Repository
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RatingServiceJDBC implements RatingService {

    public static final String SELECT_SPECIFIC = "SELECT stars FROM rating WHERE game = ? AND player = ?";

    public static final String SELECT_AVG = "SELECT avg(stars) from rating;";

    public static final String DELETE = "DELETE FROM rating";

    public static final String INSERT_UPDATE = """
        MERGE INTO rating r
        USING (SELECT CAST(? as varchar)   as game,
                      CAST(? as varchar)   as player,
                      CAST(? as int)      stars,
                      CAST(? AS timestamp) as rated_on) AS newData
        ON (r.game = newData.game AND r.player = newData.player)
        WHEN MATCHED THEN
            UPDATE
            SET stars   = newData.stars,
                rated_on = newData.rated_on
        WHEN NOT MATCHED THEN
            INSERT (game, player, stars, rated_on)
            VALUES (newData.game, newData.player, newData.stars, newData.rated_on);
        """;

    @NonNull
    DataSource dataSource;

    @Autowired
    public RatingServiceJDBC(@NonNull DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void setRating(Rating rating) throws RatingException {
        try (
            var connection = dataSource.getConnection();
            var preparedInsert = connection.prepareStatement(INSERT_UPDATE);
        ) {
            preparedInsert.setString(1, rating.getGame());
            preparedInsert.setString(2, rating.getPlayer());
            preparedInsert.setInt(3, rating.getStars());
            preparedInsert.setTimestamp(4, new Timestamp(rating.getRatedOn().getTime()));
            preparedInsert.executeUpdate();
        } catch (SQLException e) {
            throw new RatingException("Was not able to add rating", e);
        }

    }

    @Override
    public int getAverageRating(String game) throws RatingException {
        try (
            var connection = dataSource.getConnection();
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
            var connection = dataSource.getConnection();
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
            var connection = dataSource.getConnection();
            var preparedDelete = connection.prepareStatement(DELETE);
        ) {
            preparedDelete.executeUpdate();
        } catch (SQLException e) {
            throw new RatingException("Was not able delete ratings", e);
        }
    }
}
