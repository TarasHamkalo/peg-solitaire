package sk.tuke.gamestudio.service;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import sk.tuke.gamestudio.data.entity.Comment;
import sk.tuke.gamestudio.data.service.CommentService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

@EnableAutoConfiguration
@EntityScan("sk.tuke.gamestudio.data.entity")
@FieldDefaults(level = AccessLevel.PRIVATE)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class CommentServiceTest {

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

        final var insertedComments = List.of(
            new Comment("Zuzka", "pegsolitaire", "Zuzka's comment"),
            new Comment("Katka", "pegsolitaire", "Katka's comment"),
            new Comment("Jaro", "pegsolitaire", "Jaro's comment")
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