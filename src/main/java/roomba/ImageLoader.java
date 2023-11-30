
package roomba;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import processing.core.PApplet;
import processing.core.PImage;

public class ImageLoader {

    public static PImage loadImage(PApplet applet, String filename) {
        Path imagePath = getImagePath(filename);
        return applet.loadImage(imagePath.toString());
    }

    private static Path getImagePath(String filename) {
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
