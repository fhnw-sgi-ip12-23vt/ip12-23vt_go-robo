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
        String levelPath = rightLevels.get(r.nextInt(rightLevels.size()));
        levelName = levelPath.replace(".csv", "");
        return fullPath + "/" + levelPath;


    }

    static List<Sprite> obstacles = new ArrayList<>();

    /**
     * Creates platforms and obstacles for the specified game field based on the level file.
     *
     * @param gameField The game field to create platforms for.
     * @param filename  The name of the level file
     *                  g: goal
     *                  p: player
     *                  w: black wall
     *                  1: ball
     *                  2: bookshelf
     *                  3: computer
     *                  4: game console
     *                  5: shelf
     *                  6: paper
     *                  7: cushion
     *                  8: plant
     *                  9: plant
     *                  t: teddy
     *                  r: random
     *                  a,b,c,d: bett
     *                  e,f: couch
     *                  0: air
     */
    public static void createPlatforms(GameField gameField, String filename) {

        LOGGER.log(Level.INFO, "loading game objects from file " + filename);
        obstacles = new ArrayList<>();


        gameField.nextLevel = false;
        gameField.obstacles = new ArrayList<>();
        gameField.goal = new ArrayList<>();
        List<PImage> bed = gameField.pImageMultiImageObstacles.get("bed");
        List<PImage> couch = gameField.pImageMultiImageObstacles.get("couch");
        String[] lines = gameField.loadStrings(filename);
        int offsetX = 0, offsetY = 0;
        if (System.getProperty("os.name").toLowerCase().contains("mac")) {
            int totalX = (int) (gameField.width / SPRITE_SIZE);
            int totalY = (int) (gameField.height / SPRITE_SIZE);
            offsetX = (totalX - (GameField.split(lines[0], ",")).length) / 2;
            offsetY = (totalY - lines.length + 2) / 2;
        }
        for (int row = 0; row < lines.length; row++) {
            String[] values = GameField.split(lines[row], ",");
            for (int col = 0; col < values.length; col++) {
                int colOffset = offsetX + col;
                int rowOffset = offsetY + row;
                switch (values[col]) {
                case "g" -> {
                    Goal loadedGoal =
                        new Goal(gameField, gameField.imageMap.get("chargingStation"), SPRITE_SIZE * 0.007f);
                    loadedGoal.centerX = SPRITE_SIZE / 2 + colOffset * SPRITE_SIZE;
                    loadedGoal.centerY = SPRITE_SIZE / 2 + rowOffset * SPRITE_SIZE;
                    gameField.goal.add(loadedGoal);
                }
                case "w" -> {
                    createObstacle(gameField, gameField.imageMap.get("wall"), colOffset, rowOffset);
                }
                case "B" -> {
                    createObstacle(gameField, gameField.imageMap.get("bricks"), colOffset, rowOffset);
                }
                case "p" -> {
                    Player player = new Player(gameField, gameField.imageMap.get("playerImage"), SPRITE_SIZE * 0.006f);
                    player.centerX = SPRITE_SIZE / 2 + colOffset * SPRITE_SIZE;
                    player.centerY = SPRITE_SIZE / 2 + rowOffset * SPRITE_SIZE;
                    gameField.player = player;
                }
                case "1", "2", "3", "4", "5", "6", "7", "8", "9" -> {
                    createObstacle(gameField, Integer.parseInt(values[col]) - 1, colOffset, rowOffset);
                }
                case "t" -> {
                    createObstacle(gameField, 9, colOffset, rowOffset);
                }
                // RANDOM OBSTACLES
                case "r" -> {
                    List<PImage> allObstacleImages = gameField.pImageListObstacles;
                    Random random = new Random();
                    int i = random.nextInt(allObstacleImages.size());
                    createObstacle(gameField, i, colOffset, rowOffset);
                }
                // BED
                case "a" -> {
                    createObstacle(gameField, bed.get(0), colOffset, rowOffset);
                }
                case "b" -> {
                    createObstacle(gameField, bed.get(2), colOffset, rowOffset);
                }
                case "c" -> {
                    createObstacle(gameField, bed.get(1), colOffset, rowOffset);
                }
                case "d" -> {
                    createObstacle(gameField, bed.get(3), colOffset, rowOffset);
                }
                // COUCH
                case "e" -> {
                    createObstacle(gameField, couch.get(0), colOffset, rowOffset);
                }
                case "f" -> {
                    createObstacle(gameField, couch.get(1), colOffset, rowOffset);
                }
                // Air does nothing
                case "", "0" -> {
                }
                //0 does nothing
                default -> LOGGER.log(Level.WARNING, "Illegal Character in csv file: " + values[col]);
                }
            }
        }
        gameField.obstacles.addAll(obstacles);
    }

    public static void createObstacle(GameField gameField, int i, int col, int row) {
        Sprite s = new Sprite(gameField, gameField.pImageListObstacles.get(i), SPRITE_SCALE);
        s.centerX = SPRITE_SIZE / 2 + col * SPRITE_SIZE;
        s.centerY = SPRITE_SIZE / 2 + row * SPRITE_SIZE;
        obstacles.add(s);
    }

    public static void createObstacle(GameField gameField, PImage pImage, int col, int row) {
        Sprite s = new Sprite(gameField, pImage, SPRITE_SCALE);
        s.centerX = SPRITE_SIZE / 2 + col * SPRITE_SIZE;
        s.centerY = SPRITE_SIZE / 2 + row * SPRITE_SIZE;
        obstacles.add(s);

    }
}
