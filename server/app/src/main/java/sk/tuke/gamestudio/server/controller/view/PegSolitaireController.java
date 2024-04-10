package sk.tuke.gamestudio.server.controller.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PegSolitaireController {

    @GetMapping("/")
    public String start() {
        return "index";
    }
}
