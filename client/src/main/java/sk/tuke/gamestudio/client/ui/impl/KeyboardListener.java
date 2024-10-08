package sk.tuke.gamestudio.client.ui.impl;

import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import sk.tuke.gamestudio.client.ui.BoardUI;
import sk.tuke.gamestudio.pegsolitaire.core.Direction;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class KeyboardListener implements NativeKeyListener {

  @NonNull
  BoardUI console;

  public KeyboardListener(@NonNull BoardUI console) {
    this.console = console;
  }

  @Override
  public void nativeKeyPressed(NativeKeyEvent nativeEvent) {
    switch (nativeEvent.getKeyCode()) {
      case NativeKeyEvent.VC_SPACE: {
        console.select();
        break;
      }

      case NativeKeyEvent.VC_UP: {
        console.adjustCursor(Direction.NORTH.getX(), Direction.NORTH.getY());
        break;
      }

      case NativeKeyEvent.VC_DOWN: {
        console.adjustCursor(Direction.SOUTH.getX(), Direction.SOUTH.getY());
        break;
      }

      case NativeKeyEvent.VC_LEFT: {
        console.adjustCursor(Direction.WEST.getX(), Direction.WEST.getY());
        break;
      }

      case NativeKeyEvent.VC_RIGHT: {
        console.adjustCursor(Direction.EAST.getX(), Direction.EAST.getY());
        break;
      }

      default: {
        break;
      }
    }
  }
}