package roomba.view;

import processing.core.PApplet;
import processing.core.PImage;
import roomba.controller.CollisionHandler;
import roomba.controller.ImageLoader;
import roomba.controller.LevelManager;
import roomba.model.AnimatedSprite;
import roomba.model.Player;
import roomba.model.Sprite;
import roomba.script.addRFIDCard;

import java.nio.file.Path;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import static roomba.model.Constants.DOWN_FACING;
import static roomba.model.Constants.FULLSCREEN;
import static roomba.model.Constants.HEIGHT;
import static roomba.model.Constants.LEFT_FACING;
import static roomba.model.Constants.RFID_DOWN;
import static roomba.model.Constants.RFID_EASY;
import static roomba.model.Constants.RFID_HARD;
import static roomba.model.Constants.RFID_LEFT;
import static roomba.model.Constants.RFID_MEDIUM;
import static roomba.model.Constants.RFID_RESET;
import static roomba.model.Constants.RFID_RIGHT;
import static roomba.model.Constants.RFID_TURN;
import static roomba.model.Constants.RFID_UP;
import static roomba.model.Constants.RIGHT_FACING;
import static roomba.model.Constants.SPRITE_SIZE;
import static roomba.model.Constants.UP_FACING;
import static roomba.model.Constants.WIDTH;


/**
 * Represents the game field where the Roomba moves and interacts with obstacles
 * and goals.
 */
public class GameField extends PApplet {
    private static final int BACKGROUND_SCALE = 2 * (int) SPRITE_SIZE;
    private static final Logger LOGGER = Logger.getLogger(GameField.class.getName());
    private static final float HEADER_SIZE = BACKGROUND_SCALE;
    private final PhysicalScanner pui;
    private final PhysicalLed puiLed;
    private final LevelManager levelManager;
    private final CollisionHandler collisionHandler;
    public boolean nextLevel = false;
    public List<Sprite> obstacles;
    public List<Sprite> goal;
    public Map<String, PImage> imageMap = new HashMap<>();
    public List<PImage> pImageListObstacles = new ArrayList<>();
    public Map<String, List<PImage>> pImageMultiImageObstacles = new HashMap<>();
    public Player player;
    private int difficulty = 0;
    private PImage backgroundImage;
    private boolean isMov = false;
    private List<String> lastInputs = new ArrayList<>();
    private boolean turnMode = false;
    private boolean loadingNextLevel = false;
    private int completionWindowStartTime;
    private String currentLevel;

    /**
     * Constructs a GameField instance.
     *
     * @param pui    The physical scanner.
     * @param puiLed The physical Led.
     */
    public GameField(PhysicalScanner pui, PhysicalLed puiLed) {
        this.pui = pui;
        this.puiLed = puiLed;
        levelManager = new LevelManager();
        pui.getController().setGm(this);
        if (FULLSCREEN) {
            collisionHandler = new CollisionHandler(HEIGHT, WIDTH, HEADER_SIZE);
        } else {
            collisionHandler = new CollisionHandler(HEIGHT, WIDTH, HEADER_SIZE);
        }
    }

    @Override
    public void settings() {
        if (FULLSCREEN) {
            fullScreen();
        } else {
            size(WIDTH, HEIGHT);
        }
    }

    /**
     * Draws the game field, including obstacles, goals, and the player.
     */
    public void draw() {
        background(backgroundImage);
        displayAll();
        if (!nextLevel) {
            updateAll();
            collectGoal();
        } else {
            if (!loadingNextLevel) {
                puiLed.blink(PhysicalLed.LEDType.GREEN);
                drawCompletionWindow();
                loadingNextLevel = true;
                completionWindowStartTime = millis(); // Record the time when completion window starts displaying
            } else {
                // Check if 5 seconds have passed since the completion window started displaying
                if (millis() - completionWindowStartTime < 5000) {
                    drawCompletionWindow(); // Draw completion window during the delay
                } else {
                    setup(); // Start the next level after the delay
                    nextLevel = false;
                    loadingNextLevel = false; // Reset flags
                }
            }
        }
    }

    public void drawCompletionWindow() {
        float viewX = 0;
        float viewY = 0;
        if (difficulty == 1) {
            image(imageMap.get("newLevel1"), (float) (viewX + width / 2.0), (float) (viewY + height / 2.0 + 50));
        }
        if (difficulty == 2) {
            image(imageMap.get("newLevel2"), (float) (viewX + width / 2.0), (float) (viewY + height / 2.0 + 50));
        }
        if (difficulty == 3) {
            image(imageMap.get("done"), (float) (viewX + width / 2.0), (float) (viewY + height / 2.0 + 50));
        }
    }

    /**
     * Updates the animation and handles collisions for all game elements.
     */
    void updateAll() {
        if (isMov) {
            player.updateAnimationFrame1();
            isMov = false;
        } else {
            player.updateAnimation();
        }
        collisionHandler.resolveObstaclesCollisions(player, obstacles, 0);
    }

    /**
     * Displays all game elements on the screen.
     */
    void displayAll() {
        List<Sprite> obstaclesCopy = new ArrayList<>(obstacles); // Kopie der Hindernisse erstellen
        for (Sprite ob : obstaclesCopy) {
            ob.display();
        }
        for (Sprite g : goal) {
            g.display();
            ((AnimatedSprite) g).updateAnimation();
        }

        player.display();
        drawHeader();

    }

    /**
     * Draws the header section of the game field, including level information and recent inputs.
     * The header consists of a black rectangle at the top, a grey rectangle below it, level information,
     * and recent inputs displayed in descending order.
     *
     * @see LevelManager
     */
    private void drawHeader() {
        int yLbl = 65;
        int yGrey = 15;
        fill(0, 0, 0);
        rect(0, 0, WIDTH, HEADER_SIZE - yGrey);
        fill(50, 50, 50);
        rect(0, HEADER_SIZE - yGrey, WIDTH, yGrey);
        fill(0, 255, 0);
        textSize(45);
        float viewX = 0;
        float viewY = 0;
        text("Level: " + levelManager.getLevelName(), viewX + 50, viewY + yLbl);
        if (turnMode) {
            text("TURN MODE", viewX + 400, viewY + yLbl);
        }
        int end = Math.min(lastInputs.size(), 5); // End index for the loop
        boolean first = true;
        for (int i = lastInputs.size() - 1; i >= lastInputs.size() - end; i--) {
            if (first) {
                textSize(50);
                text((i) + ": " + lastInputs.get(i), viewX + 1000 + (lastInputs.size() - 1 - i) * 100, viewY + yLbl);
                first = false;
            } else {
                textSize(32);
                text((i) + ": " + lastInputs.get(i), viewX + 1050 + (lastInputs.size() - 1 - i) * 75, viewY + yLbl);
            }
        }
    }

    /**
     * Handles the collection of goals and checks for win conditions.
     */
    private void collectGoal() {
        ArrayList<Sprite> goalList = collisionHandler.checkCollisionList(player, goal);
        if (!goal.isEmpty()) {
            for (Sprite g : goalList) {
                goal.remove(g);
            }

        } else {
            nextLevel = true;
        }
    }

    /**
     * Sets up the initial game state.
     */
    public void setup() {
        loadImages();

        levelSetup();
    }

    private void levelSetup() {
        imageMode(CENTER);
        LOGGER.log(Level.INFO, "levelSETUP");
        lastInputs.clear();

        levelManager.setDifficulty(difficulty);
        currentLevel = levelManager.getNextLevel();
        createPlatforms(currentLevel, java.time.LocalTime.now());
        difficulty = levelManager.getDifficulty();
    }

    private void levelSetup(int i) {
        LOGGER.log(Level.WARNING, Integer.toString(i));
        levelSetup();
    }

    /**
     * restart current level
     */
    public void restart() {
        lastInputs.clear();
        imageMode(CENTER);
        createPlatforms(currentLevel, java.time.LocalTime.now());
    }

    /**
     * Loads images for the game elements.
     */
    private void loadImages() {

        imageMap.put("playerImage", ImageLoader.loadImage(this, "img/roomba2-pixel-dark.png"));
        imageMap.put("chargingStation", ImageLoader.loadImage(this, "img/goal/battery-frame3.png"));
        imageMap.put("wall", ImageLoader.loadImage(this, "img/black.png"));
        imageMap.put("newLevel1", ImageLoader.loadImage(this, "img/response/new-level-1.png"));
        imageMap.put("newLevel2", ImageLoader.loadImage(this, "img/response/new-level-2.png"));
        imageMap.put("done", ImageLoader.loadImage(this, "img/response/done.png"));
        imageMap.put("bricks", ImageLoader.loadImage(this, "img/bricks.png"));

        Path path = ImageLoader.getImagePath("img/obstacles").toAbsolutePath();
        var files = path.toFile().listFiles();
        assert files != null;
        for (var file : files) {
            if (!file.isHidden() && file.getName().endsWith(".png")) {
                pImageListObstacles.add(ImageLoader.loadImage(this, "img/obstacles/" + file.getName()));
            }

            if (!file.isHidden() && file.isDirectory()) {
                String newName = file.getName();
                List<PImage> newList = new ArrayList<>();
                var content = file.listFiles();
                assert content != null;
                for (var c : content) {
                    if (!c.isHidden() && c.getName().endsWith(".png")) {
                        newList.add(ImageLoader.loadImage(this, "img/obstacles/" + newName + "/" + c.getName()));
                    }
                }
                pImageMultiImageObstacles.put(newName, newList);
            }
        }
        if (FULLSCREEN) {
            calculateBackgroundImage();
        } else {
            backgroundImage = ImageLoader.loadImage(this, "img/Room-Floor2.png");
        }

    }

    private void calculateBackgroundImage() {

        int scale = BACKGROUND_SCALE;

        PImage patternFull = ImageLoader.loadImage(this, "img/Room-Floor-HD.png");
        PImage pattern = patternFull.get(0, 0, 128, 128);
        pattern.resize(scale, 0);

        PImage fullSize = createImage(width, height, RGB);
        fullSize.loadPixels();
        pattern.loadPixels();

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                fullSize.pixels[j + i * width] = pattern.pixels[(j % scale) + ((i % scale) * scale)];
            }
        }
        fullSize.updatePixels();

        backgroundImage = fullSize;


    }

    /**
     * Creates platforms based on the given level file.
     *
     * @param filename The filename of the level file.
     */
    private void createPlatforms(String filename, LocalTime date) {
        LevelManager.createPlatforms(this, filename, date);
    }

    private boolean handlingInput = false;
    int countEasy = 0;
    public void handleInput(String input) {
        if (!handlingInput) {
            handlingInput = true;
            assert pui != null;
            LOGGER.log(Level.INFO, "Input: " + input);
            puiLed.blink(PhysicalLed.LEDType.BLUE);
            LOGGER.log(Level.FINE,
                    "handleInput queue item !" + input + "!" + "    nextLevel" + nextLevel + "    player.isInPlace()"
                            + player.isInPlace());
            if (RFID_EASY.contains(input)) {
                LOGGER.log(Level.INFO, "EASY RFID LEVEL CHANGE");
                difficulty = 0;
                levelSetup(countEasy++);
            }
            if (RFID_MEDIUM.contains(input)) {
                LOGGER.log(Level.INFO, "MEDIUM RFID LEVEL CHANGE");
                difficulty = 1;
                levelSetup();
            }
            if (RFID_HARD.contains(input)) {
                LOGGER.log(Level.INFO, "HARD RFID LEVEL CHANGE");
                difficulty = 2;
                levelSetup();
            }
            if (RFID_TURN.contains(input)) {
                LOGGER.log(Level.INFO, "TURN MODE");
                turnMode = !turnMode;
            }
            if (RFID_RESET.contains(input)) {
                LOGGER.log(Level.INFO, "RESET");
                restart();
            } else if (player.isInPlace()) {
                LOGGER.log(Level.INFO, "MOV");
                puiLed.ledOff(PhysicalLed.LEDType.YELLOW);
                if (turnMode) {
                    if (RFID_RIGHT.contains(input)) {
                        LOGGER.log(Level.INFO, "RIGHT");
                        lastInputs.add("→");
                        player.turnPlayer(RIGHT_FACING);
                    }
                    if (RFID_LEFT.contains(input)) {
                        LOGGER.log(Level.INFO, "LEFT");
                        lastInputs.add("←");
                        player.turnPlayer(LEFT_FACING);
                    }
                    if (RFID_UP.contains(input)) {
                        LOGGER.log(Level.INFO, "UP");
                        lastInputs.add("↑");
                        player.movePlayer(UP_FACING);
                    }
                    if (RFID_DOWN.contains(input)) {
                        LOGGER.log(Level.INFO, "DOWN");
                        lastInputs.add("↓");
                        player.movePlayer(DOWN_FACING);
                    }
                } else {
                    if (RFID_RIGHT.contains(input)) {
                        LOGGER.log(Level.INFO, "RIGHT 2");
                        lastInputs.add("→");
                        player.movePlayer(RIGHT_FACING);
                        puiLed.ledOn(PhysicalLed.LEDType.YELLOW);
                    }
                    if (RFID_LEFT.contains(input)) {
                        LOGGER.log(Level.INFO, "LEFT 2");
                        lastInputs.add("←");
                        player.movePlayer(LEFT_FACING);
                        puiLed.ledOn(PhysicalLed.LEDType.YELLOW);
                    }
                    if (RFID_UP.contains(input)) {
                        LOGGER.log(Level.INFO, "UP 2");
                        lastInputs.add("↑");
                        player.movePlayer(UP_FACING);
                        puiLed.ledOn(PhysicalLed.LEDType.YELLOW);
                    }
                    if (RFID_DOWN.contains(input)) {
                        LOGGER.log(Level.INFO, "DOWN 2");
                        lastInputs.add("↓");
                        player.movePlayer(DOWN_FACING);
                        puiLed.ledOn(PhysicalLed.LEDType.YELLOW);
                    }
                }
            }
        }
        handlingInput = false;
    }

    public void keyPressed() {
        if (key == 'l') {
            pui.test("7DCD159B");
        }

        if (key == '1') {
            difficulty = 0;
            setup();
        }
        if (key == '2') {
            difficulty = 1;
            setup();
        }
        if (key == '3') {
            difficulty = 2;
            setup();
        }
        if (Character.toLowerCase(key) == 'r') {
            restart();
        }
        if (Character.toLowerCase(key) == 'h') {
            turnMode = !turnMode;
        }
        if (Character.toLowerCase(key) == 'o') {
            addRFIDCard.openDialog(pui.getRfid());
        } else if (player.isInPlace()) {
            //Options
            if (turnMode) {
                if (((keyCode == UP || key == 'w'))) {
                    lastInputs.add("↑");
                    player.movePlayer(UP_FACING);
                }
                if (((keyCode == DOWN || key == 's'))) {
                    lastInputs.add("↓");
                    player.movePlayer(DOWN_FACING);
                }
                if (((keyCode == RIGHT || key == 'd'))) {
                    lastInputs.add("→");
                    player.turnPlayer(RIGHT_FACING);
                }
                if (((keyCode == LEFT || key == 'a'))) {
                    lastInputs.add("←");
                    player.turnPlayer(LEFT_FACING);
                }
            } else {
                if (((keyCode == RIGHT || key == 'd'))) {
                    lastInputs.add("→");
                    player.movePlayer(RIGHT_FACING);
                }
                if (((keyCode == LEFT || key == 'a'))) {
                    lastInputs.add("←");
                    player.movePlayer(LEFT_FACING);
                }
                if (((keyCode == UP || key == 'w'))) {
                    lastInputs.add("↓");
                    player.movePlayer(UP_FACING);
                }
                if (((keyCode == DOWN || key == 's'))) {
                    lastInputs.add("↑");
                    player.movePlayer(DOWN_FACING);
                }
            }
        }
    }

    /**
     * Stops the application by shutting down the controller and physical user
     * interface.
     */
    @Override
    public void exit() {
        LOGGER.log(Level.WARNING, "Shutdown");
        if (pui != null) {
            puiLed.ledReset(PhysicalLed.LEDType.BLUE);
            puiLed.ledReset(PhysicalLed.LEDType.YELLOW);
            puiLed.ledReset(PhysicalLed.LEDType.GREEN);

            pui.getController().shutdown();
            pui.shutdown();
        }
        super.exit();
    }

    public List<String> getLastInputs() {
        return lastInputs;
    }
}
