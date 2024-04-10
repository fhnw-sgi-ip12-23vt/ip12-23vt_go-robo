package roomba.controller;

import roomba.model.Sprite;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles collision detection and resolution for sprites.
 */
public class CollisionHandler {
    private final float height;
    private final float width;
    private final float headerSize;

    public CollisionHandler(float height, float width, float headerSize) {
        this.height = height;
        this.width = width;
        this.headerSize = headerSize;
    }

    /**
     * Resolves collisions between the given sprite and a list of walls.
     *
     * @param s     The sprite for which collisions need to be resolved.
     * @param walls The list of wall sprites.
     */
    public void resolveObstaclesCollisions(Sprite s, List<Sprite> walls) {
        // Vertical collisions
        s.center_y += s.change_y;
        ArrayList<Sprite> colList = checkCollisionList(s, walls);
        if (!colList.isEmpty()) {
            Sprite collided = colList.get(0);
            if (s.change_y > 0) {
                s.setBottom(collided.getTop());
            } else if (s.change_y < 0) {
                s.setTop(collided.getBottom());
            }
            s.change_y = 0;
        } else {
            // Check if sprite is colliding with window borders vertically
            if (s.getTop() < headerSize) {
                s.setTop(headerSize);
                s.change_y = 0;
            } else if (s.getBottom() > height) {
                s.setBottom(height);
                s.change_y = 0;
            }
        }

        // Horizontal collisions
        s.center_x += s.change_x;
        colList = checkCollisionList(s, walls);
        if (!colList.isEmpty()) {
            Sprite collided = colList.get(0);
            if (s.change_x > 0) {
                s.setRight(collided.getLeft());
            } else if (s.change_x < 0) {
                s.setLeft(collided.getRight());
            }
            s.change_x = 0;
        } else {
            // Check if sprite is colliding with window borders horizontally
            if (s.getLeft() < 0) {
                s.setLeft(0);
                s.change_x = 0;
            } else if (s.getRight() > width) {
                s.setRight(width);
                s.change_x = 0;
            }
        }
    }

    /**
     * Checks if two sprites have collided.
     *
     * @param s1 The first sprite.
     * @param s2 The second sprite.
     * @return True if there is a collision, false otherwise.
     */
    private boolean checkCollision(Sprite s1, Sprite s2) {
        boolean noXOverlap = s1.getRight() <= s2.getLeft() || s1.getLeft() >= s2.getRight();
        boolean noYOverlap = s1.getBottom() <= s2.getTop() || s1.getTop() >= s2.getBottom();
        return !noXOverlap && !noYOverlap;
    }

    /**
     * Checks for collisions between a sprite and a list of sprites.
     *
     * @param s    The sprite to check for collisions.
     * @param list The list of sprites to check against.
     * @return A list of sprites with which a collision has occurred.
     */
    public ArrayList<Sprite> checkCollisionList(Sprite s, List<Sprite> list) {
        ArrayList<Sprite> collisionList = new ArrayList<>();
        for (Sprite p : list) {
            if (checkCollision(s, p)) {
                collisionList.add(p);
            }
        }
        return collisionList;
    }
}
