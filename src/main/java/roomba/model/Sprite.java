package roomba.model;

import processing.core.PApplet;
import processing.core.PImage;

/**
 * The Sprite class represents a basic game sprite with position, size, and movement properties.
 */
public class Sprite {
    public float center_x;
    public float center_y;
    public float change_x;
    public float change_y;
    protected PApplet pApplet;
    protected PImage image;
    protected float w, h;

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
        center_x = x;
        center_y = y;
        change_x = 0;
        change_y = 0;
    }

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
        center_x = 0;
        center_y = 0;
        change_x = 0;
        change_y = 0;
    }

    /**
     * Displays the sprite on the PApplet canvas.
     */
    public void display() {
        pApplet.image(image, center_x, center_y, w, h);
    }

    /**
     * Returns the x-coordinate of the left edge of the sprite.
     *
     * @return The x-coordinate of the left edge.
     */
    public float getLeft() {
        return center_x - w / 2;
    }

    /**
     * Sets the left edge of the sprite to the specified x-coordinate.
     *
     * @param left The x-coordinate of the left edge.
     */
    public void setLeft(float left) {
        center_x = left + w / 2;
    }

    /**
     * Returns the x-coordinate of the right edge of the sprite.
     *
     * @return The x-coordinate of the right edge.
     */
    public float getRight() {
        return center_x + w / 2;
    }

    /**
     * Sets the right edge of the sprite to the specified x-coordinate.
     *
     * @param right The x-coordinate of the right edge.
     */
    public void setRight(float right) {
        center_x = right - w / 2;
    }

    /**
     * Returns the y-coordinate of the top edge of the sprite.
     *
     * @return The y-coordinate of the top edge.
     */
    public float getTop() {
        return center_y - h / 2;
    }

    /**
     * Sets the top edge of the sprite to the specified y-coordinate.
     *
     * @param top The y-coordinate of the top edge.
     */
    public void setTop(float top) {
        center_y = top + h / 2;
    }

    /**
     * Returns the y-coordinate of the bottom edge of the sprite.
     *
     * @return The y-coordinate of the bottom edge.
     */
    public float getBottom() {
        return center_y + h / 2;
    }

    /**
     * Sets the bottom edge of the sprite to the specified y-coordinate.
     *
     * @param bottom The y-coordinate of the bottom edge.
     */
    public void setBottom(float bottom) {
        center_y = bottom - h / 2;
    }
}
