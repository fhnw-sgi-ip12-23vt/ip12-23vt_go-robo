package roomba.view;

import processing.core.PApplet;
import processing.core.PImage;
import roomba.controller.CollisionHandler;
import roomba.controller.ImageLoader;
import roomba.controller.LevelManager;
import roomba.model.AnimatedSprite;
import roomba.model.Player;
import roomba.model.Sprite;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import static roomba.model.Constants.*;


/**
 * Represents the game field where the Roomba moves and interacts with obstacles
 * and goals.
 */
public class GameField extends PApplet {
    private static final Logger LOGGER = Logger.getLogger(GameField.class.getName());
    private static final float HEADER_SIZE = 113;
    private final PhysicalScanner pui;
    private final PhysicalLed puiLed;
    private final LevelManager levelManager;
    private final CollisionHandler collisionHandler;
    public boolean nextLevel = false;
    public List<Sprite> obstacles;
    public List<Sprite> goal;
    public PImage wall, chargingStation, playerImage;
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
     * @param pui The physical scanner.
     * @param puiLed The physical Led.
     */
    public GameField(PhysicalScanner pui, PhysicalLed puiLed) {
        this.pui = pui;
        this.puiLed = puiLed;
        levelManager = new LevelManager();
        if (FULLSCREEN) {
            collisionHandler = new CollisionHandler(HEIGHT - 230, WIDTH - 400, HEADER_SIZE);
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
            image(neueslevel1, (float) (viewX + width / 2.0), (float) (viewY + height / 2.0 + 50));
        }
        if (difficulty == 2) {
            image(neueslevel2, (float) (viewX + width / 2.0), (float) (viewY + height / 2.0 + 50));
        }
        if (difficulty == 3) {
            image(geschafft, (float) (viewX + width / 2.0), (float) (viewY + height / 2.0 + 50));
        }
    }

    /**
     * Updates the animation and handles collisions for all game elements.
     */
    void updateAll() {
        handleInput();
        if (isMov) {
            player.updateAnimationFrame1();
            isMov = false;
        } else {
            player.updateAnimation();
        }
        collisionHandler.resolveObstaclesCollisions(player, obstacles);
    }

    /**
     * Displays all game elements on the screen.
     */
    void displayAll() {
        for (Sprite ob : obstacles) {
            ob.display();
        }
        for (Sprite g : goal) {
            g.display();
            ((AnimatedSprite) g).updateAnimation();
        }

        player.display();
        drawHeader();

    }

    private void drawHeader() {
        int yLbl = 75;
//        fill(0, 0, 0);
//        rect(0, 0, WIDTH, HEADER_SIZE);
        fill(0, 255, 0);
        textSize(45);
        float viewX = 0;
        float viewY = 0;
        text("Level: " + levelManager.getLevelName(), viewX + 50, viewY + yLbl);
        if (turnMode) {
            text("H", viewX + 400, viewY + yLbl);
        }
        textSize(50);
        int end = Math.min(lastInputs.size(), 5); // End index for the loop
        for (int i = lastInputs.size() - 1; i >= lastInputs.size() - end; i--) {
            text(lastInputs.size() - 1 - i + ": " + lastInputs.get(i), viewX + 1000 + (lastInputs.size() - 1 - i) * 85,
                viewY + yLbl);
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
        imageMode(CENTER);
        loadImages();

        levelManager.setDifficulty(difficulty);
        currentLevel = levelManager.getNextLevel();
        createPlatforms(currentLevel);
        difficulty = levelManager.getDifficulty();
    }

    /**
     * restart current level
     */
    public void restart() {
        imageMode(CENTER);
        loadImages();

        createPlatforms(currentLevel);
    }

    /**
     * Loads images for the game elements.
     */
    private void loadImages() {
        playerImage = ImageLoader.loadImage(this, "img/roomba2-pixel-dark.png");
        chargingStation = ImageLoader.loadImage(this, "img/goal/battery-frame3.png");
        wall = ImageLoader.loadImage(this, "img/red_brick.png");
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

        int scale = 114;

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
    private void createPlatforms(String filename) {
        levelManager.createPlatforms(this, filename);
    }

    private void handleInput() {
        assert pui != null;
        if (!pui.getController().getQueue().getValue().isEmpty()) {
            String input = pui.getController().dequeue();
            puiLed.blink(PhysicalLed.LEDType.BLUE);
            LOGGER.log(Level.FINE,
                "handleInput queue item !" + input + "!" + "    nextLevel" + nextLevel + "    player.isInPlace()"
                    + player.isInPlace());
            if (RFID_EASY.contains(input)) {
                difficulty = 0;
                setup();
            }
            if (RFID_MEDIUM.contains(input)) {
                difficulty = 1;
                setup();
            }
            if (RFID_HARD.contains(input)) {
                difficulty = 2;
                setup();
            }
            if (RFID_TURN.contains(input)) {
                turnMode = !turnMode;
            }
            if (RFID_RESET.contains(input)) {
                restart();
            } else if (player.isInPlace()) {
                puiLed.ledOff(PhysicalLed.LEDType.YELLOW);
                if (turnMode) {
                    if (RFID_RIGHT.contains(input)) {
                        lastInputs.add("→");
                        player.turnPlayer(RIGHT_FACING);
                    }
                    if (RFID_LEFT.contains(input)) {
                        lastInputs.add("←");
                        player.turnPlayer(LEFT_FACING);
                    }
                    if (RFID_UP.contains(input)) {
                        lastInputs.add("↑");
                        player.movePlayer(UP_FACING);
                    }
                    if (RFID_DOWN.contains(input)) {
                        lastInputs.add("↓");
                        player.movePlayer(DOWN_FACING);
                    }
                } else {
                    if (RFID_RIGHT.contains(input)) {
                        lastInputs.add("→");
                        player.movePlayer(RIGHT_FACING);
                        puiLed.ledOn(PhysicalLed.LEDType.YELLOW);
                    }
                    if (RFID_LEFT.contains(input)) {
                        lastInputs.add("←");
                        player.movePlayer(LEFT_FACING);
                        puiLed.ledOn(PhysicalLed.LEDType.YELLOW);
                    }
                    if (RFID_UP.contains(input)) {
                        lastInputs.add("↑");
                        player.movePlayer(UP_FACING);
                        puiLed.ledOn(PhysicalLed.LEDType.YELLOW);
                    }
                    if (RFID_DOWN.contains(input)) {
                        lastInputs.add("↓");
                        player.movePlayer(DOWN_FACING);
                        puiLed.ledOn(PhysicalLed.LEDType.YELLOW);
                    }
                }
            }
        }
    }

    public void keyPressed() {
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
        if (key == 'r') {
            restart();
        }
        if (key == 'h') {
            turnMode = !turnMode;
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
}
