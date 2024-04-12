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
    public static final float MOVE_SPEED;
    public static final float SPRITE_SIZE;
    public static final float SPRITE_SCALE;
    public static final int HEIGHT;
    public static final int WIDTH;
    public static final boolean FULLSCREEN;
    public static final String RFID_RIGHT;
    public static final String RFID_LEFT;
    public static final String RFID_UP;
    public static final String RFID_DOWN;
    public static final String RFID_RESET;
    public static final String RFID_NEXT;

    static {
        float moveSpeed = 4;
        float spriteSize = 50;
        float spriteScale = spriteSize / 128;
        int height = (int) (spriteSize * 12);
        int width = (int) (spriteSize * 16);
        boolean fullscreen = false;
        String rfidRight = "";
        String rfidLeft = "";
        String rfidUp = "";
        String rfidDown = "";
        String rfidReset = "";
        String rfidNext = "";

        try {
            Path fullPath = ImageLoader.loadFile("/main/java/app.properties");
            Properties appProps = new Properties();
            appProps.load(new FileInputStream(fullPath.toString()));

            moveSpeed = Float.parseFloat(appProps.getProperty("MOVE_SPEED"));
            spriteSize = Float.parseFloat(appProps.getProperty("SPRITE_SIZE"));
            spriteScale = spriteSize / 128;
            fullscreen = Boolean.parseBoolean(appProps.getProperty("FULLSCREEN"));

            if (fullscreen) {
                GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
                height = Integer.parseInt(String.valueOf(gd.getDisplayMode().getHeight()));
                width = Integer.parseInt(String.valueOf(gd.getDisplayMode().getWidth()));

            } else {
                height = Integer.parseInt(appProps.getProperty("HEIGHT"));
                width = Integer.parseInt(appProps.getProperty("WIDTH"));
            }

            rfidRight = appProps.getProperty("RFID_RIGHT");
            rfidLeft = appProps.getProperty("RFID_LEFT");
            rfidUp = appProps.getProperty("RFID_UP");
            rfidDown = appProps.getProperty("RFID_DOWN");
            rfidReset = appProps.getProperty("RFID_RESET");
            rfidNext = appProps.getProperty("RFID_NEXT");

        } catch (Exception e) {
            System.err.println("Error reading config file: " + e.getMessage());
        }

        MOVE_SPEED = moveSpeed;
        SPRITE_SIZE = spriteSize;
        SPRITE_SCALE = spriteScale;
        HEIGHT = height;
        WIDTH = width;
        FULLSCREEN = fullscreen;
        RFID_RIGHT = rfidRight;
        RFID_LEFT = rfidLeft;
        RFID_UP = rfidUp;
        RFID_DOWN = rfidDown;
        RFID_RESET = rfidReset;
        RFID_NEXT = rfidNext;
    }

    private Constants() {
        throw new AssertionError("Utility class should not be instantiated");
    }
}
