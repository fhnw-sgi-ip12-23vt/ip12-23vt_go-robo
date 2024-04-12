package roomba.model;

import roomba.controller.ImageLoader;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.io.FileInputStream;
import java.nio.file.Path;
import java.util.Properties;

/**
 * Represents constants used in the "Roomba in Trouble" game.
 */
public final class Constants {

    public static final int NEUTRAL_FACING = 0;
    public static final int RIGHT_FACING = 1;
    public static final int LEFT_FACING = 2;
    public static final int UP_FACING = 3;
    public static final int DOWN_FACING = 4;
    public static float MOVE_SPEED = 4;
    public static float SPRITE_SIZE = 50;
    public static float SPRITE_SCALE = SPRITE_SIZE / 128;
    public static int HEIGHT = (int) (SPRITE_SIZE * 12);
    public static int WIDTH = (int) (SPRITE_SIZE * 16);
    public static boolean FULLSCREEN = false;
    public static String RFID_RIGHT = "";
    public static String RFID_LEFT = "";
    public static String RFID_UP = "";
    public static String RFID_DOWN = "";
    public static String RFID_RESET = "";
    public static String RFID_NEXT = "";

    static {
        initConfigs();
    }

    private Constants() {
        throw new AssertionError("Utility class should not be instantiated");
    }

    /**
     * Initializes the game configuration from the app.properties file.
     */
    private static void initConfigs() {
        try {
            Path fullPath = ImageLoader.loadFile("/main/java/app.properties");
            Properties appProps = new Properties();
            appProps.load(new FileInputStream(fullPath.toString()));

            MOVE_SPEED = Float.parseFloat(appProps.getProperty("MOVE_SPEED"));
            SPRITE_SIZE = Float.parseFloat(appProps.getProperty("SPRITE_SIZE"));
            SPRITE_SCALE = SPRITE_SIZE / 128;
            FULLSCREEN = Boolean.parseBoolean(appProps.getProperty("FULLSCREEN"));

            if (FULLSCREEN) {
                GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
                HEIGHT = Integer.parseInt(String.valueOf(gd.getDisplayMode().getHeight()));
                WIDTH = Integer.parseInt(String.valueOf(gd.getDisplayMode().getWidth()));

            } else {
                HEIGHT = Integer.parseInt(appProps.getProperty("HEIGHT"));
                WIDTH = Integer.parseInt(appProps.getProperty("WIDTH"));
            }

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
