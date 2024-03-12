package sk.tuke.gamestudio.pegsolitaire.game.ui;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public abstract class InputHandler {
    InputHandler next;

    public static InputHandler link(@NonNull InputHandler first, InputHandler... chain) {
        InputHandler head = first;
        for (InputHandler nextInChain : chain) {
            head.next = nextInChain;
            head = nextInChain;
        }

        return first;
    }

    public boolean handleNext(String line) {
        if (next == null) {
            return false;
        }

        return next.handle(line);
    }

    public abstract boolean handle(String line);
}
