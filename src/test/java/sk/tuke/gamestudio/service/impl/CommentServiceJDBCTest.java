package sk.tuke.gamestudio.service.impl;

import lombok.AccessLevel;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import org.h2.tools.RunScript;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import sk.tuke.gamestudio.configuration.impl.H2DataSourceConfiguration;
import sk.tuke.gamestudio.entity.Comment;
import sk.tuke.gamestudio.exception.CommentException;
import sk.tuke.gamestudio.service.CommentService;

import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@FieldDefaults(level = AccessLevel.PRIVATE)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CommentServiceJDBCTest {

    CommentService commentService;

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
        this.commentService = new CommentServiceJDBC(h2DataSourceConfiguration.getDataSource());
    }

    @Test
    void whenCommentWithGivenPlayerDoesNotExistShouldBeInserted() {
        commentService.reset();
        final var inserted = Comment.builder()
            .game("pegsolitaire")
            .player("Taras")
            .text("My first comment")
            .build();

        assertDoesNotThrow(() -> commentService.addComment(inserted));

        var retrievedComments = commentService.getComments(inserted.getGame());
        assertEquals(1, retrievedComments.size());

        assertEquals(inserted, retrievedComments.get(0));
    }

    @Test
    void whenCommentWithGivenPlayerExistsAddShouldThrowException() {
        commentService.reset();
        final var existed = Comment.builder()
            .game("pegsolitaire")
            .player("Taras")
            .text("My first comment")
            .build();

        final var inserted = Comment.builder()
            .game("pegsolitaire")
            .player("Taras")
            .text("My second comment")
            .build();

        commentService.addComment(existed);

        assertThrows(CommentException.class, () -> commentService.addComment(inserted));

        var retrievedComments = commentService.getComments(inserted.getGame());
        assertEquals(1, retrievedComments.size());

        assertEquals(existed, retrievedComments.get(0));
    }

    @Test
    void whenGetCommentsCalledAllCommentsShouldBeRetrieved() {
        commentService.reset();

        final var date = Date.from(Instant.now());
        final var insertedComments = List.of(
            new Comment("Zuzka", "pegsolitaire", "Zuzka's comment", date),
            new Comment("Katka", "pegsolitaire", "Katka's comment", date),
            new Comment("Jaro", "pegsolitaire", "Jaro's comment", date)
        );

        insertedComments.forEach(commentService::addComment);

        var retrievedComments = commentService.getComments("pegsolitaire");

        assertEquals(3, retrievedComments.size());
    }

    @Test
    void afterResetIsCalledTopScoresShouldBeEmpty() {
        commentService.reset();
        assertEquals(0, commentService.getComments("pegsolitaire").size());
    }
}