package roomba;

import processing.core.PImage;

/**
 * The Player class represents the player character in the "Roomba in Trouble"
 * game.
 * It extends the AnimatedSprite class and provides functionalities specific to
 * the player.
 */
public class Player extends AnimatedSprite {
	private boolean inPlace;
	private PImage[] FaceLeft;
	private PImage[] FaceRight;
	private PImage[] FaceDown;
	private PImage[] FaceUp;

	public boolean isInPlace() {
		return inPlace;
	}
	/**
	 * Constructs a new Player instance.
	 *
	 * @param gamefield The game field associated with the player.
	 * @param img       The image representing the player.
	 * @param scale     The scale factor for the player image.
	 */
	public Player(GameField gamefield, PImage img, float scale) {
		super(gamefield, img, scale);
		direction = Constants.RIGHT_FACING;
		inPlace = true;

		// Loading images for different directions and animations
		FaceLeft = new PImage[1];
		FaceLeft[0] = ImageLoader.loadImage(pApplet, "img/roomba2-pixel-green-left.png");
		FaceRight = new PImage[1];
		FaceRight[0] = ImageLoader.loadImage(pApplet, "img/roomba2-pixel-green-right.png");
		FaceDown = new PImage[1];
		FaceDown[0] = ImageLoader.loadImage(pApplet, "img/roomba2-pixel-green-down.png");
		FaceUp = new PImage[1];
		FaceUp[0] = ImageLoader.loadImage(pApplet, "img/roomba2-pixel-green-up.png");

		moveLeft = new PImage[2];
		moveLeft[0] = ImageLoader.loadImage(pApplet, "img/roomba2-pixel-orange-left.png");
		moveLeft[1] = ImageLoader.loadImage(pApplet, "img/roomba2-pixel-dark.png");
		moveRight = new PImage[2];
		moveRight[0] = ImageLoader.loadImage(pApplet, "img/roomba2-pixel-orange-right.png");
		moveRight[1] = ImageLoader.loadImage(pApplet, "img/roomba2-pixel-dark.png");
		moveUp = new PImage[2];
		moveUp[0] = ImageLoader.loadImage(pApplet, "img/roomba2-pixel-orange-up.png");
		moveUp[1] = ImageLoader.loadImage(pApplet, "img/roomba2-pixel-dark.png");
		moveDown = new PImage[2];
		moveDown[0] = ImageLoader.loadImage(pApplet, "img/roomba2-pixel-orange-down.png");
		moveDown[1] = ImageLoader.loadImage(pApplet, "img/roomba2-pixel-dark.png");
		currentImages = FaceRight; // Initial direction
	}

	/**
	 * Updates the animation and inPlace status of the player.
	 */
	@Override
	public void updateAnimation() {
		inPlace = change_x == 0 && change_y == 0;
		super.updateAnimation();
	}
	/**
	 * Updates the animation for 1 Frame and inPlace status of the player.
	 */
	public void updateAnimationFrame1() {
		inPlace = change_x == 0 && change_y == 0;
		super.updateAnimationPlayer1Frame();
	}

	/**
	 * Selects the current direction of the player based on the change in
	 * coordinates.
	 */
	@Override
	public void selectDirection() {
		if (change_x > 0)
			direction = Constants.RIGHT_FACING;
		else if (change_x < 0)
			direction = Constants.LEFT_FACING;
		else if (change_y < 0)
			direction = Constants.UP_FACING;
		else if (change_y > 0)
			direction = Constants.DOWN_FACING;
	}

	/**
	 * Selects the current set of images based on the player's direction and
	 * movement status.
	 */
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
