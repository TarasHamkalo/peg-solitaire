package sk.tuke.gamestudio.server.pegsolitaire.game.core;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum Color {
    // Foreground Colors
    RESET("\u001B[0m"),
    BLACK("\u001B[30m"),
    RED("\u001B[31m"),
    GREEN("\u001B[32m"),
    YELLOW("\u001B[33m"),
    BLUE("\u001B[34m"),
    MAGENTA("\u001B[35m"),
    CYAN("\u001B[36m"),
    WHITE("\u001B[37m"),
    BRIGHT_BLACK("\u001B[90m"),
    BRIGHT_RED("\u001B[91m"),
    BRIGHT_GREEN("\u001B[92m"),
    BRIGHT_YELLOW("\u001B[93m"),
    BRIGHT_BLUE("\u001B[94m"),
    BRIGHT_MAGENTA("\u001B[95m"),
    BRIGHT_CYAN("\u001B[96m"),
    BRIGHT_WHITE("\u001B[97m"),

    // Background Colors
    BLACK_BG("\u001B[40m"),
    RED_BG("\u001B[41m"),
    GREEN_BG("\u001B[42m"),
    YELLOW_BG("\u001B[43m"),
    BLUE_BG("\u001B[44m"),
    MAGENTA_BG("\u001B[45m"),
    CYAN_BG("\u001B[46m"),
    WHITE_BG("\u001B[47m"),
    BRIGHT_BLACK_BG("\u001B[100m"),
    BRIGHT_RED_BG("\u001B[101m"),
    BRIGHT_GREEN_BG("\u001B[102m"),
    BRIGHT_YELLOW_BG("\u001B[103m"),
    BRIGHT_BLUE_BG("\u001B[104m"),
    BRIGHT_MAGENTA_BG("\u001B[105m"),
    BRIGHT_CYAN_BG("\u001B[106m"),
    BRIGHT_WHITE_BG("\u001B[107m");

    String code;

    @Override
    public String toString() {
        return code;
    }
}
