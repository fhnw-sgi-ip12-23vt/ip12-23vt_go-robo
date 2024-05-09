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
     * @param s      The sprite for which collisions need to be resolved.
     * @param walls  The list of wall sprites.
     * @param border distance between screen border and game border
     */
    public void resolveObstaclesCollisions(Sprite s, List<Sprite> walls, int border) {

        int gridcorrection = 1;

        if (border != 0) {
            gridcorrection = border;
        }

        // Vertical collisions
        s.centerY += s.changeY;
        ArrayList<Sprite> colList = checkCollisionList(s, walls);
        if (!colList.isEmpty()) {
            Sprite collided = colList.get(0);
            if (s.changeY > 0) {
                s.setBottom(collided.getTop());
            } else if (s.changeY < 0) {
                s.setTop(collided.getBottom());
            }
            s.changeY = 0;
        } else {
            // Check if sprite is colliding with window borders vertically
            if (s.getTop() < headerSize + border) {
                s.setTop(headerSize + border);
                s.changeY = 0;
            } else if (s.getBottom() > height - border - height % gridcorrection) {
                s.setBottom(height - border - height % gridcorrection);
                s.changeY = 0;
            }
        }

        // Horizontal collisions
        s.centerX += s.changeX;
        colList = checkCollisionList(s, walls);
        if (!colList.isEmpty()) {
            Sprite collided = colList.get(0);
            if (s.changeX > 0) {
                s.setRight(collided.getLeft());
            } else if (s.changeX < 0) {
                s.setLeft(collided.getRight());
            }
            s.changeX = 0;
        } else {
            // Check if sprite is colliding with window borders horizontally
            if (s.getLeft() < border) {
                s.setLeft(border);
                s.changeX = 0;
            } else if (s.getRight() > width - border - width % gridcorrection) {
                s.setRight(width - border - width % gridcorrection);
                s.changeX = 0;
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
