package roomba.model;

import roomba.controller.ImageLoader;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.io.FileInputStream;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

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
    public static final Set<String> RFID_RIGHT;
    public static final Set<String> RFID_LEFT;
    public static final Set<String> RFID_UP;
    public static final Set<String> RFID_DOWN;
    public static final Set<String> RFID_RESET;
    public static final Set<String> RFID_NEXT;

    static {
        float moveSpeed = 4;
        float spriteSize = 50;
        float spriteScale = spriteSize / 128;
        int height = (int) (spriteSize * 12);
        int width = (int) (spriteSize * 16);
        boolean fullscreen = false;
        Set<String> rfidRight = new HashSet<>();
        Set<String> rfidLeft = new HashSet<>();
        Set<String> rfidUp = new HashSet<>();
        Set<String> rfidDown = new HashSet<>();
        Set<String> rfidReset = new HashSet<>();
        Set<String> rfidNext = new HashSet<>();

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
            }

            //RfID Sets
            String[] ids;
            ids = appProps.getProperty("RFID_RIGHT").split(", ");
            rfidRight.addAll(Arrays.asList(ids));
            ids = appProps.getProperty("RFID_LEFT").split(", ");
            rfidLeft.addAll(Arrays.asList(ids));
            ids = appProps.getProperty("RFID_UP").split(", ");
            rfidUp.addAll(Arrays.asList(ids));
            ids = appProps.getProperty("RFID_DOWN").split(", ");
            rfidDown.addAll(Arrays.asList(ids));
            ids = appProps.getProperty("RFID_RESET").split(", ");
            rfidReset.addAll(Arrays.asList(ids));
            ids = appProps.getProperty("RFID_NEXT").split(", ");
            rfidNext.addAll(Arrays.asList(ids));

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
