package ui;

import com.github.kwhat.jnativehook.NativeHookException;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;



@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class KeyboardListener implements NativeKeyListener {
    @NonNull ConsoleUI console;

    public KeyboardListener(ConsoleUI console) throws NativeHookException {
        this.console = console;
    }

    @Override
    public void nativeKeyPressed(NativeKeyEvent nativeEvent) {
        int keyCode = nativeEvent.getKeyCode();
        switch (keyCode) {
            case NativeKeyEvent.VC_SPACE:
                console.select();
            case NativeKeyEvent.VC_UP:
                console.moveUp();
                break;
            case NativeKeyEvent.VC_DOWN:
                console.moveDown();
                break;
            case NativeKeyEvent.VC_LEFT:
                console.moveLeft();
                break;
            case NativeKeyEvent.VC_RIGHT:
                console.moveRight();
                break;
            default:
                return;
        }
    }

}