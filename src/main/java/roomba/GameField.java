package roomba;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import processing.core.PImage;
import roomba.view.PhysicalScanner;
import processing.core.PApplet;

public class GameField extends PApplet {
    PhysicalScanner pui;
    LevelManager levelManager;
    CollisionHandler collisionHandler;

    public GameField(PhysicalScanner pui) {
        this.pui = pui;
        collisionHandler = new CollisionHandler();
    }

    private int difficulty = 0;
    public boolean nextLevel = false;
    List<Sprite> obstacles;
    List<Sprite> goal;
    Player player;
    String levelPath = "../../resources/files/";
    PImage wall, ball, toy, pillow, plushie, plant1, plant2, computer, paper, chargingStation;

    float view_x = 0;
    float view_y = 0;

    @Override
    public void settings() {
        if (Constants.FULLSCREEN) {
            fullScreen();
        } else {
            size(Constants.WIDTH, Constants.HEIGHT);
        }
    }

    public void PInputLogic() {
        pui.controller.subscribeToQueueChanges((oldValue, newValue) -> {
            // Handle queue changes
            System.out.println("Queue changed: " + newValue);
            handleInput(newValue);
        });
    }

    void handleInput(Queue<String> inputQueue) {
        while (!inputQueue.isEmpty()) {
            String input = pui.controller.dequeue();
            switch (input) {
                case "RIGHT":
                    // Handle movement to the right
                    player.change_y = 0;
                    player.change_x = Constants.MOVE_SPEED;
                    break;
                case "LEFT":
                    // Handle movement to the left
                    player.change_y = 0;
                    player.change_x = -Constants.MOVE_SPEED;
                    break;
                case "UP":
                    // Handle movement upwards
                    player.change_y = -Constants.MOVE_SPEED;
                    player.change_x = 0;
                    break;
                case "DOWN":
                    // Handle movement downwards
                    player.change_y = Constants.MOVE_SPEED;
                    player.change_x = 0;
                    break;
                // Add more cases for other directions if needed
            }
        }
    }

    public void draw() {
        if (Constants.FULLSCREEN) {

            // TODO fix lag
            PImage backgroundImage = ImageLoader.loadImage(this, "img/Room-Floor-HD.png");
            backgroundImage.resize(width, height);
            background(backgroundImage);
        } else {
            background(ImageLoader.loadImage(this, "img/Room-Floor2.png"));
        }

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
        text("Level: " + player.levelName, view_x + 50, view_y + 100);

        // if (winCondition) {
        // fill(0, 0, 255);
        // text("Du hast es geschaft!", (float) (view_x + width / 2.0 - 100), (float)
        // (view_y +
        // height / 2.0 + 50));

        // //nach 5s rufe setup methode auf ...
        // }
    }

    public void setup() {
        imageMode(CENTER);
        PImage p = ImageLoader.loadImage(this, "img/roomba2-pixel-dark.png");
        player = new Player(this, p, 0.3f);
        player.center_x = 100;
        player.change_y = 550;
        obstacles = new ArrayList<Sprite>();
        goal = new ArrayList<Sprite>();

        chargingStation = ImageLoader.loadImage(this, "img/charger.png");
        wall = ImageLoader.loadImage(this, "img/red_brick.png");
        ball = ImageLoader.loadImage(this, "img/obstacles/ball.png");
        pillow = ImageLoader.loadImage(this, "img/obstacles/pillow.png");
        toy = ImageLoader.loadImage(this, "img/obstacles/bookshelf.png");
        plushie = ImageLoader.loadImage(this, "img/obstacles/Teddy.png");
        plant1 = ImageLoader.loadImage(this, "img/obstacles/plant1.png");
        plant2 = ImageLoader.loadImage(this, "img/obstacles/plant2.png");
        computer = ImageLoader.loadImage(this, "img/obstacles/computer.png");
        paper = ImageLoader.loadImage(this, "img/obstacles/paper.png");

        levelManager = new LevelManager(); // Instantiate LevelManager
        levelManager.setDifficulty(difficulty);
        createPlatforms(levelManager.getNextLevel()); // Use LevelManager to get the next level
        difficulty = levelManager.getDifficulty();

    }

    void createPlatforms(String filename) {
        levelManager.createPlatforms(this, filename); // Use LevelManager to create platforms
    }

    void collectGoal() {
        ArrayList<Sprite> goal_list = collisionHandler.checkCollisionList(player, goal);
        if (!goal.isEmpty()) {
            for (Sprite g : goal_list) {
                goal.remove(g);
            }
        } else {
            nextLevel = true;
        }
    }

    // called whenever a key is pressed.
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
            System.out.println(player.direction);
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
