package pegsolitaire.game.core.events.impl;

import pegsolitaire.game.core.Direction;
import pegsolitaire.game.core.commands.impl.RemoveCommand;
import pegsolitaire.game.core.events.BoardEvent;
import pegsolitaire.game.core.events.BoardEventHandler;

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