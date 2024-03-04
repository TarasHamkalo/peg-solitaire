package pegsolitaire.game.core.events.impl;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import pegsolitaire.game.core.commands.BoardCommand;
import pegsolitaire.game.core.events.BoardEvent;

@Data
@Builder
@AllArgsConstructor
public class BoardEventImpl {
    BoardEvent.Type type;
    BoardCommand trigger;
}
