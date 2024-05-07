package sk.tuke.gamestudio.server.game.controller;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.SessionScope;
import sk.tuke.gamestudio.pegsolitaire.core.events.BoardEvent;
import sk.tuke.gamestudio.pegsolitaire.core.game.Game;
import sk.tuke.gamestudio.pegsolitaire.core.game.GameUtility;
import sk.tuke.gamestudio.pegsolitaire.core.levels.LevelBuilder;
import sk.tuke.gamestudio.pegsolitaire.core.pegs.PegFactory;
import sk.tuke.gamestudio.server.game.dto.GameSessionState;
import sk.tuke.gamestudio.server.game.dto.SetupForm;
import sk.tuke.gamestudio.server.game.events.EventsDetector;
import sk.tuke.gamestudio.server.game.exception.PegSolitaireException;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Controller
@SessionScope
@AllArgsConstructor
@RequestMapping("/pegsolitaire")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PegSolitaireController {

  Game game;

  PegFactory pegFactory;

  EventsDetector eventsDetector;

  List<Class<? extends LevelBuilder>> levelBuilders;

  @PostMapping(value = "/new")
  @ResponseStatus(HttpStatus.SEE_OTHER)
  public String createNewGame() {
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

  @PostMapping("/play")
  @ResponseStatus(HttpStatus.SEE_OTHER)
  public String play() {
    if (game.isStarted()) {
      return "redirect:game";
    } else {
      return "forward:new";
    }
  }

  @GetMapping("/undo")
  public String undoMove() {
    if (game.isStarted() && game.undoMove()) {
      return "redirect:game";
    }

    throw new PegSolitaireException(BAD_REQUEST);
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
  public GameSessionState getGameState() {
    if (game.isStarted()) {
      return new GameSessionState(
        game.getScore(),
        game.getBoard().isSolved(),
        game.getBoard().hasAvailableMoves(),
        eventsDetector.isEventPublished()
      );
    }

    throw new PegSolitaireException(BAD_REQUEST);
  }

  @PostMapping("/setup")
  public String gameSetup(@ModelAttribute("setupForm") SetupForm setupForm) {
    var optionalLevel = Optional.ofNullable(
      GameUtility.getInstanceOfLevelBuilder(
        levelBuilders.get(setupForm.getLevel()), pegFactory
      )
    );

    optionalLevel.ifPresent(game::setLevelBuilder);

    var optionalEvents = Optional.ofNullable(setupForm.getSelectedEvents());

    pegFactory.clearPegEvents();
    optionalEvents.ifPresent(e -> e.stream()
      .filter(Predicate.not(Objects::isNull))
      .map(BoardEvent.Type::valueOf)
      .forEach(pegFactory::addIfNotPresent)
    );

    System.out.println(pegFactory.getSelectedEvents());
    game.stop();
    return "forward:play";
  }

}