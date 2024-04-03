package roomba.controller;

import roomba.model.Constants;
import roomba.model.Player;

public class PlayerMovement {
    private Player player;
    public void movePlayer(Player player, int inpDirection){
        this.player = player;

        //Player is facing right
        if (player.direction == Constants.RIGHT_FACING){
            switch (inpDirection) {
                case Constants.RIGHT_FACING -> moveDown();
                case Constants.LEFT_FACING -> moveUp();
                case Constants.UP_FACING -> moveRight();
                case Constants.DOWN_FACING -> moveLeft();
            }
        }
        //Player is facing left
        if (player.direction == Constants.LEFT_FACING){
            switch (inpDirection) {
                case Constants.RIGHT_FACING -> moveUp();
                case Constants.LEFT_FACING -> moveDown();
                case Constants.UP_FACING -> moveLeft();
                case Constants.DOWN_FACING -> moveRight();
            }
        }
        //Player is facing up
        if (player.direction == Constants.UP_FACING){
            switch (inpDirection) {
                case Constants.RIGHT_FACING -> moveRight();
                case Constants.LEFT_FACING -> moveLeft();
                case Constants.UP_FACING -> moveUp();
                case Constants.DOWN_FACING -> moveDown();
            }
        }
        //Player is facing down
        if (player.direction == Constants.DOWN_FACING){
            switch (inpDirection) {
                case Constants.RIGHT_FACING -> moveLeft();
                case Constants.LEFT_FACING -> moveRight();
                case Constants.UP_FACING -> moveDown();
                case Constants.DOWN_FACING -> moveUp();
            }
        }
    }

    private void moveLeft(){
        player.change_y = 0;
        player.change_x = -Constants.MOVE_SPEED;
    }
    private void moveRight(){
        player.change_y = 0;
        player.change_x = Constants.MOVE_SPEED;
    }
    private void moveUp(){
        player.change_y = -Constants.MOVE_SPEED;
        player.change_x = 0;
    }
    private void moveDown(){
        player.change_y = Constants.MOVE_SPEED;
        player.change_x = 0;
    }

}