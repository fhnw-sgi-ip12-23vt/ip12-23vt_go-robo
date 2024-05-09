package roomba.controller;

import processing.core.PImage;
import roomba.model.Goal;
import roomba.model.Player;
import roomba.model.Sprite;
import roomba.view.GameField;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import static roomba.model.Constants.SPRITE_SCALE;
import static roomba.model.Constants.SPRITE_SIZE;

/**
 * Manages the levels in the "Roomba in Trouble" game.
 */
public class LevelManager {
    private static final Logger LOGGER = Logger.getLogger(LevelManager.class.getName());

    private String levelName;
    private int difficulty = 0;

    /**
     * Gets the name of the current level.
     *
     * @return The name of the current level.
     */
    public String getLevelName() {
        if (levelName.substring(0, levelName.indexOf("_")).equals("1")) {
            return "1 - " + levelName.substring(levelName.indexOf("_") + 1);
        }
        if (levelName.substring(0, levelName.indexOf("_")).equals("2")) {
            return "2 - " + levelName.substring(levelName.indexOf("_") + 1);
        }
        if (levelName.substring(0, levelName.indexOf("_")).equals("3")) {
            return "3 - " + levelName.substring(levelName.indexOf("_") + 1);
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
        LOGGER.log(Level.FINE, "Next Level");

        boolean mac = false;
        File[] listOfFiles;

        // Resets difficulty after 3
        difficulty++;
        if (difficulty == 4) {
            difficulty = 1;
        }

        Path fullPath = ImageLoader.loadFile("files/level");

        File folder = new File(fullPath.toString());
        listOfFiles = folder.listFiles();
        ArrayList<String> rightLevels = new ArrayList<>();

        // Loads levels with correct difficulty in the list
        for (int i = 0; i < Objects.requireNonNull(listOfFiles).length; i++) {
            if (listOfFiles[i].getName().substring(0, listOfFiles[i].getName().indexOf("_"))
                .equals("" + difficulty)) {
                rightLevels.add(listOfFiles[i].getName());
            }
        }
        Random r = new Random();
        levelName = rightLevels.get(r.nextInt(rightLevels.size())).replace(".csv", "");
        if (mac) {
            return fullPath + "/" + rightLevels.get(r.nextInt(rightLevels.size()));
        }
        return "files/level/" + rightLevels.get(r.nextInt(rightLevels.size()));
    }

    /**
     * Creates platforms and obstacles for the specified game field based on the level file.
     *
     * @param gameField The game field to create platforms for.
     * @param filename  The name of the level file.
     */
    public void createPlatforms(GameField gameField, String filename) {
        LOGGER.log(Level.FINE, "loading game objects from file ");

        gameField.nextLevel = false;
        gameField.obstacles = new ArrayList<>();
        gameField.goal = new ArrayList<>();

        List<PImage> bed = gameField.pImageMultiImageObstacles.get("bed");
        List<PImage> couch = gameField.pImageMultiImageObstacles.get("couch");

        String[] lines = gameField.loadStrings(filename);
        for (int row = 0; row < lines.length; row++) {
            String[] values = GameField.split(lines[row], ",");
            for (int col = 0; col < values.length; col++) {
                switch (values[col]) {
                case "g" -> {
                    Goal loadedGoal =
                        new Goal(gameField, gameField.imageMap.get("chargingStation"), SPRITE_SIZE * 0.009f);
                    loadedGoal.centerX = SPRITE_SIZE / 2 + col * SPRITE_SIZE;
                    loadedGoal.centerY = SPRITE_SIZE / 2 + row * SPRITE_SIZE;
                    gameField.goal.add(loadedGoal);

                }
                case "w" -> {
                    Sprite s = new Sprite(gameField, gameField.imageMap.get("wall"), SPRITE_SCALE);
                    s.centerX = SPRITE_SIZE / 2 + col * SPRITE_SIZE;
                    s.centerY = SPRITE_SIZE / 2 + row * SPRITE_SIZE;
                    gameField.obstacles.add(s);
                }

                case "p" -> {
                    Player player = new Player(gameField, gameField.imageMap.get("playerImage"), SPRITE_SIZE * 0.006f);
                    player.centerX = SPRITE_SIZE / 2 + col * SPRITE_SIZE;
                    player.centerY = SPRITE_SIZE / 2 + row * SPRITE_SIZE;
                    gameField.player = player;
                }

                case "1" -> {
                    createObstacle(gameField, 0, col, row); }
                case "2" -> {
                    createObstacle(gameField, 1, col, row); }
                case "3" -> {
                    createObstacle(gameField, 2, col, row); }
                case "4" -> {
                    createObstacle(gameField, 3, col, row); }
                case "5" -> {
                    createObstacle(gameField, 4, col, row); }
                case "6" -> {
                    createObstacle(gameField, 5, col, row); }
                case "7" -> {
                    createObstacle(gameField, 6, col, row); }
                case "8" -> {
                    createObstacle(gameField, 7, col, row); }
                case "9" -> {
                    createObstacle(gameField, 8, col, row); }

                // RANDOM OBSTACLES
                case "r" -> {
                    List<PImage> allObstacleImages = gameField.pImageListObstacles;
                    Random random = new Random();
                    int i = random.nextInt(allObstacleImages.size());
                    createObstacle(gameField, i, col, row);
                }
                // BED
                case "a" -> {
                    createObstacle(gameField, bed.get(0), col, row); }
                case "b" -> {
                    createObstacle(gameField, bed.get(2), col, row); }
                case "c" -> {
                    createObstacle(gameField, bed.get(1), col, row); }
                case "d" -> {
                    createObstacle(gameField, bed.get(3), col, row); }
                // COUCH
                case "e" -> {
                    createObstacle(gameField, couch.get(0), col, row); }
                case "f" -> {
                    createObstacle(gameField, couch.get(1), col, row); }
                // Air does nothing
                case "" -> { }
                //0 does nothing
                case "0" -> { }
                default -> LOGGER.log(Level.WARNING, "Illegal Character in csv file: " + values[col]);

                }
            }
        }
    }

    public void createObstacle(GameField gameField, int i, int col, int row) {
        Sprite s = new Sprite(gameField, gameField.pImageListObstacles.get(i), SPRITE_SCALE);
        s.centerX = SPRITE_SIZE / 2 + col * SPRITE_SIZE;
        s.centerY = SPRITE_SIZE / 2 + row * SPRITE_SIZE;
        gameField.obstacles.add(s);
    }

    public void createObstacle(GameField gameField, PImage pImage, int col, int row) {
        Sprite s = new Sprite(gameField, pImage, SPRITE_SCALE);
        s.centerX = SPRITE_SIZE / 2 + col * SPRITE_SIZE;
        s.centerY = SPRITE_SIZE / 2 + row * SPRITE_SIZE;
        gameField.obstacles.add(s);

    }
}
