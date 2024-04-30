package sk.tuke.gamestudio.pegsolitaire.core.events.impl;

import sk.tuke.gamestudio.pegsolitaire.core.Direction;
import sk.tuke.gamestudio.pegsolitaire.core.commands.impl.RemoveCommand;
import sk.tuke.gamestudio.pegsolitaire.core.events.BoardEvent;
import sk.tuke.gamestudio.pegsolitaire.core.events.BoardEventHandler;

public class BombEventHandler implements BoardEventHandler {

  @Override
  public void handle(BoardEvent event) {
    var command = event.getTriggerCommand();
    if (command instanceof RemoveCommand) {
      return;
    }

    int[] currentPosition = command.getFinalPosition();
    for (Direction direction : Direction.values()) {
      int[] destroyPosition = new int[]{
        currentPosition[0] + direction.getX(), currentPosition[1] + direction.getY()
      };

      command.getBoard().destroyCell(destroyPosition);
    }

    command.getBoard().destroyCell(currentPosition);
  }
}