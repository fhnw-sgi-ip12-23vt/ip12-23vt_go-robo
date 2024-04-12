package roomba.controller;

import processing.core.PApplet;
import processing.core.PImage;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Utility class for loading images in the "Roomba in Trouble" game.
 */
public final class ImageLoader {
    private static final Logger LOGGER = Logger.getLogger(ImageLoader.class.getName());
    private ImageLoader() {
        throw new AssertionError("Utility class should not be instantiated");
    }
    /**
     * Loads an image using the provided PApplet and filename.
     *
     * @param applet   The PApplet instance.
     * @param filename The filename of the image.
     * @return The loaded PImage.
     */
    public static PImage loadImage(PApplet applet, String filename) {
        Path imagePath = getImagePath(filename);
        return applet.loadImage(imagePath.toString());
    }

    /**
     * Gets the path of the image based on the operating system.
     *
     * @param filename The filename of the image.
     * @return The Path object representing the image path.
     */
    public static Path getImagePath(String filename) {
        String os = System.getProperty("os.name").toLowerCase();
        String currentDirectory = System.getProperty("user.dir");
        LOGGER.log(Level.FINE, "OS: " + os);
        LOGGER.log(Level.FINE, "FilePath: "
            + FileSystems.getDefault().getPath(currentDirectory, "target/classes", filename));

        if (os.contains("win")) {
            // Windows path
            LOGGER.log(Level.FINE, "Windows path");
            return FileSystems.getDefault().getPath(currentDirectory, "target/classes", filename);
        } else if (os.contains("mac")) {
            // MacOS path
            LOGGER.log(Level.FINE, "MacOS path");
            return FileSystems.getDefault().getPath("target/classes", filename);
        } else if (os.contains("nix") || os.contains("nux")) {
            // Unix path (Maven project structure)
            LOGGER.log(Level.FINE, "Unix path");
            return FileSystems.getDefault().getPath(currentDirectory, "target/classes", filename);
        } else {
            // Default path
            LOGGER.log(Level.FINE, "Default path");
            return FileSystems.getDefault().getPath("src/main/resources", filename);
        }
    }

    public static Path loadFile(String filename) {
        return getFilePath(filename);
    }

    /**
     * Gets the full path for a file based on the operating system.
     *
     * @param filename The filename of the file.
     * @return The Path object representing the full file path.
     */
    private static Path getFilePath(String filename) {
        String os = System.getProperty("os.name").toLowerCase();
        String currentDirectory = System.getProperty("user.dir");
        LOGGER.log(Level.FINE, "OS: " + os);
        LOGGER.log(Level.FINE, "FilePath: "
            + FileSystems.getDefault().getPath(currentDirectory, "target/classes", filename));

        if (os.contains("win")) {
            // Windows path
            return Paths.get(currentDirectory, "target/classes", filename);
        } else if (os.contains("mac") || os.contains("nix") || os.contains("nux")) {
            // MacOS or Unix path (Maven project structure)
            return Paths.get(currentDirectory, "target/classes", filename);
        } else {
            // Default path
            return Paths.get(currentDirectory, "src/main/resources", filename);
        }
    }
}
