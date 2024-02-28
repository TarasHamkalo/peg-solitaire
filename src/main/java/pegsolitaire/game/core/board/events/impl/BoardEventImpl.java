package pegsolitaire.game.core.board.events.impl;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import pegsolitaire.game.core.board.commands.BoardCommand;
import pegsolitaire.game.core.board.events.BoardEvent;

@Data
@Builder
@AllArgsConstructor
public class BoardEventImpl {
    BoardEvent.Type type;
    BoardCommand trigger;
}
