package sk.tuke.gamestudio.pegsolitaire.core.events.impl;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import sk.tuke.gamestudio.pegsolitaire.core.commands.BoardCommand;
import sk.tuke.gamestudio.pegsolitaire.core.events.BoardEvent;

@Data
@Builder
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BoardEventImpl implements BoardEvent {

  BoardEvent.Type eventType;

  BoardCommand triggerCommand;
}
