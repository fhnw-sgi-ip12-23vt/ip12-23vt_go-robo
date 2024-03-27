package roomba;

import java.awt.*;
import java.io.FileInputStream;
import java.nio.file.Path;
import java.util.Properties;

/**
 * Represents constants used in the "Roomba in Trouble" game.
 */
public class Constants {

    static float MOVE_SPEED = 4;
    static float SPRITE_SCALE = (float) (50.0 / 128);
    static float SPRITE_SIZE = 50;
    static int HEIGHT = (int) (SPRITE_SIZE * 12);
    static int WIDTH = (int) (SPRITE_SIZE * 16);
    static final int NEUTRAL_FACING = 0;
    static final int RIGHT_FACING = 1;
    static final int LEFT_FACING = 2;
    static final int UP_FACING = 3;
    static final int DOWN_FACING = 4;
    static boolean FULLSCREEN = true;
    static String RFID_RIGHT = "";
    static String RFID_LEFT = "";
    static String RFID_UP = "";
    static String RFID_DOWN = "";
    static String RFID_RESET = "";
    static String RFID_NEXT = "";

    static {
        initConfigs();
    }

    /**
     * Initializes the game configuration from the app.properties file.
     */
    private static void initConfigs() {
        try {
            // String relativePath = "src/main/java/app.properties";
            // Path fullPath = Paths.get(System.getProperty("user.dir"), relativePath);

           Path fullPath = ImageLoader.loadFile("/main/java/app.properties");

            Properties appProps = new Properties();
            appProps.load(new FileInputStream(fullPath.toString()));

            MOVE_SPEED = Float.parseFloat(appProps.getProperty("MOVE_SPEED"));
            SPRITE_SCALE = Float.parseFloat(appProps.getProperty("SPRITE_SCALE"));
            SPRITE_SIZE = Float.parseFloat(appProps.getProperty("SPRITE_SIZE"));
            FULLSCREEN = Boolean.parseBoolean(appProps.getProperty("FULLSCREEN"));
            if (FULLSCREEN){
                GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
                HEIGHT = Integer.parseInt(String.valueOf(gd.getDisplayMode().getHeight()));
                WIDTH = Integer.parseInt(String.valueOf(gd.getDisplayMode().getWidth()));

            } else {
                HEIGHT = Integer.parseInt(appProps.getProperty("HEIGHT"));
                WIDTH = Integer.parseInt(appProps.getProperty("WIDTH"));
            }
            // RFID
            RFID_RIGHT = appProps.getProperty("RFID_RIGHT");
            RFID_LEFT = appProps.getProperty("RFID_LEFT");
            RFID_UP = appProps.getProperty("RFID_UP");
            RFID_DOWN = appProps.getProperty("RFID_DOWN");
            RFID_RESET = appProps.getProperty("RFID_RESET");
            RFID_NEXT = appProps.getProperty("RFID_NEXT");

        } catch (Exception e) {
            System.err.println("Error reading config file: " + e.getMessage());
        }
    }
}
