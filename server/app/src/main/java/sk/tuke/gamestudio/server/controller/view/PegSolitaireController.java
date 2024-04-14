package sk.tuke.gamestudio.server.controller.view;

import com.google.gson.JsonObject;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;
import sk.tuke.gamestudio.pegsolitaire.core.game.Game;
import sk.tuke.gamestudio.pegsolitaire.core.levels.LevelBuilder;
import sk.tuke.gamestudio.pegsolitaire.core.pegs.PegFactory;
import sk.tuke.gamestudio.server.exception.PegSolitaireException;

import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Controller
@Scope(WebApplicationContext.SCOPE_SESSION)
@RequestMapping("/pegsolitaire")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PegSolitaireController {

    @Autowired
    Game game;

    @Autowired
    PegFactory pegFactory;

    @Autowired
    LevelBuilder levelBuilder;

    @ResponseStatus(HttpStatus.SEE_OTHER)
    @PostMapping(value = "/new")
    public String createNewGame() {
        game.stop();
        game.start();
        return "redirect:game";
    }

    @GetMapping("/game")
    public String game(Model model) {
        if (game.isStarted()) {
            model.addAttribute("boardCells", game.getBoard().getBoardCells());
            return "pegsolitaire";
        } else {
            throw new PegSolitaireException(BAD_REQUEST, "Game was not started");
        }
    }

    @ResponseStatus(HttpStatus.SEE_OTHER)
    @PostMapping("/play")
    public String play() {
        if (game.isStarted()) {
            return "redirect:game";
        } else {
            return "forward:new";
        }
    }

    @ResponseBody
    @GetMapping("moves")
    public List<int[]> getPossibleMoves() {
        if (game.isStarted()) {
            return game.getPossibleMoves();
        } else {
            throw new PegSolitaireException(BAD_REQUEST, "Game was not started");
        }
    }

    @ResponseBody
    @PostMapping("select")
    public void selectPeg(@RequestParam("x") int x, @RequestParam("y") int y) {
        if (!game.isStarted() || !game.selectPeg(x, y)) {
            throw new PegSolitaireException(BAD_REQUEST);
        }
    }

    @ResponseBody
    @PostMapping("move")
    public void makeMove(@RequestParam("x") int x, @RequestParam("y") int y) {
        if (!game.isStarted() || !game.makeMove(x, y)) {
            throw new PegSolitaireException(BAD_REQUEST);
        }
    }

    @ResponseBody
    @GetMapping(value = "state", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getGameState() {
        if (game.isStarted()) {
            var jsonObject = new JsonObject();
            jsonObject.addProperty("won", game.getBoard().isSolved());
            jsonObject.addProperty("hasMoves", game.getBoard().hasAvailableMoves());
            jsonObject.addProperty("score", game.getScore());

            return jsonObject.toString();
        }

        throw new PegSolitaireException(BAD_REQUEST);
    }


}
