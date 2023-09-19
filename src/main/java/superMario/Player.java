package superMario;

import processing.core.PImage;

public class Player extends AnimatedSprite {
	PlatformerS platformer;
	int lives;
	boolean onPlatform, inPlace;
	PImage[] standLeft;
	PImage[] standRight;
	PImage[] jumpLeft;
	PImage[] jumpRight;

	public Player(PlatformerS platformer, PImage img, float scale) {
		super(platformer, img, scale);
		this.platformer = platformer;
		lives = 3;
		direction = Constants.RIGHT_FACING;
		onPlatform = false;
		inPlace = true;
		standLeft = new PImage[1];
		standLeft[0] = pApplet.loadImage("../../resources/img/player_stand_left.png");
		standRight = new PImage[1];
		standRight[0] = pApplet.loadImage("../../resources/img/player_stand_right.png");
		jumpLeft = new PImage[1];
		jumpLeft[0] = pApplet.loadImage("../../resources/img/player_jump_left.png");
		jumpRight = new PImage[1];
		jumpRight[0] = pApplet.loadImage("../../resources/img/player_jump_right.png");
		moveLeft = new PImage[2];
		moveLeft[0] = pApplet.loadImage("../../resources/img/player_walk_left1.png");
		moveLeft[1] = pApplet.loadImage("../../resources/img/player_walk_left2.png");
		moveRight = new PImage[2];
		moveRight[0] = pApplet.loadImage("../../resources/img/player_walk_right1.png");
		moveRight[1] = pApplet.loadImage("../../resources/img/player_walk_right2.png");
		currentImages = standRight;
	}

	@Override
	public void updateAnimation() {
		onPlatform = platformer.isOnPlatforms(this, platformer.platforms);
		inPlace = change_x == 0 && change_y == 0;
		super.updateAnimation();
	}

	@Override
	public void selectDirection() {
		if (change_x > 0)
			direction = Constants.RIGHT_FACING;
		else if (change_x < 0)
			direction = Constants.LEFT_FACING;
	}

	@Override
	public void selectCurrentImages() {
		if (direction == Constants.RIGHT_FACING) {
			if (inPlace) {
				currentImages = standRight;
			} else if (!onPlatform)
				currentImages = jumpRight;
			else
				currentImages = moveRight;
		} else if (direction == Constants.LEFT_FACING) {
			if (inPlace)
				currentImages = standLeft;
			else if (!onPlatform)
				currentImages = jumpLeft;
			else
				currentImages = moveLeft;
		}
	}
}
