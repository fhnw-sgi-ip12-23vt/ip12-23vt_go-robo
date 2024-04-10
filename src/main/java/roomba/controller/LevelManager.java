package roomba.controller;

import processing.core.PImage;
import roomba.model.Constants;
import roomba.model.Goal;
import roomba.model.Player;
import roomba.model.Sprite;
import roomba.view.GameField;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Manages the levels in the "Roomba in Trouble" game.
 */
public class LevelManager {
    private static final Logger logger = Logger.getLogger(LevelManager.class.getName());

    private String levelName;
    private int difficulty = 0;

    /**
     * Gets the name of the current level.
     *
     * @return The name of the current level.
     */
    public String getLevelName() {
        if (levelName.substring(0, levelName.indexOf("_")).equals("1")) {
            return "Tutorial: " + levelName.substring(levelName.indexOf("_") + 1);
        }
        if (levelName.substring(0, levelName.indexOf("_")).equals("2")) {
            return "Normal: " + levelName.substring(levelName.indexOf("_") + 1);
        }
        if (levelName.substring(0, levelName.indexOf("_")).equals("3")) {
            return "Schwer: " + levelName.substring(levelName.indexOf("_") + 1);
        }
        return levelName;
    }

    /**
     * Gets the current difficulty level.
     *
     * @return The current difficulty level.
     */
    public int getDifficulty() {
        return difficulty;
    }

    /**
     * Sets the difficulty level.
     *
     * @param difficulty The new difficulty level.
     */
    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    /**
     * Gets the next level file path based on the difficulty.
     *
     * @return The file path of the next level.
     */
    public String getNextLevel() {
        logger.log(Level.FINE, "Next Level");

        File[] listOfFiles;
        // Resets difficulty after 3
        difficulty++;
        if (difficulty == 4) {
            difficulty = 1;
        }

        // Load directory using ImageLoader
        // String relativePath = "files/level";
        // Path fullPath = ImageLoader.getImagePath(relativePath);
        Path fullPath = ImageLoader.loadFile("files/level");

        File folder = new File(fullPath.toString());
        listOfFiles = folder.listFiles();
        ArrayList<String> rightLevels = new ArrayList<String>();

        // Loads levels with correct difficulty in the list
        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].getName().substring(0, listOfFiles[i].getName().indexOf("_"))
                .equals("" + difficulty)) {
                rightLevels.add(listOfFiles[i].getName());
            }
        }
        Random r = new Random();
        levelName = rightLevels.get(r.nextInt(rightLevels.size())).replace(".csv", "");
        return "files/level/" + rightLevels.get(r.nextInt(rightLevels.size()));
    }

    /**
     * Creates platforms and obstacles for the specified game field based on the level file.
     *
     * @param gameField The game field to create platforms for.
     * @param filename  The name of the level file.
     */
    public void createPlatforms(GameField gameField, String filename) {
        logger.log(Level.FINE, "load from file game Objects");

        gameField.nextLevel = false;
        gameField.obstacles = new ArrayList<Sprite>();
        gameField.goal = new ArrayList<Sprite>();

        String[] lines = gameField.loadStrings(filename);
        for (int row = 0; row < lines.length; row++) {
            String[] values = GameField.split(lines[row], ",");
            for (int col = 0; col < values.length; col++) {
                switch (values[col]) {
                case "1" -> {
                    Goal goal_ = new Goal(gameField, gameField.chargingStation, Constants.SPRITE_SIZE * 0.0055f);
                    goal_.center_x = Constants.SPRITE_SIZE / 2 + col * Constants.SPRITE_SIZE;
                    goal_.center_y = Constants.SPRITE_SIZE / 2 + row * Constants.SPRITE_SIZE;
                    gameField.goal.add(goal_);
                }
                case "2" -> {
                    Sprite s = new Sprite(gameField, gameField.wall, Constants.SPRITE_SCALE);
                    s.center_x = Constants.SPRITE_SIZE / 2 + col * Constants.SPRITE_SIZE;
                    s.center_y = Constants.SPRITE_SIZE / 2 + row * Constants.SPRITE_SIZE;
                    gameField.obstacles.add(s);
                }
                case "3" -> {
                    PImage[] allObstacleImages = new PImage[] {gameField.ball, gameField.pillow, gameField.toy,
                        gameField.plushy, gameField.plant1, gameField.plant2, gameField.computer,
                        gameField.paper};
                    Random random = new Random();
                    int i = random.nextInt(allObstacleImages.length);

                    Sprite s = new Sprite(gameField, allObstacleImages[i], Constants.SPRITE_SCALE);
                    s.center_x = Constants.SPRITE_SIZE / 2 + col * Constants.SPRITE_SIZE;
                    s.center_y = Constants.SPRITE_SIZE / 2 + row * Constants.SPRITE_SIZE;
                    gameField.obstacles.add(s);
                }
                case "4" -> {
                    Player player = new Player(gameField, gameField.playerImage, Constants.SPRITE_SIZE * 0.006f);
                    player.center_x = Constants.SPRITE_SIZE / 2 + col * Constants.SPRITE_SIZE;
                    player.center_y = Constants.SPRITE_SIZE / 2 + row * Constants.SPRITE_SIZE;
                    gameField.player = player;
                }
                }
            }
        }
    }
}
