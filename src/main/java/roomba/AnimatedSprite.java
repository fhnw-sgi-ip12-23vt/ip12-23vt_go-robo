package roomba;

import processing.core.PApplet;
import processing.core.PImage;

/**
 * Represents an animated sprite with various movement directions.
 */
public class AnimatedSprite extends Sprite {
    PImage[] currentImages;
    PImage[] standNeutral;
    PImage[] moveLeft;
    PImage[] moveRight;
    PImage[] moveUp;
    PImage[] moveDown;
    int direction;
    int index;
    int frame;

    /**
     * Constructs an animated sprite.
     *
     * @param pApplet The processing applet.
     * @param img     The image for the sprite.
     * @param scale   The scale factor for the sprite.
     */
    public AnimatedSprite(PApplet pApplet, PImage img, float scale) {
        super(pApplet, img, scale);
        direction = Constants.NEUTRAL_FACING;
        index = 0;
        frame = 0;
    }

    /**
     * Updates the animation of the sprite.
     */
    public void updateAnimation() {
        frame++;
        if (frame % 5 == 0) {
            selectDirection();
            selectCurrentImages();
            advanceToNextImage();
        }
    }

    /**
     * Selects the direction of the sprite based on its movement.
     */
    public void selectDirection() {
        if (change_x > 0)
            direction = Constants.RIGHT_FACING;
        else if (change_x < 0)
            direction = Constants.LEFT_FACING;
        else if (change_y < 0)
            direction = Constants.DOWN_FACING;
        else if (change_y > 0)
            direction = Constants.UP_FACING;
        else
            direction = Constants.NEUTRAL_FACING;
    }

    /**
     * Selects the current images based on the sprite's direction.
     */
    public void selectCurrentImages() {
        if (direction == Constants.RIGHT_FACING)
            currentImages = moveRight;
        else if (direction == Constants.LEFT_FACING)
            currentImages = moveLeft;
        else if (direction == Constants.UP_FACING)
            currentImages = moveUp;
        else if (direction == Constants.DOWN_FACING)
            currentImages = moveDown;
        else
            currentImages = standNeutral;
    }

    /**
     * Advances the sprite to the next image in the animation sequence.
     */
    public void advanceToNextImage() {
        index++;
        if (index >= currentImages.length)
            index = 0;
        image = currentImages[index];
    }
}
