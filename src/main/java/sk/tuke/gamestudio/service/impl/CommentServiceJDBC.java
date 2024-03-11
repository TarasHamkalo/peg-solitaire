package sk.tuke.gamestudio.service.impl;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import sk.tuke.gamestudio.entity.Comment;
import sk.tuke.gamestudio.exception.CommentException;
import sk.tuke.gamestudio.service.CommentService;

import javax.sql.ConnectionPoolDataSource;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentServiceJDBC implements CommentService {

    public static final String SELECT = "SELECT game, player, comment, commentedON FROM comment ORDER BY commentedOn ASC LIMIT 10";

    public static final String DELETE = "DELETE FROM comment";

    public static final String INSERT = "INSERT INTO comment (game, player, comment, commentedOn) VALUES (?, ?, ?, ?)";

    @NonNull
    ConnectionPoolDataSource connectionPoolDataSource;

    @Override
    public void addComment(@NonNull Comment comment) throws CommentException {
        try (
            var connection = connectionPoolDataSource.getPooledConnection().getConnection();
            var preparedInsert = connection.prepareStatement(INSERT);
        ) {
            preparedInsert.setString(1, comment.getGame());
            preparedInsert.setString(2, comment.getPlayer());
            preparedInsert.setString(3, comment.getText());
            preparedInsert.setTimestamp(4, comment.getCommentedOn());
            preparedInsert.executeUpdate();
        } catch (SQLException e) {
            throw new CommentException("Was not able to add comment", e);
        }
    }

    @Override
    public List<Comment> getComments(String game) throws CommentException {
        try (
            var connection = connectionPoolDataSource.getPooledConnection().getConnection();
            var preparedSelect = connection.prepareStatement(SELECT);
        ) {
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
            var connection = connectionPoolDataSource.getPooledConnection().getConnection();
            var preparedDelete = connection.prepareStatement(DELETE);
        ) {
            preparedDelete.executeUpdate();
        } catch (SQLException e) {
            throw new CommentException("Was not able to delete comments", e);
        }
    }
}
