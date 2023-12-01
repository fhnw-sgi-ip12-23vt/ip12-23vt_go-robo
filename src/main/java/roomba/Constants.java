package roomba;

import java.io.FileInputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class Constants {
    
    static float MOVE_SPEED = 4;
    static float SPRITE_SCALE = (float) (50.0 / 128);
    static float SPRITE_SIZE = 50;
    static int HEIGHT = (int) (SPRITE_SIZE * 12);
    static int WIDTH = (int) (SPRITE_SIZE * 16);
    static int NEUTRAL_FACING = 0;
    static int RIGHT_FACING = 1;
    static int LEFT_FACING = 2;
    static int UP_FACING = 3;
    static int DOWN_FACING = 4;
    static boolean FULLSCREEN = true;

    static {
        initConfigs();
    }

    private static void initConfigs() {
        try {
            // String rootPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
            // String appConfigPath = rootPath + "app.properties";

            String relativePath = "src/main/java/app.properties";
            Path fullPath = Paths.get(System.getProperty("user.dir"), relativePath);

            Properties appProps = new Properties();
            appProps.load(new FileInputStream(fullPath.toString()));

            MOVE_SPEED = Float.parseFloat(appProps.getProperty("MOVE_SPEED"));
            SPRITE_SCALE = Float.parseFloat(appProps.getProperty("SPRITE_SCALE"));
            SPRITE_SIZE = Float.parseFloat(appProps.getProperty("SPRITE_SIZE"));
            HEIGHT = Integer.parseInt(appProps.getProperty("HEIGHT"));
            WIDTH = Integer.parseInt(appProps.getProperty("WIDTH"));
            // NEUTRAL_FACING = Integer.parseInt(appProps.getProperty("NEUTRAL_FACING"));
            // RIGHT_FACING = Integer.parseInt(appProps.getProperty("RIGHT_FACING"));
            // LEFT_FACING = Integer.parseInt(appProps.getProperty("LEFT_FACING"));
            // UP_FACING = Integer.parseInt(appProps.getProperty("UP_FACING"));
            // DOWN_FACING = Integer.parseInt(appProps.getProperty("DOWN_FACING"));
            FULLSCREEN = Boolean.parseBoolean(appProps.getProperty("FULLSCREEN"));

        } catch (Exception e) {
            System.err.println("Error reading config file: " + e.getMessage());
        }
    }
}
