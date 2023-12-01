package roomba;

import processing.core.PApplet;
import processing.core.PImage;

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
        standNeutral = new PImage[4];

        standNeutral[0] = ImageLoader.loadImage(pApplet, "img/goal/battery-frame0.png");
        standNeutral[1] = ImageLoader.loadImage(pApplet, "img/goal/battery-frame1.png");
        standNeutral[2] = ImageLoader.loadImage(pApplet, "img/goal/battery-frame2.png");
        standNeutral[3] = ImageLoader.loadImage(pApplet, "img/goal/battery-frame3.png");

        currentImages = standNeutral;
    }
}
