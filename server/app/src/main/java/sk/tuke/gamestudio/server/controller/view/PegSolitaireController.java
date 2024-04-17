package sk.tuke.gamestudio.server.controller.view;

import com.google.gson.JsonObject;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;
import sk.tuke.gamestudio.pegsolitaire.core.events.BoardEvent;
import sk.tuke.gamestudio.pegsolitaire.core.events.BoardEventHandler;
import sk.tuke.gamestudio.pegsolitaire.core.events.BoardEventManager;
import sk.tuke.gamestudio.pegsolitaire.core.events.impl.BombEventHandler;
import sk.tuke.gamestudio.pegsolitaire.core.events.impl.LightningEventHandler;
import sk.tuke.gamestudio.pegsolitaire.core.game.Game;
import sk.tuke.gamestudio.pegsolitaire.core.game.GameUtility;
import sk.tuke.gamestudio.pegsolitaire.core.levels.LevelBuilder;
import sk.tuke.gamestudio.pegsolitaire.core.pegs.PegFactory;
import sk.tuke.gamestudio.server.dto.SetupForm;
import sk.tuke.gamestudio.server.events.EventsDetector;
import sk.tuke.gamestudio.server.exception.PegSolitaireException;

import java.util.*;
import java.util.function.Predicate;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.MediaType.*;

@Controller
@Scope(WebApplicationContext.SCOPE_SESSION)
@RequestMapping("/pegsolitaire")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PegSolitaireController {

    private static final Map<BoardEvent.Type, BoardEventHandler> eventToHandler = Map.ofEntries(
        Map.entry(BoardEvent.Type.BOMB, new BombEventHandler()),
        Map.entry(BoardEvent.Type.LIGHTNING, new LightningEventHandler()),
        Map.entry(BoardEvent.Type.TRIVIAL_MOVE, command -> {}),
        Map.entry(BoardEvent.Type.TRIVIAL_REMOVE, command -> {})
    );

    @Autowired
    Game game;

    @Autowired
    PegFactory pegFactory;

    @Autowired
    BoardEventManager eventManager;

    @Autowired
    List<Class<? extends LevelBuilder>> levelBuilders;

    EventsDetector eventsDetector = new EventsDetector();


    @ResponseStatus(HttpStatus.SEE_OTHER)
    @PostMapping(value = "/new")
    public String createNewGame() {
        game.start();
        return "redirect:game";
    }

    @GetMapping("/game")
    public String game(Model model) {
        if (game.isStarted()) {
            model.addAttribute("boardCells", game.getBoard().getBoardCells());
            for (var row : game.getBoard().getBoardCells()) {
                System.out.println(Arrays.toString(row));
            }

            model.addAttribute("isIndex", false);
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
    @GetMapping(value = "state", produces = APPLICATION_JSON_VALUE)
    public String getGameState() {
        if (game.isStarted()) {
            var jsonObject = new JsonObject();
            jsonObject.addProperty("won", game.getBoard().isSolved());
            jsonObject.addProperty("hasMoves", game.getBoard().hasAvailableMoves());
            jsonObject.addProperty("score", game.getScore());
            jsonObject.addProperty("reload", eventsDetector.isEventPublished());
            //location.reload();
            return jsonObject.toString();
        }

        throw new PegSolitaireException(BAD_REQUEST);
    }


    @GetMapping("/undo")
    public String undoMove() {
        if (game.isStarted() && game.undoMove()) {
            return "redirect:game";
        }

        throw new PegSolitaireException(BAD_REQUEST);
    }


    @PostMapping("/setup")
    public String postForm(@ModelAttribute("setupForm") SetupForm setupForm) {
        var optionalLevel = Optional.ofNullable(
            GameUtility.getInstanceOfLevelBuilder(levelBuilders.get(setupForm.getLevel()), pegFactory)
        );

        optionalLevel.ifPresent(game::setLevelBuilder);


        setEventsToDefault();
        var optionalEvents = Optional.ofNullable(setupForm.getSelectedEvents());

        optionalEvents.ifPresent(e -> e.stream()
            .filter(Predicate.not(Objects::isNull))
            .map(BoardEvent.Type::valueOf)
            .forEach(pegFactory::addIfNotPresent)
        );


        pegFactory.getPegEvents().forEach(
            event -> eventManager.subscribe(event, eventToHandler.get(event))
        );

        Arrays.stream(BoardEvent.Type.values())
            .forEach(e -> eventManager.subscribe(e, eventsDetector));

        System.out.println(pegFactory.getPegEvents());
        return "forward:play";
    }

    private void setEventsToDefault() {
        pegFactory.clearPegEvents();
        List.of(BoardEvent.Type.TRIVIAL_MOVE, BoardEvent.Type.TRIVIAL_REMOVE).forEach(
            pegFactory::addIfNotPresent
        );
    }

}
