package pegsolitaire.game.ui.impl;

import lombok.AccessLevel;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import pegsolitaire.game.core.board.impl.BoardImpl;
import pegsolitaire.game.core.events.BoardEvent;
import pegsolitaire.game.core.events.BoardEventHandler;
import pegsolitaire.game.core.events.BoardEventManager;
import pegsolitaire.game.core.events.impl.BoardEventManagerImpl;
import pegsolitaire.game.core.events.impl.BombEventHandler;
import pegsolitaire.game.core.events.impl.LightningEventHandler;
import pegsolitaire.game.core.game.Game;
import pegsolitaire.game.core.game.GameUtility;
import pegsolitaire.game.core.game.impl.GameImpl;
import pegsolitaire.game.core.levels.LevelBuilder;
import pegsolitaire.game.core.levels.impl.ClassicLevelBuilder;
import pegsolitaire.game.ui.ConsoleUI;
import pegsolitaire.game.ui.Prompt;

import java.util.*;
import java.util.regex.Pattern;


@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
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

    Scanner scanner;
    ConsoleUI consoleUI;
    BoardEventManager eventManager;
    List<BoardEvent.Type> selectedPegEvents;
    List<Class<? extends LevelBuilder>> levelBuilders;

    @NonFinal
    Game game;
    @NonFinal
    LevelBuilder selectedLevel;
    @NonFinal
    boolean running;

    public PromptImpl(ConsoleUI consoleUI) {
        this.consoleUI = consoleUI;
        this.levelBuilders = GameUtility.getLevelBuilders();
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
            var levelNumber = levelsMatcher.group("args");
            if (levelNumber == null) {
                displayLevels();
            } else {
                selectLevel(Integer.parseInt(levelNumber));
            }

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
        this.selectedPegEvents.clear();
        this.selectedPegEvents.addAll(
            List.of(BoardEvent.Type.TRIVIAL_MOVE, BoardEvent.Type.TRIVIAL_REMOVE)
        );

        for (int i : pegNumbers) {
            if (i < 0 || i > BoardEvent.Type.values().length) {
                continue;
            }

            var event = BoardEvent.Type.values()[i];
            if (!this.selectedPegEvents.contains(event)) {
                this.selectedPegEvents.add(event);
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

    public void selectLevel(int levelNumber) {
        if (levelNumber < 0 || levelNumber >= this.levelBuilders.size()) {
            System.out.print("\nInvalid Input.\nThe ClassicLevelBuilder was chosen.");
            return;
        }

        this.selectedLevel = GameUtility
            .getInstanceOfLevelBuilder(this.levelBuilders.get(levelNumber));

        if (this.selectedLevel == null) {
            this.selectedLevel = new ClassicLevelBuilder();
            System.out.printf(
                "\nWas not able to create instance of %s.\n", levelBuilders.get(levelNumber).getSimpleName()
            );

            System.out.print("The ClassicLevelBuilder was chosen.");
        } else {
            System.out.printf(
                "\nThis is level built by %s\n", levelBuilders.get(levelNumber).getSimpleName()
            );
        }

        consoleUI.printBoard(this.selectedLevel.build());
    }

    public void displayLevels() {
        System.out.println("\nChoose level builder to use:");
        for (int i = 0; i < this.levelBuilders.size(); i++) {
            System.out.printf("%4c%d. %s\n", ' ', i, this.levelBuilders.get(i).getSimpleName());
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
            this.game = GameImpl.builder()
                .boardBuilder(BoardImpl.builder())
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
        clearPrompt();
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