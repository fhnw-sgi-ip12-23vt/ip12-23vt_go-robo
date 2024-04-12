package roomba.model;

import processing.core.PApplet;
import processing.core.PImage;

/**
 * The Sprite class represents a basic game sprite with position, size, and movement properties.
 */
public class Sprite {
    public float centerX;
    public float centerY;
    public float changeX;
    public float changeY;
    protected PApplet pApplet;
    protected PImage image;
    /**
     * Constructs a Sprite object with the specified image file, scale, and initial position.
     *
     * @param pApplet  The PApplet instance associated with the sprite.
     * @param filename The filename of the image.
     * @param scale    The scale factor for the image.
     * @param x        The initial x-coordinate of the sprite's center.
     * @param y        The initial y-coordinate of the sprite's center.
     */
    public Sprite(PApplet pApplet, String filename, float scale, float x, float y) {
        this.pApplet = pApplet;
        image = pApplet.loadImage(filename);
        w = image.width * scale;
        h = image.height * scale;
        centerX = x;
        centerY = y;
        changeX = 0;
        changeY = 0;
    }

    private float w, h;

    /**
     * Constructs a Sprite object with the specified image file and scale, with the initial position at (0,0).
     *
     * @param pApplet  The PApplet instance associated with the sprite.
     * @param filename The filename of the image.
     * @param scale    The scale factor for the image.
     */
    public Sprite(PApplet pApplet, String filename, float scale) {
        this(pApplet, filename, scale, 0, 0);
    }

    /**
     * Constructs a Sprite object with the specified PImage and scale, with the initial position at (0,0).
     *
     * @param pApplet The PApplet instance associated with the sprite.
     * @param img     The PImage representing the sprite.
     * @param scale   The scale factor for the image.
     */
    public Sprite(PApplet pApplet, PImage img, float scale) {
        this.pApplet = pApplet;
        image = img;
        w = image.width * scale;
        h = image.height * scale;
        centerX = 0;
        centerY = 0;
        changeX = 0;
        changeY = 0;
    }

    /**
     * Displays the sprite on the PApplet canvas.
     */
    public void display() {
        pApplet.image(image, centerX, centerY, w, h);
    }

    /**
     * Returns the x-coordinate of the left edge of the sprite.
     *
     * @return The x-coordinate of the left edge.
     */
    public float getLeft() {
        return centerX - w / 2;
    }

    /**
     * Sets the left edge of the sprite to the specified x-coordinate.
     *
     * @param left The x-coordinate of the left edge.
     */
    public void setLeft(float left) {
        centerX = left + w / 2;
    }

    /**
     * Returns the x-coordinate of the right edge of the sprite.
     *
     * @return The x-coordinate of the right edge.
     */
    public float getRight() {
        return centerX + w / 2;
    }

    /**
     * Sets the right edge of the sprite to the specified x-coordinate.
     *
     * @param right The x-coordinate of the right edge.
     */
    public void setRight(float right) {
        centerX = right - w / 2;
    }

    /**
     * Returns the y-coordinate of the top edge of the sprite.
     *
     * @return The y-coordinate of the top edge.
     */
    public float getTop() {
        return centerY - h / 2;
    }

    /**
     * Sets the top edge of the sprite to the specified y-coordinate.
     *
     * @param top The y-coordinate of the top edge.
     */
    public void setTop(float top) {
        centerY = top + h / 2;
    }

    /**
     * Returns the y-coordinate of the bottom edge of the sprite.
     *
     * @return The y-coordinate of the bottom edge.
     */
    public float getBottom() {
        return centerY + h / 2;
    }

    /**
     * Sets the bottom edge of the sprite to the specified y-coordinate.
     *
     * @param bottom The y-coordinate of the bottom edge.
     */
    public void setBottom(float bottom) {
        centerY = bottom - h / 2;
    }
}
