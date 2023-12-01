package roomba;

import java.nio.file.FileSystems;
import java.nio.file.Path;

import processing.core.PApplet;
import processing.core.PImage;

/**
 * Utility class for loading images in the "Roomba in Trouble" game.
 */
public class ImageLoader {

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

        if (os.contains("win")) {
            // Windows path
            return FileSystems.getDefault().getPath("src\\main\\resources", filename);
        } else if (os.contains("nix") || os.contains("nux") || os.contains("mac")) {
            // Unix or MacOS path
            return FileSystems.getDefault().getPath("src/main/resources", filename);
        } else {
            // Default path
            return FileSystems.getDefault().getPath("src/main/resources", filename);
        }
    }
}
