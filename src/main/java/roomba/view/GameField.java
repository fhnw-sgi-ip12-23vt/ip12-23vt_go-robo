package roomba.view;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;
import roomba.controller.LevelManager;
import roomba.model.Player;
import roomba.controller.PlayerMovement;
import roomba.model.Sprite;
import roomba.controller.CollisionHandler;
import roomba.controller.ImageLoader;
import roomba.model.AnimatedSprite;
import roomba.model.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Represents the game field where the Roomba moves and interacts with obstacles
 * and goals.
 */
public class GameField extends PApplet {
    private static final Logger logger = Logger.getLogger(GameField.class.getName());
    private final PhysicalScanner pui;
    private final LevelManager levelManager;
    private final CollisionHandler collisionHandler;
    private final PlayerMovement playerMovement = new PlayerMovement();

    public boolean nextLevel = false;
    public List<Sprite> obstacles;
    public List<Sprite> goal;
    public PImage wall, ball, toy, pillow, plushie, plant1, plant2, computer, paper, chargingStation, playerImage;
    private int difficulty = 0;
    public Player player;
    private float view_x = 0;
    private float view_y = 0;
    private float headerSize;
    private PImage backgroundImage;
    private static boolean init = true;
    private boolean winCondition = false;
    private boolean isMov = false;

    /**
     * Constructs a GameField instance.
     *
     * @param pui The physical scanner.
     */
    public GameField(PhysicalScanner pui) {
        this.pui = pui;
        levelManager = new LevelManager();
        this.headerSize = 100;
        if (Constants.FULLSCREEN){
            collisionHandler = new CollisionHandler(Constants.HEIGHT - 230, Constants.WIDTH - 400, headerSize);
        } else {
            collisionHandler = new CollisionHandler(Constants.HEIGHT, Constants.WIDTH, headerSize);
        }
    }

    public GameField() {
        this.pui = null;
        levelManager = new LevelManager();
        this.headerSize = 100;
        if (Constants.FULLSCREEN){
            collisionHandler = new CollisionHandler(Constants.HEIGHT - 230, Constants.WIDTH - 400, headerSize);
        } else {
            collisionHandler = new CollisionHandler(Constants.HEIGHT, Constants.WIDTH, headerSize);
        }
    }

    @Override
    public void settings() {
        if (Constants.FULLSCREEN) {
            fullScreen();
        } else {
            size(Constants.WIDTH, Constants.HEIGHT);
        }
    }

    /**
     * Draws the game field, including obstacles, goals, and the player.
     */
    public void draw() {
        clear();
        background(backgroundImage);
        displayAll();
        if (!nextLevel) {
            updateAll();
            collectGoal();
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
        }else {
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
        rect(0, 0, Constants.WIDTH, 100);
        fill(0, 255, 0);
        textSize(32);
        text("Level: " + levelManager.getLevelName(), view_x + 50, view_y + 50);

        if (winCondition) {
            fill(0, 0, 255);
            text("Du hast es geschafft!", (float) (view_x + width / 2.0 - 100), (float) (view_y + height / 2.0 + 50));
        }
    }

    /**
     * Handles the collection of goals and checks for win conditions.
     */
    private void collectGoal() {
        ArrayList<Sprite> goal_list = collisionHandler.checkCollisionList(player, goal);
        if (!goal.isEmpty()) {
            for (Sprite g : goal_list) {
                goal.remove(g);
            }
        } else {
            nextLevel = true;
            winCondition = (nextLevel && (difficulty == 3));
        }
    }

    /**
     * Sets up the initial game state.
     */
    public void setup() {
        winCondition = false;
        imageMode(CENTER);
        if (init) {
            loadImages();
        }

        levelManager.setDifficulty(difficulty);
        createPlatforms(levelManager.getNextLevel());
        difficulty = levelManager.getDifficulty();
    }

    /**
     * Loads images for the game elements.
     *
     * @return The image for the player.
     */
    private void loadImages() {
        playerImage = ImageLoader.loadImage(this, "img/roomba2-pixel-dark.png");
        chargingStation = ImageLoader.loadImage(this, "img/goal/battery-frame0.png");
        wall = ImageLoader.loadImage(this, "img/red_brick.png");
        ball = ImageLoader.loadImage(this, "img/obstacles/ball.png");
        pillow = ImageLoader.loadImage(this, "img/obstacles/pillow.png");
        toy = ImageLoader.loadImage(this, "img/obstacles/bookshelf.png");
        plushie = ImageLoader.loadImage(this, "img/obstacles/Teddy.png");
        plant1 = ImageLoader.loadImage(this, "img/obstacles/plant1.png");
        plant2 = ImageLoader.loadImage(this, "img/obstacles/plant2.png");
        computer = ImageLoader.loadImage(this, "img/obstacles/computer.png");
        paper = ImageLoader.loadImage(this, "img/obstacles/paper.png");

        if (Constants.FULLSCREEN) {
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

//    /**
//     * Handles physical input logic from the scanner.
//     */
//    public void PInputLogic() {
//        logger.log(Level.FINE, "Init PinputLogic");
//        pui.controller.subscribeToQueueChanges((oldValue, newValue) -> {
//            logger.log(Level.INFO, "Queue changed: " + newValue);
//            handleInput();
//        });
//    }

    private void handleInput() {
        if (!pui.controller.getQueue().getValue().isEmpty()) {
            String input = pui.controller.dequeue();
            logger.log(Level.FINE, "handleInput queue item !"+input + "!"  + "    nextLevel"+nextLevel+ "    player.isInPlace()"+player.isInPlace());

            if (nextLevel) {
                setup();
            } else if (player.isInPlace()) {

                if (input.equals(Constants.RFID_RIGHT)) {

                    playerMovement.movePlayer(player, Constants.RIGHT_FACING);
                }
                if (input.equals(Constants.RFID_LEFT)) {
                        playerMovement.movePlayer(player, Constants.LEFT_FACING);
                }
                if (input.equals(Constants.RFID_UP)) {
                        playerMovement.movePlayer(player, Constants.UP_FACING);
                }
                if (input.equals(Constants.RFID_DOWN)) {
                    playerMovement.movePlayer(player, Constants.DOWN_FACING);
                }
            }
        }
    }

    public void keyPressed() {
        if (nextLevel) {
            setup();
        } else if (player.isInPlace()) {
            if (((keyCode == RIGHT || key == 'd'))) {
                playerMovement.movePlayer(player, Constants.RIGHT_FACING);
            }
            if (((keyCode == LEFT || key == 'a'))) {
                playerMovement.movePlayer(player, Constants.LEFT_FACING);
            }
            if (((keyCode == UP || key == 'w'))) {
                playerMovement.movePlayer(player, Constants.UP_FACING);
            }
            if (((keyCode == DOWN || key == 's'))) {
                playerMovement.movePlayer(player, Constants.DOWN_FACING);
            }
        }
    }

    /**
     * Stops the application by shutting down the controller and physical user
     * interface.
     */
    @Override
    public void exit() {
        logger.log(Level.WARNING, "Shutdown");
        if (pui != null){
            pui.controller.shutdown();
            pui.shutdown();
        }
        super.exit();
    }
}
