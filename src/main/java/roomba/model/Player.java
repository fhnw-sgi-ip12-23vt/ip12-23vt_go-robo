package roomba.model;

import processing.core.PImage;
import roomba.controller.ImageLoader;
import roomba.view.GameField;

/**
 * The Player class represents the player character in the "Roomba in Trouble"
 * game.
 * It extends the AnimatedSprite class and provides functionalities specific to
 * the player.
 */
public class Player extends AnimatedSprite {
    private boolean inPlace;
    private final PImage[] faceLeft;
    private final PImage[] faceRight;
    private final PImage[] faceDown;
    private final PImage[] faceUp;

    /**
     * Constructs a new Player instance.
     *
     * @param gamefield The game field associated with the player.
     * @param img       The image representing the player.
     * @param scale     The scale factor for the player image.
     */
    public Player(GameField gamefield, PImage img, float scale) {
        super(gamefield, img, scale);
        direction = Constants.RIGHT_FACING;
        inPlace = true;

        // Loading images for different directions and animations
        faceLeft = new PImage[1];
        faceLeft[0] = ImageLoader.loadImage(pApplet, "img/roomba2-pixel-green-left.png");
        faceRight = new PImage[1];
        faceRight[0] = ImageLoader.loadImage(pApplet, "img/roomba2-pixel-green-right.png");
        faceDown = new PImage[1];
        faceDown[0] = ImageLoader.loadImage(pApplet, "img/roomba2-pixel-green-down.png");
        faceUp = new PImage[1];
        faceUp[0] = ImageLoader.loadImage(pApplet, "img/roomba2-pixel-green-up.png");

        moveLeft = new PImage[2];
        moveLeft[0] = ImageLoader.loadImage(pApplet, "img/roomba2-pixel-orange-left.png");
        moveLeft[1] = ImageLoader.loadImage(pApplet, "img/roomba2-pixel-dark.png");
        moveRight = new PImage[2];
        moveRight[0] = ImageLoader.loadImage(pApplet, "img/roomba2-pixel-orange-right.png");
        moveRight[1] = ImageLoader.loadImage(pApplet, "img/roomba2-pixel-dark.png");
        moveUp = new PImage[2];
        moveUp[0] = ImageLoader.loadImage(pApplet, "img/roomba2-pixel-orange-up.png");
        moveUp[1] = ImageLoader.loadImage(pApplet, "img/roomba2-pixel-dark.png");
        moveDown = new PImage[2];
        moveDown[0] = ImageLoader.loadImage(pApplet, "img/roomba2-pixel-orange-down.png");
        moveDown[1] = ImageLoader.loadImage(pApplet, "img/roomba2-pixel-dark.png");
        currentImages = faceRight; // Initial direction
    }

    public boolean isInPlace() {
        return inPlace;
    }

    /**
     * Updates the animation and inPlace status of the player.
     */
    @Override
    public void updateAnimation() {
        inPlace = changeX == 0 && changeY == 0;
        super.updateAnimation();
    }

    /**
     * Updates the animation for 1 Frame and inPlace status of the player.
     */
    public void updateAnimationFrame1() {
        inPlace = changeX == 0 && changeY == 0;
        super.updateAnimationPlayer1Frame();
    }

    /**
     * Selects the current direction of the player based on the change in
     * coordinates.
     */
    @Override
    public void selectDirection() {
        if (changeX > 0) {
            direction = Constants.RIGHT_FACING;
        } else if (changeX < 0) {
            direction = Constants.LEFT_FACING;
        } else if (changeY < 0) {
            direction = Constants.UP_FACING;
        } else if (changeY > 0) {
            direction = Constants.DOWN_FACING;
        }
    }

    /**
     * Selects the current set of images based on the player's direction and
     * movement status.
     */
    @Override
    public void selectCurrentImages() {
        if (direction == Constants.RIGHT_FACING) {
            if (inPlace) {
                currentImages = faceRight;
            } else {
                currentImages = moveRight;
            }
        } else if (direction == Constants.LEFT_FACING) {
            if (inPlace) {
                currentImages = faceLeft;
            } else {
                currentImages = moveLeft;
            }
        } else if (direction == Constants.UP_FACING) {
            if (inPlace) {
                currentImages = faceUp;
            } else {
                currentImages = moveUp;
            }
        } else if (direction == Constants.DOWN_FACING) {
            if (inPlace) {
                currentImages = faceDown;
            } else {
                currentImages = moveDown;
            }
        }
    }
}
