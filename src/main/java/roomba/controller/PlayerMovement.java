package roomba.controller;

import roomba.model.Constants;
import roomba.model.Player;

/**
 * The PlayerMovement class represents the logic for moving the player character in the Roomba application.
 * It determines the direction in which the player should move based on input direction and current facing direction.
 */
public class PlayerMovement {
    private Player player;

    /**
     * Move the player character based on the input direction and current facing direction.
     *
     * @param player       The player object to be moved.
     * @param inpDirection The input direction indicating the desired movement direction.
     */
    public void movePlayer(Player player, int inpDirection) {
        this.player = player;

        switch (player.direction) {
        case Constants.RIGHT_FACING -> {
            switch (inpDirection) {
            case Constants.RIGHT_FACING -> moveDown();
            case Constants.LEFT_FACING -> moveUp();
            case Constants.UP_FACING -> moveRight();
            case Constants.DOWN_FACING -> moveLeft();
            }
        }
        case Constants.LEFT_FACING -> {
            switch (inpDirection) {
            case Constants.RIGHT_FACING -> moveUp();
            case Constants.LEFT_FACING -> moveDown();
            case Constants.UP_FACING -> moveLeft();
            case Constants.DOWN_FACING -> moveRight();
            }
        }
        case Constants.UP_FACING -> {
            switch (inpDirection) {
            case Constants.RIGHT_FACING -> moveRight();
            case Constants.LEFT_FACING -> moveLeft();
            case Constants.UP_FACING -> moveUp();
            case Constants.DOWN_FACING -> moveDown();
            }
        }
        case Constants.DOWN_FACING -> {
            switch (inpDirection) {
            case Constants.RIGHT_FACING -> moveLeft();
            case Constants.LEFT_FACING -> moveRight();
            case Constants.UP_FACING -> moveDown();
            case Constants.DOWN_FACING -> moveUp();
            }
        }
        default -> {
        }
        // Handle unexpected input direction
        }
    }


    private void moveLeft() {
        player.changeY = 0;
        player.changeX = -Constants.MOVE_SPEED;
    }

    private void moveRight() {
        player.changeY = 0;
        player.changeX = Constants.MOVE_SPEED;
    }

    private void moveUp() {
        player.changeY = -Constants.MOVE_SPEED;
        player.changeX = 0;
    }

    private void moveDown() {
        player.changeY = Constants.MOVE_SPEED;
        player.changeX = 0;
    }

}
