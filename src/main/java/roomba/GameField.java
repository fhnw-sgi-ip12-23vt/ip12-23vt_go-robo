package roomba;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

import processing.core.PImage;
import roomba.view.PhysicalScanner;
import processing.core.PApplet;

public class GameField extends PApplet {
    PhysicalScanner pui;

    public GameField(PhysicalScanner pui) {
        this.pui = pui;
    }

    private boolean nextLevel = false;
    List<Sprite> obstacles;
    List<Sprite> goal;
    Player player;
    int difficulty = 0;
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

            //TODO fix lag
            PImage  backgroundImage = loadImage("../../resources/img/Room-Floor-HD.png");
            backgroundImage.resize(width, height);

            background(backgroundImage);

        } else {
            background(loadImage("../../resources/img/Room-Floor2.png"));

        }

        displayAll();

        if (!nextLevel) {
            updateAll();
            collectGoal();
        }

    }

    void updateAll() {
        player.updateAnimation();
        resolveObstaclesCollisions(player, obstacles);

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
        PImage p = loadImage("../../resources/img/roomba2-pixel-dark.png");
        player = new Player(this, p, 0.3f);
        player.center_x = 100;
        player.change_y = 550;
        obstacles = new ArrayList<Sprite>();
        goal = new ArrayList<Sprite>();

        chargingStation = loadImage("../../resources/img/charger.png");

        wall = loadImage("../../resources/img/red_brick.png");
        ball = loadImage("../../resources/img/obstacles/ball.png");
        pillow = loadImage("../../resources/img/obstacles/pillow.png");
        toy = loadImage("../../resources/img/obstacles/bookshelf.png");
        plushie = loadImage("../../resources/img/obstacles/Teddy.png");
        plant1 = loadImage("../../resources/img/obstacles/plant1.png");
        plant2 = loadImage("../../resources/img/obstacles/plant2.png");
        computer = loadImage("../../resources/img/obstacles/computer.png");
        paper = loadImage("../../resources/img/obstacles/paper.png");

        createPlatforms(getNextLevel());
    }

    // selects a level, currently random within difficulty tier
    public String getNextLevel() {
        File[] listOfFiles = new File[0];
        // resets difficulty after 3
        difficulty++;
        if (difficulty == 4) {
            difficulty = 1;
        }

        // load directory and needs to be like that cause java file objects are weird
        // with directories
        String relativePath = "src/main/resources/files/level";
        Path fullPath = Paths.get(System.getProperty("user.dir"), relativePath);
        File folder = new File(fullPath.toString());
        listOfFiles = folder.listFiles();
        ArrayList<String> rightLevels = new ArrayList<String>();

        // loads levels with correct difficulty in list
        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].getName().substring(0, listOfFiles[i].getName().indexOf("_")).equals("" + difficulty)) {
                rightLevels.add(listOfFiles[i].getName());
            }
        }
        Random r = new Random();
        return "../../resources/files/level/" + rightLevels.get(r.nextInt(rightLevels.size()));
    }

    void collectGoal() {
        ArrayList<Sprite> goal_list = checkCollisionList(player, goal);
        if (!goal.isEmpty()) {
            for (Sprite g : goal_list) {
                goal.remove(g);
            }
        }
        if (goal.isEmpty()) {
            nextLevel = true;
            // TODO
            // Call next leve

        }
    }

    public boolean isHitObstacles(Sprite s, List<Sprite> walls) {

        ArrayList<Sprite> col_list = checkCollisionList(s, walls);
        return !col_list.isEmpty();
    }

    public void resolveObstaclesCollisions(Sprite s, List<Sprite> walls) {
        s.center_y += s.change_y;
        ArrayList<Sprite> col_list = checkCollisionList(s, walls);
        if (!col_list.isEmpty()) {
            Sprite collided = col_list.get(0);
            if (s.change_y > 0) {
                s.setBottom(collided.getTop());
                stopAtObstacle();

            } else if (s.change_y < 0) {
                s.setTop(collided.getBottom());
                stopAtObstacle();

            }
            s.change_y = 0;
        }

        s.center_x += s.change_x;
        col_list = checkCollisionList(s, walls);
        if (!col_list.isEmpty()) {
            Sprite collided = col_list.get(0);
            if (s.change_x > 0) {
                s.setRight(collided.getLeft());
                stopAtObstacle();
            } else if (s.change_x < 0) {
                s.setLeft(collided.getRight());
                stopAtObstacle();
            }

            s.change_x = 0;
        }

    }

    boolean checkCollision(Sprite s1, Sprite s2) {
        boolean noXOverlap = s1.getRight() <= s2.getLeft() || s1.getLeft() >= s2.getRight();
        boolean noYOverlap = s1.getBottom() <= s2.getTop() || s1.getTop() >= s2.getBottom();
        return !noXOverlap && !noYOverlap;
    }

    public ArrayList<Sprite> checkCollisionList(Sprite s, List<Sprite> list) {
        ArrayList<Sprite> collision_list = new ArrayList<>();
        for (Sprite p : list) {
            if (checkCollision(s, p))
                collision_list.add(p);
        }
        return collision_list;
    }

    void createPlatforms(String filename) {
        nextLevel = false;

        String[] lines = loadStrings(filename);
        for (int row = 0; row < lines.length; row++) {
            String[] values = split(lines[row], ",");
            for (int col = 0; col < values.length; col++) {
                switch (values[col]) {
                    case "1" -> {
                        Goal goal_ = new Goal(this, chargingStation, Constants.SPRITE_SCALE);
                        goal_.center_x = Constants.SPRITE_SIZE / 2 + col * Constants.SPRITE_SIZE;
                        goal_.center_y = Constants.SPRITE_SIZE / 2 + row * Constants.SPRITE_SIZE;
                        goal.add(goal_);
                    }
                    case "2" -> {
                        Sprite s = new Sprite(this, wall, Constants.SPRITE_SCALE);
                        s.center_x = Constants.SPRITE_SIZE / 2 + col * Constants.SPRITE_SIZE;
                        s.center_y = Constants.SPRITE_SIZE / 2 + row * Constants.SPRITE_SIZE;
                        obstacles.add(s);
                    }
                    case "3" -> {
                        PImage[] allObstacleImages = new PImage[] { ball, pillow, toy, plushie, plant1, plant2,
                                computer, paper };
                        Random random = new Random();
                        int i = random.nextInt(allObstacleImages.length);

                        Sprite s = new Sprite(this, allObstacleImages[i], Constants.SPRITE_SCALE);
                        s.center_x = Constants.SPRITE_SIZE / 2 + col * Constants.SPRITE_SIZE;
                        s.center_y = Constants.SPRITE_SIZE / 2 + row * Constants.SPRITE_SIZE;
                        obstacles.add(s);
                    }
                }
            }
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

    public void stopAtObstacle() {
        if (player.hitObstacle) {

            player.change_x = 0;
            player.change_y = 0;

        }
    }

}
