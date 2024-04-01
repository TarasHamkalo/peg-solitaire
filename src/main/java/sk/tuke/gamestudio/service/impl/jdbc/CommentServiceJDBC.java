package sk.tuke.gamestudio.service.impl.jdbc;

import lombok.AccessLevel;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import sk.tuke.gamestudio.entity.Comment;
import sk.tuke.gamestudio.exception.CommentException;
import sk.tuke.gamestudio.service.CommentService;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;


@Repository
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CommentServiceJDBC implements CommentService {

    public static final String SELECT = """
        SELECT game, player, text, commented_on
        FROM comment
        WHERE game = ?
        ORDER BY commented_on LIMIT 10
    """;

    public static final String DELETE = "DELETE FROM comment";

    public static final String INSERT =
        "INSERT INTO comment (game, player, text, commented_on) VALUES (?, ?, ?, ?)";

    /*
    public static final String INSERT = """
        MERGE INTO comment c
        USING (SELECT CAST(? as varchar)   as game,
                      CAST(? as varchar)   as player,
                      CAST(? as varchar)      text,
                      CAST(? AS timestamp) as commentedOn) AS newData
        ON (c.game = newData.game AND c.player = newData.player)
        WHEN MATCHED THEN
            UPDATE
            SET text        = newData.text,
                commentedOn = newData.commentedOn
        WHEN NOT MATCHED THEN
            INSERT (game, player, text, commentedOn)
            VALUES (newData.game, newData.player, newData.text, newData.commentedOn);
        """;
    */

    @NonNull
    DataSource dataSource;

    @Autowired
    public CommentServiceJDBC(@NonNull DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void addComment(@NonNull Comment comment) throws CommentException {
        try (
            var connection = dataSource.getConnection();
            var preparedInsert = connection.prepareStatement(INSERT);
        ) {
            preparedInsert.setString(1, comment.getGame());
            preparedInsert.setString(2, comment.getPlayer());
            preparedInsert.setString(3, comment.getText());
            preparedInsert.setTimestamp(4, new Timestamp(comment.getCommentedOn().getTime()));
            preparedInsert.executeUpdate();
        } catch (SQLException e) {
            throw new CommentException("Was not able to add comment", e);
        }
    }

    @Override
    public List<Comment> getComments(String game) throws CommentException {
        try (
            var connection = dataSource.getConnection();
            var preparedSelect = connection.prepareStatement(SELECT);
        ) {
            preparedSelect.setString(1, game);

            var resultSet = preparedSelect.executeQuery();

            List<Comment> comments = new ArrayList<>();
            while (resultSet.next()) {
                comments.add(
                    Comment.builder()
                        .game(resultSet.getString(1))
                        .player(resultSet.getString(2))
                        .text(resultSet.getString(3))
                        .commentedOn(resultSet.getTimestamp(4))
                        .build()
                );
            }

            return comments;
        } catch (SQLException e) {
            throw new CommentException("Was not able to read comments", e);
        }
    }

    @Override
    public void reset() throws CommentException {
        try (
            var connection = dataSource.getConnection();
            var preparedDelete = connection.prepareStatement(DELETE);
        ) {
            preparedDelete.executeUpdate();
        } catch (SQLException e) {
            throw new CommentException("Was not able to delete comments", e);
        }
    }
}
