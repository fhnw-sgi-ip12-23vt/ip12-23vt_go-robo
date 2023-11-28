package roomba;

import processing.core.PImage;

public class Player extends AnimatedSprite {
	GameField gamefield;
	String levelName;
	boolean hitObstacle, inPlace;
	PImage[] FaceLeft;
	PImage[] FaceRight;
	PImage[] FaceDown;
	PImage[] FaceUp;

	public Player(GameField gamefield, PImage img, float scale) {
		super(gamefield, img, scale);
		this.gamefield = gamefield;
		levelName = "";
		direction = Constants.NEUTRAL_FACING;
		hitObstacle = false;
		inPlace = true;

		//TODO Change resource img
		FaceLeft = new PImage[1];
		FaceLeft[0] = pApplet.loadImage("../../resources/img/roomba2-pixel-green-left.png");
		FaceRight = new PImage[1];
		FaceRight[0] = pApplet.loadImage("../../resources/img/roomba2-pixel-green-right.png");
		FaceDown = new PImage[1];
		FaceDown[0] = pApplet.loadImage("../../resources/img/roomba2-pixel-green-bottom.png");
		FaceUp = new PImage[1];
		FaceUp[0] = pApplet.loadImage("../../resources/img/roomba2-pixel-green-top.png");

		moveLeft = new PImage[2];
		moveLeft[0] = pApplet.loadImage("../../resources/img/roomba2-pixel-orange-left.png");
		moveLeft[1] = pApplet.loadImage("../../resources/img/roomba2-pixel-dark.png");
		moveRight = new PImage[2];
		moveRight[0] = pApplet.loadImage("../../resources/img/roomba2-pixel-orange-right.png");
		moveRight[1] = pApplet.loadImage("../../resources/img/roomba2-pixel-dark.png");
		moveUp = new PImage[2];
		moveUp[0] = pApplet.loadImage("../../resources/img/roomba2-pixel-orange-top.png");
		moveUp[1] = pApplet.loadImage("../../resources/img/roomba2-pixel-dark.png");
		moveDown = new PImage[2];
		moveDown[0] = pApplet.loadImage("../../resources/img/roomba2-pixel-orange-bottom.png");
		moveDown[1] = pApplet.loadImage("../../resources/img/roomba2-pixel-dark.png");
		currentImages = FaceRight;
	}


	@Override
	public void updateAnimation() {
		hitObstacle = gamefield.isHitObstacles(this, gamefield.obstacles);
		inPlace = change_x == 0 && change_y == 0;
		super.updateAnimation();
	}

	@Override
	public void selectDirection() {
		if (change_x > 0)
			direction = Constants.RIGHT_FACING;
		else if (change_x < 0)
			direction = Constants.LEFT_FACING;
		else if (change_y < 0)
			direction = Constants.DOWN_FACING;
		else if (change_y > 0)
			direction = Constants.UP_FACING;
	}

	@Override
	public void selectCurrentImages() {
		if (direction == Constants.RIGHT_FACING) {
			if (inPlace) {
				currentImages = FaceRight;
			} else
				currentImages = moveRight;
		} else if (direction == Constants.LEFT_FACING) {
			if (inPlace)
				currentImages = FaceLeft;
			else
				currentImages = moveLeft;
		} else if (direction == Constants.UP_FACING) {
			if (inPlace)
				currentImages = FaceUp;
			else
				currentImages = moveUp;
		} else if (direction == Constants.DOWN_FACING) {
			if (inPlace)
				currentImages = FaceDown;
			else
				currentImages = moveDown;
		}
	}
}
