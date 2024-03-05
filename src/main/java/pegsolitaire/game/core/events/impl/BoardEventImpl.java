package pegsolitaire.game.core.events.impl;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import pegsolitaire.game.core.commands.BoardCommand;
import pegsolitaire.game.core.events.BoardEvent;

@Data
@Builder
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BoardEventImpl {
    BoardEvent.Type type;
    BoardCommand trigger;
}
