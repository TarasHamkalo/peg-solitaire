package sk.tuke.gamestudio.server.controller.view;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import sk.tuke.gamestudio.server.controller.rest.data.ScoreController;

@Controller
public class RootController {

    ScoreController scoreController;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("scores", scoreController.getTopScores("pegsolitaire"));
        return "index";
    }

}
