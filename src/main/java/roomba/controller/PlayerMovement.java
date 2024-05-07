package roomba.controller;

import roomba.model.Player;

import static roomba.model.Constants.*;

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
        case RIGHT_FACING -> {
            switch (inpDirection) {
            case RIGHT_FACING -> moveDown();
            case LEFT_FACING -> moveUp();
            case UP_FACING -> moveRight();
            case DOWN_FACING -> moveLeft();
            }
        }
        case LEFT_FACING -> {
            switch (inpDirection) {
            case RIGHT_FACING -> moveUp();
            case LEFT_FACING -> moveDown();
            case UP_FACING -> moveLeft();
            case DOWN_FACING -> moveRight();
            }
        }
        case UP_FACING -> {
            switch (inpDirection) {
            case RIGHT_FACING -> moveRight();
            case LEFT_FACING -> moveLeft();
            case UP_FACING -> moveUp();
            case DOWN_FACING -> moveDown();
            }
        }
        case DOWN_FACING -> {
            switch (inpDirection) {
            case RIGHT_FACING -> moveLeft();
            case LEFT_FACING -> moveRight();
            case UP_FACING -> moveDown();
            case DOWN_FACING -> moveUp();
            }
        }
        default -> {
        }
        // Handle unexpected input direction
        }
    }
    /**
     * Turn the player character based on the input direction and current facing direction.
     *
     * @param player       The player object to be moved.
     * @param inpDirection The input direction indicating the desired movement direction.
     */
    public void turnPlayer(Player player, int inpDirection) {
        this.player = player;

        switch (player.direction) {
        case RIGHT_FACING -> {
            switch (inpDirection) {
            case RIGHT_FACING -> player.direction = DOWN_FACING;
            case LEFT_FACING -> player.direction = UP_FACING;
            }
        }
        case LEFT_FACING -> {
            switch (inpDirection) {
            case RIGHT_FACING -> player.direction = UP_FACING;
            case LEFT_FACING -> player.direction = DOWN_FACING;
            }
        }
        case UP_FACING -> {
            switch (inpDirection) {
            case RIGHT_FACING -> player.direction = RIGHT_FACING;
            case LEFT_FACING -> player.direction = LEFT_FACING;
            }
        }
        case DOWN_FACING -> {
            switch (inpDirection) {
            case RIGHT_FACING -> player.direction = LEFT_FACING;
            case LEFT_FACING -> player.direction = RIGHT_FACING;
            }
        }
        default -> {
        }
        // Handle unexpected input direction
        }
    }
    private void moveLeft() {
        player.changeY = 0;
        player.changeX = -MOVE_SPEED;
    }

    private void moveRight() {
        player.changeY = 0;
        player.changeX = MOVE_SPEED;
    }

    private void moveUp() {
        player.changeY = -MOVE_SPEED;
        player.changeX = 0;
    }

    private void moveDown() {
        player.changeY = MOVE_SPEED;
        player.changeX = 0;
    }




}
