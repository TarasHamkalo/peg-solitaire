package sk.tuke.gamestudio.server.controller.view;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;
import sk.tuke.gamestudio.pegsolitaire.core.game.GameUtility;
import sk.tuke.gamestudio.pegsolitaire.core.levels.LevelBuilder;
import sk.tuke.gamestudio.pegsolitaire.core.levels.impl.SmallFrogLevelBuilder;
import sk.tuke.gamestudio.pegsolitaire.core.pegs.impl.PegFactoryImpl;
import sk.tuke.gamestudio.server.controller.rest.data.ScoreController;

import java.awt.*;
import java.nio.charset.Charset;
import java.util.Arrays;

@Controller
@Scope(WebApplicationContext.SCOPE_SESSION)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RootController {

    ScoreController scoreController;

    @Autowired
    public RootController(ScoreController scoreController) {
        this.scoreController = scoreController;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("scores", scoreController.getTopScores("pegsolitaire"));
//        model.addAttribute("levels", GameUtility.getLevelBuilders());
        return "index";
    }

}
