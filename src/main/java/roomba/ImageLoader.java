package roomba;

import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;

import processing.core.PApplet;
import processing.core.PImage;

/**
 * Utility class for loading images in the "Roomba in Trouble" game.
 */
public class ImageLoader {
    private static final Logger logger = Logger.getLogger(ImageLoader.class.getName());

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
        logger.info("OS: " + os);
        logger.info("FilePath: "+FileSystems.getDefault().getPath(currentDirectory, "target/classes", filename).toString());

        if (os.contains("win")) {
            // Windows path
            logger.warning("Windows path");
            return FileSystems.getDefault().getPath(currentDirectory, "target/classes", filename);
        } else if (os.contains("mac")) {
            // MacOS path
            logger.warning("MacOS path");
            return FileSystems.getDefault().getPath("src/main/resources", filename);
        } else if (os.contains("nix") || os.contains("nux")) {
            // Unix path (Maven project structure)
            logger.warning("Unix path");
            return FileSystems.getDefault().getPath(currentDirectory, "target/classes", filename);
        } else {
            // Default path
            logger.warning("Default path");
            return FileSystems.getDefault().getPath("src/main/resources", filename);
        }
    }
}
