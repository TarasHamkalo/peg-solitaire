package sk.tuke.gamestudio.server.controller.advice;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import sk.tuke.gamestudio.pegsolitaire.core.events.BoardEvent;
import sk.tuke.gamestudio.pegsolitaire.core.levels.LevelBuilder;
import sk.tuke.gamestudio.server.dto.SetupForm;

import java.util.List;

@ControllerAdvice
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GameSessionAdvice {

    SetupForm setupForm;

    List<BoardEvent.Type> events;

    List<Class<? extends LevelBuilder>> levelBuilders;

    @ModelAttribute("setupForm")
    public SetupForm setupForm() {
        return setupForm;
    }

    @ModelAttribute("levels")
    public List<Class<? extends LevelBuilder>> levels() {
        return levelBuilders;
    }

    @ModelAttribute("events")
    public List<BoardEvent.Type> getEvents() {
        return events;
    }

}
