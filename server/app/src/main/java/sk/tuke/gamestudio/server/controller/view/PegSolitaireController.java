package sk.tuke.gamestudio.server.controller.view;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import sk.tuke.gamestudio.pegsolitaire.core.levels.LevelBuilder;
import sk.tuke.gamestudio.pegsolitaire.core.levels.impl.DiamondLevelBuilder;
import sk.tuke.gamestudio.pegsolitaire.core.pegs.PegFactory;
import sk.tuke.gamestudio.pegsolitaire.core.pegs.impl.PegFactoryImpl;

@Controller
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PegSolitaireController {

    PegFactory pegFactory = new PegFactoryImpl();

    LevelBuilder diamondLevelBuilder = new DiamondLevelBuilder(pegFactory);

    @GetMapping("/")
    public String start(Model model) {
        model.addAttribute("boardCells", diamondLevelBuilder.build());
        return "index";
    }
}
