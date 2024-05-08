package roomba.model;

import processing.core.PApplet;
import processing.core.PImage;
import roomba.controller.ImageLoader;

/**
 * Represents a goal in the "Roomba in Trouble" game.
 */
public class Goal extends AnimatedSprite {

    /**
     * Constructs a Goal object with the specified PApplet, image, and scale.
     *
     * @param pApplet The PApplet instance.
     * @param img     The PImage representing the goal's image.
     * @param scale   The scale factor for the image.
     */
    public Goal(PApplet pApplet, PImage img, float scale) {
        super(pApplet, img, scale);
        int frameRateScale = 5;
        standNeutral = new PImage[4 * frameRateScale];

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < frameRateScale; j++) {
                standNeutral[i * frameRateScale + j] =
                    ImageLoader.loadImage(pApplet, "img/goal/battery-frame" + i + ".png");
            }
        }

        currentImages = standNeutral;
    }
}
