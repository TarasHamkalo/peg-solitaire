package pegsolitaire.game.ui.impl;

import lombok.AccessLevel;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import pegsolitaire.game.core.Game;
import pegsolitaire.game.core.board.events.BoardEvent;
import pegsolitaire.game.core.board.events.BoardEventHandler;
import pegsolitaire.game.core.board.events.BoardEventManager;
import pegsolitaire.game.core.board.events.impl.BoardEventManagerImpl;
import pegsolitaire.game.core.board.events.impl.BombEventHandler;
import pegsolitaire.game.core.board.events.impl.LightningEventHandler;
import pegsolitaire.game.core.levels.LevelBuilder;
import pegsolitaire.game.core.levels.impl.ClassicLevelBuilder;
import pegsolitaire.game.ui.ConsoleUI;
import pegsolitaire.game.ui.Prompt;

import java.util.*;
import java.util.regex.Pattern;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PromptImpl implements Prompt {
    private static final Map<BoardEvent.Type, BoardEventHandler> eventToHandler = Map.ofEntries(
        Map.entry(BoardEvent.Type.BOMB, new BombEventHandler()),
        Map.entry(BoardEvent.Type.LIGHTNING, new LightningEventHandler()),
        Map.entry(BoardEvent.Type.TRIVIAL_MOVE, (command) -> {}),
        Map.entry(BoardEvent.Type.TRIVIAL_REMOVE, (command) -> {})
    );

    private static final Pattern BASE_CMDS = Pattern.compile("help|start|stop|undo|exit");
    private static final Pattern LEVELS_CDM = Pattern.compile("levels(\s(?<args>[0-9]))?");
    private static final Pattern PEGS_CDM = Pattern.compile("pegs(\s(?<args>([0-9],?)+))?");

    Game game;
    Scanner scanner;
    ConsoleUI consoleUI;
    LevelBuilder selectedLevel;
    BoardEventManager eventManager;
    List<BoardEvent.Type> selectedPegEvents;
    List<Class<? extends LevelBuilder>> levelBuilders;
    boolean running;

    public PromptImpl(ConsoleUI consoleUI) {
        this.consoleUI = consoleUI;
        this.levelBuilders = Game.getLevelBuilders();
        this.selectedPegEvents = new ArrayList<>(
            List.of(BoardEvent.Type.TRIVIAL_MOVE, BoardEvent.Type.TRIVIAL_REMOVE)
        );

        this.scanner = new Scanner(System.in);
        this.selectedLevel = new ClassicLevelBuilder();
        this.eventManager = new BoardEventManagerImpl();
        this.running = true;
    }

    @Override
    public void begin() {
        consoleUI.clearScreen();
        while (running) {
            consoleUI.drawPrompt();
            parseInput();
        }
    }

    @Override
    @SneakyThrows
    public void parseInput() {
        var line = scanner.nextLine().trim().toLowerCase(Locale.ENGLISH);
        clearPrompt();
        var baseMatcher = BASE_CMDS.matcher(line);
        if (baseMatcher.matches()) {
            this.getClass().getMethod(line).invoke(this);
            return;
        }

        var levelsMatcher = LEVELS_CDM.matcher(line);
        if (levelsMatcher.matches()) {
            var level = levelsMatcher.group("args");
            levelsCmd(level != null ? Integer.parseInt(level) : -1);
            return;
        }

        var pegsMatcher = PEGS_CDM.matcher(line);
        if (pegsMatcher.matches()) {
            var args = pegsMatcher.group("args");
            if (args != null) {
                var pegNumbers = Arrays.stream(args.split(",")).mapToInt(Integer::parseInt).toArray();

                selectPegEvents(pegNumbers);
            } else {
                displayPegEvents();
            }

        } else {
            System.out.printf("\nInvalid input %s.\n", line);
        }
    }

    private void selectPegEvents(int[] pegNumbers) {
        this.selectedPegEvents = new ArrayList<>(
            List.of(BoardEvent.Type.TRIVIAL_MOVE, BoardEvent.Type.TRIVIAL_REMOVE)
        );

        for (int i : pegNumbers) {
            if (i < BoardEvent.Type.values().length) {
                this.selectedPegEvents.add(BoardEvent.Type.values()[i]);
            }
        }
    }

    public void displayPegEvents() {
        var events = BoardEvent.Type.values();
        System.out.println("\nIn use:");
        for (int i = 0; i < selectedPegEvents.size(); i++) {
            System.out.printf("%4d. %s\n", i, selectedPegEvents.get(i).toString());
        }

        System.out.println("Can be used:");
        for (int i = 0; i < events.length; i++) {
            System.out.printf("%4d. %s\n", i, events[i].toString());
        }
    }

    public void levelsCmd(int level) {
        if (level == -1) {
            System.out.println("\nChoose level builder to use:");
            for (int i = 0; i < this.levelBuilders.size(); i++) {
                System.out.printf("%4c%d. %s\n", ' ', i, this.levelBuilders.get(i).getSimpleName());
            }

        } else {
            try {
                this.selectedLevel =
                    levelBuilders.get(level).getDeclaredConstructor().newInstance();
            } catch (Exception ignore) {
                return;
            }

            System.out.printf(
                "\nThis is level built by %s\n", levelBuilders.get(0).getSimpleName()
            );
            consoleUI.printBoard(this.selectedLevel.build());
        }

    }

    public void start() {
        if (this.game != null && this.game.isStarted()) {
            return;
        }

        eventManager.clearAll();
        selectedPegEvents.forEach(
            event -> eventManager.subscribe(event, eventToHandler.get(event))
        );

        if (this.game == null) {
            this.game = Game.builder()
                .eventManager(this.eventManager)
                .levelBuilder(this.selectedLevel)
                .build();
        } else {
            this.game.setLevelBuilder(this.selectedLevel);
        }

        this.consoleUI.start(game);
    }

    public void exit() {
        this.running = false;
        this.consoleUI.exit();
    }

    public void stop() {
        this.consoleUI.stop();
    }

    public void help() {
        System.out.println("""
        \nUsage info:
            help: shows this information
            start: starts the game
            stop: stops the current game;
            undo: undo previous move
            exit: exits the program
            pegs {1,2,3...}:
                without parameters: displays peg types
                with parameters: selects types
            levels {n}:
                without parameter: displays levels
                with parameter: selects types
        """);
    }

    public void clearPrompt() {
        consoleUI.positionPrompt();
        System.out.print("\033[0J");
    }

    public void undo() {
        this.consoleUI.undo();
    }
}
