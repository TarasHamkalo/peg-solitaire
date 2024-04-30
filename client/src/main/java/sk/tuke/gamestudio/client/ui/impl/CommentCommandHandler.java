package sk.tuke.gamestudio.client.ui.impl;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sk.tuke.gamestudio.client.service.CommentService;
import sk.tuke.gamestudio.client.service.exception.ServiceException;
import sk.tuke.gamestudio.client.ui.InputHandler;
import sk.tuke.gamestudio.server.api.rest.dto.CommentDto;

import java.util.regex.Pattern;

@Component
@EqualsAndHashCode(callSuper = false)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CommentCommandHandler extends InputHandler {

  private static final Pattern COMMENT_CMD = Pattern.compile(
    "^comment\\s(?<cmd>add|list)\\s?(?<text>(?:[\\w.,!\n]\\s?){1,150})?$"
  );

  @NonNull
  CommentService commentService;

  @Autowired
  public CommentCommandHandler(@NonNull CommentService commentService) {
    this.commentService = commentService;
  }

  @Override
  public boolean handle(String line) {
    var matcher = COMMENT_CMD.matcher(line);
    if (matcher.matches()) {
      try {
        switch (matcher.group("cmd")) {
          case "add" -> addPegsolitaireComment(matcher.group("text"));
          case "list" -> listPegsolitaireComments();
//                    case "reset" -> commentService.reset();
        }

      } catch (ServiceException e) {
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
      CommentDto.builder()
        .game("pegsolitaire")
        .player(System.getProperty("user.name"))
        .text(text)
        .build()
    );

    System.out.println("Comment added");
  }

  private void listPegsolitaireComments() {
    commentService.getComments("pegsolitaire").forEach(System.out::println);
  }
}
