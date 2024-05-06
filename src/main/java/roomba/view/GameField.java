package roomba.view;

import processing.core.PApplet;
import processing.core.PImage;
import roomba.controller.CollisionHandler;
import roomba.controller.ImageLoader;
import roomba.controller.LevelManager;
import roomba.model.AnimatedSprite;
import roomba.model.Player;
import roomba.model.Sprite;
import java.util.ArrayList;
import java.util.List;
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
    private final LevelManager levelManager;
    private final CollisionHandler collisionHandler;
    public boolean nextLevel = false;
    public List<Sprite> obstacles;
    public List<Sprite> goal;
    public PImage wall, ball, toy, pillow, plushy, plant1, plant2, computer, paper, chargingStation, playerImage;
    public Player player;
    private int difficulty = 0;
    private PImage backgroundImage;
    private boolean winCondition = false;
    private boolean isMov = false;
    private List<String> lastInputs = new ArrayList<>();
    private boolean turnMode = false;
    private boolean loadingNextLevel = false;

    /**
     * Constructs a GameField instance.
     *
     * @param pui The physical scanner.
     */
    public GameField(PhysicalScanner pui) {
        this.pui = pui;
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
            // If the next level flag is set, delay input for 5 seconds
            if (!loadingNextLevel) {
                delay(5000); // 5 seconds delay
                loadingNextLevel = true;
            } else {
                setup(); // Start the next level after the delay
                loadingNextLevel = false; // Reset the flag for the next level
            }
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

        //Header
        fill(0, 0, 0);
        rect(0, 0, WIDTH, HEADER_SIZE);
        fill(0, 255, 0);
        textSize(32);
        float viewX = 0;
        float viewY = 0;
        text("Level: " + levelManager.getLevelName(), viewX + 50, viewY + 50);
        if (turnMode){
            text("Turn-Mode", viewX + 300, viewY + 50);
        } else {
            text("Normal-Mode", viewX + 300, viewY + 50);
        }
        int start = Math.max(0, lastInputs.size() - 5); // Start index for the loop
        for (int i = start; i < lastInputs.size(); i++) {
            text(lastInputs.get(i), viewX + 1200 + (i - start) * 25, viewY + 50);
        }

        if (winCondition) {
            fill(0, 0, 255);
            text("Du hast es geschafft!", (float) (viewX + width / 2.0 - 100), (float) (viewY + height / 2.0 + 50));
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
            winCondition = difficulty == 3;
        }
    }

    /**
     * Sets up the initial game state.
     */
    public void setup() {
        winCondition = false;
        imageMode(CENTER);
        loadImages();

        levelManager.setDifficulty(difficulty);
        createPlatforms(levelManager.getNextLevel(false));
        difficulty = levelManager.getDifficulty();
    }

    /**
     * restart current level
     */
    public void restart() {
        winCondition = false;
        imageMode(CENTER);
        loadImages();

        levelManager.setDifficulty(difficulty);
        createPlatforms(levelManager.getNextLevel(true));
        difficulty = levelManager.getDifficulty();
    }

    /**
     * Loads images for the game elements.
     */
    private void loadImages() {
        playerImage = ImageLoader.loadImage(this, "img/roomba2-pixel-dark.png");
        chargingStation = ImageLoader.loadImage(this, "img/goal/battery-frame0.png");
        wall = ImageLoader.loadImage(this, "img/red_brick.png");
        ball = ImageLoader.loadImage(this, "img/obstacles/ball.png");
        pillow = ImageLoader.loadImage(this, "img/obstacles/pillow.png");
        toy = ImageLoader.loadImage(this, "img/obstacles/bookshelf.png");
        plushy = ImageLoader.loadImage(this, "img/obstacles/Teddy.png");
        plant1 = ImageLoader.loadImage(this, "img/obstacles/plant1.png");
        plant2 = ImageLoader.loadImage(this, "img/obstacles/plant2.png");
        computer = ImageLoader.loadImage(this, "img/obstacles/computer.png");
        paper = ImageLoader.loadImage(this, "img/obstacles/paper.png");

        if (FULLSCREEN) {
            backgroundImage = ImageLoader.loadImage(this, "img/Room-Floor-HD.png");
            backgroundImage.resize(width, height);
        } else {
            backgroundImage = ImageLoader.loadImage(this, "img/Room-Floor2.png");
        }

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
            LOGGER.log(Level.FINE,
                "handleInput queue item !" + input + "!" + "    nextLevel" + nextLevel + "    player.isInPlace()"
                    + player.isInPlace());

            if (nextLevel) {
                setup();
            } else if (player.isInPlace()) {
                if (turnMode){
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
                    }
                    if (RFID_LEFT.contains(input)) {
                        lastInputs.add("←");
                        player.movePlayer(LEFT_FACING);
                    }
                    if (RFID_UP.contains(input)) {
                        lastInputs.add("↑");
                        player.movePlayer(UP_FACING);
                    }
                    if (RFID_DOWN.contains(input)) {
                        lastInputs.add("↓");
                        player.movePlayer(DOWN_FACING);
                    }
                }
                if (input.equals(RFID_NEXT.contains(input))){
                    nextLevel = true;
                }
                if(input.equals(RFID_RESET.contains(input))){
                    restart();
                }
            }
        }
    }

    public void keyPressed() {
        if (nextLevel) {
            setup();
        } else if (player.isInPlace()) {
            if (key == 'h'){
                if (turnMode){
                    turnMode = false;
                } else {
                    turnMode = true;
                }
            }
            if (turnMode){
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
            pui.getController().shutdown();
            pui.shutdown();
        }
        super.exit();
    }
}
