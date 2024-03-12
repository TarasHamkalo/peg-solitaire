package sk.tuke.gamestudio.pegsolitaire.game.ui.impl;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import sk.tuke.gamestudio.entity.Comment;
import sk.tuke.gamestudio.exception.CommentException;
import sk.tuke.gamestudio.pegsolitaire.game.ui.InputHandler;
import sk.tuke.gamestudio.service.CommentService;

import java.util.regex.Pattern;

@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CommentCommandHandler extends InputHandler {

    private static final Pattern COMMENT_CMD = Pattern.compile(
        "^comment\\s(?<cmd>add|list|reset)\\s?(?<text>(?:[\\w.,!\n]\\s?){1,150})?$"
    );

    CommentService commentService;

    @Override
    public boolean handle(String line) {
        var matcher  = COMMENT_CMD.matcher(line);
        if (matcher.matches()) {
            try {
                switch (matcher.group("cmd")) {
                    case "add" -> addPegsolitaireComment(matcher.group("text"));
                    case "list" -> listPegsolitaireComments();
                    case "reset" -> commentService.reset();
                }

            } catch (CommentException e) {
                System.out.println(e.getMessage());
            }

            return true;
        }

        return handleNext(line);
    }

    private void addPegsolitaireComment(String text) {
        if (text == null) {
            System.out.println("You forgot to provide a text...");
            return;
        }

        commentService.addComment(
            Comment.builder()
                .game("pegsolitaire")
                .player(System.getProperty("user.name"))
                .text(text)
                .build()
        );

        System.out.println("Comment added");
    }

    private void listPegsolitaireComments() {
        commentService.getComments("pegsoliatire").forEach(System.out::println);
    }
}
