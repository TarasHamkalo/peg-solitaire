package sk.tuke.gamestudio.server.controller.view;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import sk.tuke.gamestudio.pegsolitaire.core.board.impl.BoardImpl;
import sk.tuke.gamestudio.pegsolitaire.core.events.impl.BoardEventManagerImpl;
import sk.tuke.gamestudio.pegsolitaire.core.game.Game;
import sk.tuke.gamestudio.pegsolitaire.core.game.impl.GameImpl;
import sk.tuke.gamestudio.pegsolitaire.core.levels.LevelBuilder;
import sk.tuke.gamestudio.pegsolitaire.core.levels.impl.DiamondLevelBuilder;
import sk.tuke.gamestudio.pegsolitaire.core.pegs.PegFactory;
import sk.tuke.gamestudio.pegsolitaire.core.pegs.impl.PegFactoryImpl;

import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/pegsolitaire")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PegSolitaireController {

    Game game;

    PegFactory pegFactory = new PegFactoryImpl();

    LevelBuilder levelBuilder = new DiamondLevelBuilder(pegFactory);

    @GetMapping("/new")
    public String play(Model model) {
        this.game = GameImpl.builder()
            .eventManager(new BoardEventManagerImpl())
            .boardBuilder(BoardImpl.builder())
            .levelBuilder(levelBuilder)
            .build();

        game.start();
        model.addAttribute("boardCells",  game.getBoard().getBoardCells());
        return "pegsolitaire";
    }

    @ResponseBody
    @GetMapping("/api/game/moves")
    public List<int[]> getPossibleMoves(@RequestParam("x") int x, @RequestParam("y") int y) {
        if (game != null && game.isStarted()) {
            return game.getBoard().getPossibleMoves(x, y);
        }

        return Collections.emptyList();
    }


}
