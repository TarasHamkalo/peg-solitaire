package pegsolitaire.game.ui;

import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import pegsolitaire.game.core.board.Direction;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class KeyboardListener implements NativeKeyListener {
    @NonNull ConsoleUI console;

    public KeyboardListener(ConsoleUI console) {
        this.console = console;
    }

    @Override
    public void nativeKeyPressed(NativeKeyEvent nativeEvent) {
        switch (nativeEvent.getKeyCode()) {
            case NativeKeyEvent.VC_SPACE: {
                console.select();
//                System.out.println("listener");
                return;
            }

            case NativeKeyEvent.VC_UP: {
//                System.out.println("listener");
                console.adjustCursor(Direction.SOUTH.getX(), Direction.SOUTH.getY());
                return;
            }

            case NativeKeyEvent.VC_DOWN: {
//                System.out.println("listener");
                console.adjustCursor(Direction.NORTH.getX(), Direction.NORTH.getY());
                return;
            }

            case NativeKeyEvent.VC_LEFT: {
//                System.out.println("listener");
                console.adjustCursor(Direction.WEST.getX(), Direction.WEST.getY());
                return;
            }

            case NativeKeyEvent.VC_RIGHT: {
//                System.out.println("listener");
                console.adjustCursor(Direction.EAST.getX(), Direction.EAST.getY());
            }
        }
    }

}