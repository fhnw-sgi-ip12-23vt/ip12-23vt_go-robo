package roomba;

import java.util.ArrayList;
import java.util.List;

import processing.core.PImage;
import processing.core.PApplet;

public class GameField extends PApplet {

    private final int Xsize = 800;
    private final int Ysize = 600;

    private boolean nextLevel = false;
    List<Sprite> obstacles;
    List<Sprite> goal;
    Player player;
    PImage ball, toy, pillow, plushie, chargingStation;

    float view_x = 0;
    float view_y = 0;

    @Override
    public void settings() {
        size(Xsize, Ysize);
    }

    public void draw() {
        background(255);

        displayAll();

        if (!nextLevel) {
            updateAll();
            collectGoal();
        }

    }

    void updateAll() {
        player.updateAnimation();
        resolvePlatformCollisions(player, obstacles);

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
        PImage p = loadImage("../../resources/img/player.png");
        player = new Player(this, p, 0.5f);
        player.center_x = 100;
        player.change_y = 550;
        obstacles = new ArrayList<Sprite>();
        goal = new ArrayList<Sprite>();
        
        chargingStation = loadImage("../../resources/img/gold1.png");

        ball = loadImage("../../resources/img/red_brick.png");
        pillow = loadImage("../../resources/img/spider_walk_right1.png");
        toy = loadImage("../../resources/img/red_brick.png");
        plushie = loadImage("../../resources/img/brown_brick.png");

        createPlatforms("../../resources/files/map.csv");
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
            // Call next levle

        }
    }

    public boolean isHitObstacles(Sprite s, List<Sprite> walls) {
        s.center_y += 5;
        ArrayList<Sprite> col_list = checkCollisionList(s, walls);
        s.center_y -= 5;
        return !col_list.isEmpty();
    }

    public void resolvePlatformCollisions(Sprite s, List<Sprite> walls) {
        s.change_y += 0;
        s.center_y += s.change_y;
        ArrayList<Sprite> col_list = checkCollisionList(s, walls);
        if (!col_list.isEmpty()) {
            Sprite collided = col_list.get(0);
            if (s.change_y > 0) {
                s.setBottom(collided.getTop());
            } else if (s.change_y < 0) {
                s.setTop(collided.getBottom());
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
                        Sprite s = new Sprite(this, ball, Constants.SPRITE_SCALE);
                        s.center_x = Constants.SPRITE_SIZE / 2 + col * Constants.SPRITE_SIZE;
                        s.center_y = Constants.SPRITE_SIZE / 2 + row * Constants.SPRITE_SIZE;
                        obstacles.add(s);
                    }
                    case "3" -> {
                        Sprite s = new Sprite(this, toy, Constants.SPRITE_SCALE);
                        s.center_x = Constants.SPRITE_SIZE / 2 + col * Constants.SPRITE_SIZE;
                        s.center_y = Constants.SPRITE_SIZE / 2 + row * Constants.SPRITE_SIZE;
                        obstacles.add(s);
                    }
                    case "4" -> {
                        Sprite s = new Sprite(this, pillow, Constants.SPRITE_SCALE);
                        s.center_x = Constants.SPRITE_SIZE / 2 + col * Constants.SPRITE_SIZE;
                        s.center_y = Constants.SPRITE_SIZE / 2 + row * Constants.SPRITE_SIZE;
                        obstacles.add(s);
                    }
                    case "5" -> {
                        Sprite s = new Sprite(this, plushie, Constants.SPRITE_SCALE);
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
        } else if ((keyCode == RIGHT || key == 'd') || player.direction == Constants.NEUTRAL_FACING) {
            player.change_y = 0;
            player.change_x = Constants.MOVE_SPEED;
        } else if ((keyCode == LEFT || key == 'a')  || player.direction == Constants.NEUTRAL_FACING) {
            player.change_y = 0;
            player.change_x = -Constants.MOVE_SPEED;
        } else if ((keyCode == DOWN || key == 's') || player.direction == Constants.NEUTRAL_FACING) {
            player.change_x = 0;
            player.change_y = Constants.MOVE_SPEED;
        } else if ((keyCode == UP || key == 'w') || player.direction == Constants.NEUTRAL_FACING) {
            player.change_x = 0;
            player.change_y = -Constants.MOVE_SPEED;
        }
    }

    public void stopAtObstacle(){
        if (player.hitObstacle){
            player.change_x = 0;
            player.change_y = 0;
        }
    }

}
