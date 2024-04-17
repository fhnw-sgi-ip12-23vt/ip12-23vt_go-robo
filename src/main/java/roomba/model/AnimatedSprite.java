package roomba.model;

import processing.core.PApplet;
import processing.core.PImage;

import static roomba.model.Constants.*;

/**
 * Represents an animated sprite with various movement directions.
 */
public class AnimatedSprite extends Sprite {
    public int direction;
    protected PImage[] currentImages;
    protected PImage[] standNeutral;
    protected PImage[] moveLeft;
    protected PImage[] moveRight;
    protected PImage[] moveUp;
    protected PImage[] moveDown;
    private int index;
    private int frame;

    /**
     * Constructs an animated sprite.
     *
     * @param pApplet The processing applet.
     * @param img     The image for the sprite.
     * @param scale   The scale factor for the sprite.
     */
    public AnimatedSprite(PApplet pApplet, PImage img, float scale) {
        super(pApplet, img, scale);
        direction = NEUTRAL_FACING;
        index = 0;
        frame = 0;
    }

    /**
     * Updates the animation of the sprite.
     */
    public void updateAnimation() {
        frame++;
        selectDirection();

        if (frame % 5 == 0) {
            selectCurrentImages();
            advanceToNextImage();
        }
    }

    /**
     * Updates the animation of the sprite for one frame, regardless of the animation frame rate.
     */
    public void updateAnimationPlayer1Frame() {
        frame++;
        selectDirection();
        selectCurrentImages();
        advanceToNextImage();
    }

    /**
     * Selects the direction of the sprite based on its movement.
     */
    public void selectDirection() {
        if (changeX > 0) {
            direction = RIGHT_FACING;
        } else if (changeX < 0) {
            direction = LEFT_FACING;
        } else if (changeY < 0) {
            direction = DOWN_FACING;
        } else if (changeY > 0) {
            direction = UP_FACING;
        } else {
            direction = NEUTRAL_FACING;
        }
    }

    /**
     * Selects the current images based on the sprite's direction.
     */
    public void selectCurrentImages() {
        if (direction == RIGHT_FACING) {
            currentImages = moveRight;
        } else if (direction == LEFT_FACING) {
            currentImages = moveLeft;
        } else if (direction == UP_FACING) {
            currentImages = moveUp;
        } else if (direction == DOWN_FACING) {
            currentImages = moveDown;
        } else {
            currentImages = standNeutral;
        }
    }

    /**
     * Advances the sprite to the next image in the animation sequence.
     */
    public void advanceToNextImage() {
        index++;
        if (index >= currentImages.length) {
            index = 0;
        }
        image = currentImages[index];
    }
}
