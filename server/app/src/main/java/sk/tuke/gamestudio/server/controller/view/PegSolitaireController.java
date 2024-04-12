package sk.tuke.gamestudio.server.controller.view;

import com.google.gson.JsonObject;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.context.annotation.Scope;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;
import sk.tuke.gamestudio.pegsolitaire.core.board.impl.BoardImpl;
import sk.tuke.gamestudio.pegsolitaire.core.events.impl.BoardEventManagerImpl;
import sk.tuke.gamestudio.pegsolitaire.core.game.Game;
import sk.tuke.gamestudio.pegsolitaire.core.game.impl.GameImpl;
import sk.tuke.gamestudio.pegsolitaire.core.levels.LevelBuilder;
import sk.tuke.gamestudio.pegsolitaire.core.levels.impl.DiamondLevelBuilder;
import sk.tuke.gamestudio.pegsolitaire.core.pegs.PegFactory;
import sk.tuke.gamestudio.pegsolitaire.core.pegs.impl.PegFactoryImpl;
import sk.tuke.gamestudio.server.exception.PegSolitaireException;

import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Controller
@Scope(WebApplicationContext.SCOPE_SESSION)
@RequestMapping("/pegsolitaire")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PegSolitaireController {

    Game game;

    PegFactory pegFactory = new PegFactoryImpl();

    LevelBuilder levelBuilder = new DiamondLevelBuilder(pegFactory);

    public PegSolitaireController() {
        this.game = GameImpl.builder()
            .eventManager(new BoardEventManagerImpl())
            .boardBuilder(BoardImpl.builder())
            .levelBuilder(levelBuilder)
            .build();
    }

    @GetMapping("/new")
    public String play(Model model) {
        game.stop();
        game.start();
        model.addAttribute("boardCells",  game.getBoard().getBoardCells());
        return "pegsolitaire";
    }

    @ResponseBody
    @GetMapping("/api/game/moves")
    public List<int[]> getPossibleMoves() {
        if (game.isStarted()) {
            return game.getPossibleMoves();
        } else {
            throw new PegSolitaireException(BAD_REQUEST, "Game was not started");
        }
    }

    @ResponseBody
    @PostMapping("/api/game/select")
    public void selectPeg(@RequestParam("x") int x, @RequestParam("y") int y) {
        if (!game.isStarted() || !game.selectPeg(x, y)) {
            throw new PegSolitaireException(BAD_REQUEST);
        }
    }

    @ResponseBody
    @PostMapping("/api/game/move")
    public void makeMove(@RequestParam("x") int x, @RequestParam("y") int y) {
        if (!game.isStarted() || !game.makeMove(x, y)) {
            throw new PegSolitaireException(BAD_REQUEST);
        }
    }

    @ResponseBody
    @GetMapping("api/game/state")
    public ResponseEntity<String> getGameState() {
        if (game.isStarted()) {
            var jsonObject = new JsonObject();
            jsonObject.addProperty("won", game.getBoard().isSolved());
            jsonObject.addProperty("hasMoves", game.getBoard().hasAvailableMoves());
            jsonObject.addProperty("score", game.getScore());

            return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(jsonObject.toString());
        }

        throw new PegSolitaireException(BAD_REQUEST);
    }



}
