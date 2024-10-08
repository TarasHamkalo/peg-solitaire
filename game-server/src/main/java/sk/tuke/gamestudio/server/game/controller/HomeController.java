package sk.tuke.gamestudio.server.game.controller;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.context.annotation.SessionScope;

@Controller
@SessionScope
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class HomeController {

  @GetMapping("/")
  public String index() {
    return "index";
  }

  @GetMapping("/social")
  public String social() {
    return "social";
  }
}
