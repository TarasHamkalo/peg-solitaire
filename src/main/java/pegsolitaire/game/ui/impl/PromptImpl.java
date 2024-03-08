package pegsolitaire.game.ui.impl;

import lombok.AccessLevel;
import lombok.NonNull;
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
import pegsolitaire.game.core.pegs.PegFactory;
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

    @NonNull
    ConsoleUI consoleUI;

    BoardEventManager eventManager;

    @NonNull
    PegFactory pegFactory;

    List<Class<? extends LevelBuilder>> levelBuilders;

    @NonFinal
    Game game;

    @NonFinal
    LevelBuilder selectedLevel;

    @NonFinal
    boolean running;

    public PromptImpl(@NonNull ConsoleUI consoleUI, @NonNull PegFactory pegFactory) {
        this.running = true;
        this.consoleUI = consoleUI;
        this.scanner = new Scanner(System.in);
        this.levelBuilders = GameUtility.getLevelBuilders();
        this.eventManager = new BoardEventManagerImpl();
        this.selectedLevel = new ClassicLevelBuilder(pegFactory);
        this.pegFactory = pegFactory;
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
        if (!(execBasicCommand(line) || execLevelsCommand(line) || execPegsCommand(line))) {
            System.out.printf("\nInvalid input %s.\n", line);
        }
    }

    private boolean execBasicCommand(String line) throws ReflectiveOperationException {
        var baseMatcher = BASE_CMDS.matcher(line);
        if (baseMatcher.matches()) {
            this.getClass().getMethod(line).invoke(this);
            return true;
        }

        return false;
    }

    private boolean execLevelsCommand(String line) {
        var levelsMatcher = LEVELS_CDM.matcher(line);
        if (levelsMatcher.matches()) {
            var levelNumber = levelsMatcher.group("args");
            if (levelNumber == null) {
                displayLevels();
            } else {
                selectLevel(Integer.parseInt(levelNumber));
            }

            return true;
        }
        return false;
    }

    private boolean execPegsCommand(String line) {
        var pegsMatcher = PEGS_CDM.matcher(line);
        if (pegsMatcher.matches()) {
            var args = pegsMatcher.group("args");
            if (args != null) {
                var pegNumbers = Arrays.stream(args.split(","))
                    .mapToInt(Integer::parseInt)
                    .toArray();

                selectPegEvents(pegNumbers);
            } else {
                displayPegEvents();
            }

            return true;
        }

        return false;
    }

    private void selectPegEvents(int[] pegNumbers) {
        pegFactory.clearPegEvents();
        for (int i : pegNumbers) {
            if (i >= 0 && i < BoardEvent.Type.values().length) {
                pegFactory.addIfNotPresent(BoardEvent.Type.values()[i]);
            }
        }
    }

    public void displayPegEvents() {
        System.out.println("\nIn use:");
        for (int i = 0; i < pegFactory.getPegEvents().size(); i++) {
            System.out.printf("%4d. %s\n", i, pegFactory.getPegEvents().get(i).toString());
        }

        System.out.println("Can be used:");

        var events = BoardEvent.Type.values();
        for (int i = 0; i < events.length; i++) {
            if (!(BoardEvent.Type.TRIVIAL_MOVE.equals(events[i]) ||
                BoardEvent.Type.TRIVIAL_REMOVE.equals(events[i]))) {

                System.out.printf("%4d. %s\n", i, events[i].toString());
            }
        }
    }

    public void selectLevel(int levelNumber) {
        if (levelNumber < 0 || levelNumber >= levelBuilders.size()) {
            System.out.print("\nInvalid Input.\nThe ClassicLevelBuilder was chosen.");
            return;
        }

        this.selectedLevel = GameUtility
            .getInstanceOfLevelBuilder(levelBuilders.get(levelNumber), pegFactory);

        if (this.selectedLevel == null) {
            this.selectedLevel = new ClassicLevelBuilder(pegFactory);
            System.out.printf("\nWas not able to create instance of %s.\n",
                levelBuilders.get(levelNumber).getSimpleName()
            );

            System.out.print("The ClassicLevelBuilder was chosen.");
        } else {
            System.out.printf(
                "\nThis is level built by %s\n", levelBuilders.get(levelNumber).getSimpleName()
            );
        }

        consoleUI.printBoard(selectedLevel.build());
    }

    public void displayLevels() {
        System.out.println("\nChoose level builder to use:");
        for (int i = 0; i < levelBuilders.size(); i++) {
            System.out.printf("%4c%d. %s\n", ' ', i, levelBuilders.get(i).getSimpleName());
        }

    }

    public void start() {
        if (this.game != null && this.game.isStarted()) {
            return;
        }

        eventManager.clearAll();
        pegFactory.getPegEvents().forEach(
            event -> eventManager.subscribe(event, eventToHandler.get(event))
        );

        if (this.game == null) {
            this.game = GameImpl.builder()
                .boardBuilder(BoardImpl.builder())
                .eventManager(eventManager)
                .levelBuilder(selectedLevel)
                .build();

        } else {
            this.game.setLevelBuilder(selectedLevel);
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