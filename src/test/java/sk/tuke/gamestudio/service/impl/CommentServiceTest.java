package sk.tuke.gamestudio.service.impl;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import sk.tuke.gamestudio.entity.Comment;
import sk.tuke.gamestudio.service.CommentService;

import java.time.Instant;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
@FieldDefaults(level = AccessLevel.PRIVATE)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CommentServiceTest {

    @Autowired
    CommentService commentService;

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
    void whenGetCommentsCalledAllCommentsShouldBeRetrieved() {
        commentService.reset();

        final var date = Date.from(Instant.now());
        final var insertedComments = List.of(
            new Comment(1L, "Zuzka", "pegsolitaire", "Zuzka's comment", date),
            new Comment(2L, "Katka", "pegsolitaire", "Katka's comment", date),
            new Comment(3L, "Jaro", "pegsolitaire", "Jaro's comment", date)
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