package roomba;

import java.util.ArrayList;
import java.util.List;

public class CollisionHandler {

    public boolean isHitObstacles(Sprite s, List<Sprite> walls) {
        ArrayList<Sprite> col_list = checkCollisionList(s, walls);
        return !col_list.isEmpty();
    }

    public void resolveObstaclesCollisions(Sprite s, List<Sprite> walls) {
        s.center_y += s.change_y;
        ArrayList<Sprite> col_list = checkCollisionList(s, walls);
        if (!col_list.isEmpty()) {
            Sprite collided = col_list.get(0);
            if (s.change_y > 0) {
                s.setBottom(collided.getTop());
             
            } else if (s.change_y < 0) {
                s.setTop(collided.getBottom());
             
            }
            s.change_y = 0;
        }

        s.center_x += s.change_x;
        col_list = checkCollisionList(s, walls);
        if (!col_list.isEmpty()) {
            Sprite collided = col_list.get(0);
            if (s.change_x > 0) {
                s.setRight(collided.getLeft());
             
            } else if (s.change_x < 0) {
                s.setLeft(collided.getRight());
             
            }
            s.change_x = 0;
        }
    }

    boolean checkCollision(Sprite s1, Sprite s2) {
        boolean noXOverlap = s1.getRight() <= s2.getLeft() || s1.getLeft() >= s2.getRight();
        boolean noYOverlap = s1.getBottom() <= s2.getTop() || s1.getTop() >= s2.getBottom();
        return !noXOverlap && !noYOverlap;
    }

    public ArrayList<Sprite> checkCollisionList(Sprite s, List<Sprite> list) {
        ArrayList<Sprite> collision_list = new ArrayList<>();
        for (Sprite p : list) {
            if (checkCollision(s, p))
                collision_list.add(p);
        }
        return collision_list;
    }


}
