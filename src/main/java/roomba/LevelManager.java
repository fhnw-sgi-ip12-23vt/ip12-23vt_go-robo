package roomba;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Random;

import processing.core.PImage;

public class LevelManager {
    private int difficulty = 0;

    public int getDifficulty() {
        return difficulty;
    }
    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

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

        // loads levels with correct difficulty in the list
        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].getName().substring(0, listOfFiles[i].getName().indexOf("_"))
                    .equals("" + difficulty)) {
                rightLevels.add(listOfFiles[i].getName());
            }
        }
        Random r = new Random();
        return "../../resources/files/level/" + rightLevels.get(r.nextInt(rightLevels.size()));
    }

    public void createPlatforms(GameField gameField, String filename) {
        gameField.nextLevel = false;
        gameField.obstacles = new ArrayList<Sprite>();
        gameField.goal = new ArrayList<Sprite>();

        String[] lines = gameField.loadStrings(filename);
        for (int row = 0; row < lines.length; row++) {
            String[] values = gameField.split(lines[row], ",");
            for (int col = 0; col < values.length; col++) {
                switch (values[col]) {
                    case "1" -> {
                        Goal goal_ = new Goal(gameField, gameField.chargingStation, Constants.SPRITE_SCALE);
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
                        PImage[] allObstacleImages = new PImage[] { gameField.ball, gameField.pillow, gameField.toy,
                                gameField.plushie, gameField.plant1, gameField.plant2, gameField.computer,
                                gameField.paper };
                        Random random = new Random();
                        int i = random.nextInt(allObstacleImages.length);

                        Sprite s = new Sprite(gameField, allObstacleImages[i], Constants.SPRITE_SCALE);
                        s.center_x = Constants.SPRITE_SIZE / 2 + col * Constants.SPRITE_SIZE;
                        s.center_y = Constants.SPRITE_SIZE / 2 + row * Constants.SPRITE_SIZE;
                        gameField.obstacles.add(s);
                    }
                }
            }
        }
    }
}
