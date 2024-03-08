package pegsolitaire.game.core.events.impl;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import pegsolitaire.game.core.commands.BoardCommand;
import pegsolitaire.game.core.commands.impl.RemoveCommand;
import pegsolitaire.game.core.events.BoardEvent;
import pegsolitaire.game.core.events.BoardEventHandler;

import java.security.SecureRandom;
import java.util.Random;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class LightningEventHandler implements BoardEventHandler {
    Random random = new SecureRandom();

    @Override
    public void handle(BoardEvent event) {
        var command = event.getTriggerCommand();
        if (command instanceof RemoveCommand) {
            return;
        }

        destroyNRandomCells(command, 5);

        command.getBoard().removePeg(command.getFinalPosition());
    }

    private void destroyNRandomCells(BoardCommand command, int n) {
        var boardCells = command.getBoard().getBoardCells();
        while (n > 0) {
            int[] destroyPosition = new int[]{
                random.nextInt(boardCells[0].length), random.nextInt(boardCells.length)
            };

            if (command.getBoard().destroyCell(destroyPosition)) {
                n--;
            }
        }
    }

}