package roomba;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import processing.core.PImage;
import roomba.view.PhysicalScanner;
import processing.core.PApplet;

public class GameField extends PApplet {
    private PhysicalScanner pui;
    private LevelManager levelManager;
    private CollisionHandler collisionHandler;

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

    public GameField(PhysicalScanner pui) {
        this.pui = pui;
        levelManager = new LevelManager();
        collisionHandler = new CollisionHandler();
    }

    @Override
    public void settings() {
        if (Constants.FULLSCREEN) {
            fullScreen();
        } else {
            size(Constants.WIDTH, Constants.HEIGHT);
        }
    }

    public void draw() {
        clear();

        background(backgroundImage);

        displayAll();
        if (!nextLevel) {
            updateAll();
            collectGoal();
        }
    }

    void updateAll() {
        player.updateAnimation();
        collisionHandler.resolveObstaclesCollisions(player, obstacles);
    }

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
            text("Du hast es geschaft!", (float) (view_x + width / 2.0 - 100), (float) (view_y +
                    height / 2.0 + 50));
        }
    }

        private void collectGoal() {
        ArrayList<Sprite> goal_list = collisionHandler.checkCollisionList(player, goal);
        if (!goal.isEmpty()) {
            for (Sprite g : goal_list) {
                goal.remove(g);
            }
        } else {
            nextLevel = true;

          winCondition =  (nextLevel && (difficulty ==3)) ?  true : false;
            
        }
    }

    public void setup() {
        winCondition = false;
        imageMode(CENTER);
        // just load once
        if (init) {
            PImage playerImage = loadImages();
            player = new Player(this, playerImage, 0.3f);
            obstacles = new ArrayList<Sprite>();
            goal = new ArrayList<Sprite>();
        }

        player.center_x = 100;
        player.change_y = 550;

        levelManager.setDifficulty(difficulty);
        createPlatforms(levelManager.getNextLevel()); // Use LevelManager to get the next level
        difficulty = levelManager.getDifficulty();

    }

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

    private void createPlatforms(String filename) {
        levelManager.createPlatforms(this, filename); // Use LevelManager to create platforms
    }

    public void PInputLogic() {
        pui.controller.subscribeToQueueChanges((oldValue, newValue) -> {
            // Handle queue changes
            System.out.println("Queue changed: " + newValue);
            handleInput(newValue);
        });
    }

    //TODO replace directions with card ids
    private void handleInput(Queue<String> inputQueue) {
        while (!inputQueue.isEmpty()) {
            String input = pui.controller.dequeue();
            if (nextLevel) {
                setup();
            } else {
                switch (input) {
                    case "RIGHT":
                        if (player.direction == Constants.RIGHT_FACING) { // right
                            player.change_y = Constants.MOVE_SPEED;
                            player.change_x = 0;
                        }
                        if (player.direction == Constants.LEFT_FACING) { // left
                            player.change_y = -Constants.MOVE_SPEED;
                            player.change_x = 0;
                        }
                        if (player.direction == Constants.UP_FACING) { // up
                            player.change_y = 0;
                            player.change_x = -Constants.MOVE_SPEED;
                        }
                        if (player.direction == Constants.DOWN_FACING) { // down
                            player.change_y = 0;
                            player.change_x = Constants.MOVE_SPEED;
                        }
                        break;
                    case "LEFT":
                        if (player.direction == Constants.RIGHT_FACING) {
                            player.change_y = -Constants.MOVE_SPEED;
                            player.change_x = 0;
                        }
                        if (player.direction == Constants.LEFT_FACING) {
                            player.change_y = Constants.MOVE_SPEED;
                            player.change_x = 0;
                        }
                        if (player.direction == Constants.UP_FACING) {
                            player.change_y = 0;
                            player.change_x = Constants.MOVE_SPEED;
                        }
                        if (player.direction == Constants.DOWN_FACING) { // down
                            player.change_y = 0;
                            player.change_x = -Constants.MOVE_SPEED;
                        }
                        break;
                    case "UP":
                        if (player.direction == Constants.RIGHT_FACING) { // right
                            player.change_y = 0;
                            player.change_x = Constants.MOVE_SPEED;
                        }
                        if (player.direction == Constants.LEFT_FACING) { // left
                            player.change_y = 0;
                            player.change_x = -Constants.MOVE_SPEED;
                        }
                        if (player.direction == Constants.UP_FACING) { // up
                            player.change_y = Constants.MOVE_SPEED;
                            player.change_x = 0;
                        }
                        if (player.direction == Constants.DOWN_FACING) { // down
                            player.change_y = -Constants.MOVE_SPEED;
                            player.change_x = 0;
                        }
                        break;
                    case "DOWN":
                        if (player.direction == Constants.RIGHT_FACING) { // right
                            player.change_x = -Constants.MOVE_SPEED;
                            player.change_y = 0;
                        }
                        if (player.direction == Constants.LEFT_FACING) { // left
                            player.change_x = Constants.MOVE_SPEED;
                            player.change_y = 0;
                        }
                        if (player.direction == Constants.UP_FACING) { // up
                            player.change_x = 0;
                            player.change_y = -Constants.MOVE_SPEED;
                        }
                        if (player.direction == Constants.DOWN_FACING) { // down
                            player.change_x = 0;
                            player.change_y = Constants.MOVE_SPEED;
                        }
                        break;
                }
            }
        }
    }

    // called whenever a key is pressed, will be deleted later on if the handleInput functions
    public void keyPressed() {

        if (nextLevel) {
            setup();
        } else if (((keyCode == RIGHT || key == 'd') && player.inPlace)) {
            if (player.direction == Constants.RIGHT_FACING) { // right
                player.change_y = Constants.MOVE_SPEED;
                player.change_x = 0;
            }
            if (player.direction == Constants.LEFT_FACING) { // left
                player.change_y = -Constants.MOVE_SPEED;
                player.change_x = 0;
            }
            if (player.direction == Constants.UP_FACING) { // up
                player.change_y = 0;
                player.change_x = -Constants.MOVE_SPEED;
            }
            if (player.direction == Constants.DOWN_FACING) { // down
                player.change_y = 0;
                player.change_x = Constants.MOVE_SPEED;
            }

        } else if (((keyCode == LEFT || key == 'a'))
                && player.inPlace) {
            if (player.direction == Constants.RIGHT_FACING) {
                player.change_y = -Constants.MOVE_SPEED;
                player.change_x = 0;
            }
            if (player.direction == Constants.LEFT_FACING) {
                player.change_y = Constants.MOVE_SPEED;
                player.change_x = 0;
            }
            if (player.direction == Constants.UP_FACING) {
                player.change_y = 0;
                player.change_x = Constants.MOVE_SPEED;
            }
            if (player.direction == Constants.DOWN_FACING) { // down
                player.change_y = 0;
                player.change_x = -Constants.MOVE_SPEED;
            }

        } else if (((keyCode == DOWN || key == 's'))
                && player.inPlace) {
            if (player.direction == Constants.RIGHT_FACING) { // right
                player.change_x = -Constants.MOVE_SPEED;
                player.change_y = 0;
            }
            if (player.direction == Constants.LEFT_FACING) { // left
                player.change_x = Constants.MOVE_SPEED;
                player.change_y = 0;
            }
            if (player.direction == Constants.UP_FACING) { // up
                player.change_x = 0;
                player.change_y = -Constants.MOVE_SPEED;
            }
            if (player.direction == Constants.DOWN_FACING) { // down
                player.change_x = 0;
                player.change_y = Constants.MOVE_SPEED;
            }
        } else if (((keyCode == UP || key == 'w')) && player.inPlace) {
            if (player.direction == Constants.RIGHT_FACING) { // right
                player.change_y = 0;
                player.change_x = Constants.MOVE_SPEED;
            }
            if (player.direction == Constants.LEFT_FACING) { // left
                player.change_y = 0;
                player.change_x = -Constants.MOVE_SPEED;
            }
            if (player.direction == Constants.UP_FACING) { // up
                player.change_y = Constants.MOVE_SPEED;
                player.change_x = 0;
            }
            if (player.direction == Constants.DOWN_FACING) { // down
                player.change_y = -Constants.MOVE_SPEED;
                player.change_x = 0;
            }

        }
    }

}
