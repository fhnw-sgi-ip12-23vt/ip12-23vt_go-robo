package roomba;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;

import processing.core.PImage;
import roomba.view.PhysicalScanner;
import processing.core.PApplet;

/**
 * Represents the game field where the Roomba moves and interacts with obstacles
 * and goals.
 */
public class GameField extends PApplet {
    private static final Logger logger = Logger.getLogger(GameField.class.getName());
    private PhysicalScanner pui;
    private LevelManager levelManager;
    private CollisionHandler collisionHandler;
    private PlayerMovement playerMovement = new PlayerMovement();

    public boolean nextLevel = false;
    public List<Sprite> obstacles;
    public List<Sprite> goal;
    public PImage wall, ball, toy, pillow, plushie, plant1, plant2, computer, paper, chargingStation;
    private int difficulty = 0;
    private Player player;
    private float view_x = 0;
    private float view_y = 0;
    private PImage backgroundImage;
    private static boolean init = true;
    private boolean winCondition = false;

    /**
     * Constructs a GameField instance.
     *
     * @param pui The physical scanner.
     */
    public GameField(PhysicalScanner pui) {
        this.pui = pui;
        levelManager = new LevelManager();
        collisionHandler = new CollisionHandler();
        PInputLogic();
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
        player.updateAnimation();
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
        fill(255, 0, 0);
        textSize(32);
        text("Level: " + levelManager.getLevelName(), view_x + 50, view_y + 100);

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
            PImage playerImage = loadImages();
            player = new Player(this, playerImage, 0.3f);
            obstacles = new ArrayList<Sprite>();
            goal = new ArrayList<Sprite>();
        }

        player.center_x = 100;
        player.change_y = 550;

        levelManager.setDifficulty(difficulty);
        createPlatforms(levelManager.getNextLevel());
        difficulty = levelManager.getDifficulty();
    }

    /**
     * Loads images for the game elements.
     *
     * @return The image for the player.
     */
    private PImage loadImages() {
        PImage p = ImageLoader.loadImage(this, "img/roomba2-pixel-dark.png");
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

        return p;
    }

    /**
     * Creates platforms based on the given level file.
     *
     * @param filename The filename of the level file.
     */
    private void createPlatforms(String filename) {
        levelManager.createPlatforms(this, filename);
    }

    /**
     * Handles physical input logic from the scanner.
     */
    public void PInputLogic() {
        logger.log(Level.FINE, "Init PinputLogic");
        pui.controller.subscribeToQueueChanges((oldValue, newValue) -> {
            logger.log(Level.INFO, "Queue changed: " + newValue);
            handleInput(newValue);
        });
    }

    // TODO replace directions with card ids
    private void handleInput(Queue<String> inputQueue) {
        while (!inputQueue.isEmpty()) {
            String input = pui.controller.dequeue();
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

    // called whenever a key is pressed, will be deleted later on if the handleInput
    // functions
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
}
