package roomba;

import processing.core.PApplet;
import processing.core.PImage;

public class AnimatedSprite extends Sprite {
	PImage[] currentImages;
	PImage[] standNeutral;
	PImage[] moveLeft;
	PImage[] moveRight;
	PImage[] moveUp;
	PImage[] moveDown;
	int direction;
	int index;
	int frame;

	public AnimatedSprite(PApplet pApplet, PImage img, float scale) {
		super(pApplet, img, scale);
		direction = Constants.NEUTRAL_FACING;
		index = 0;
		frame = 0;
	}

	public void updateAnimation() {
		frame++;
		if (frame % 5 == 0) {
			selectDirection();
			selectCurrentImages();
			advanceToNextImage();
		}
	}

	public void selectDirection() {
		if (change_x > 0)
			direction = Constants.RIGHT_FACING;
		else if (change_x < 0)
			direction = Constants.LEFT_FACING;
		else if (change_y < 0)
			direction = Constants.DOWN_FACING;
		else if (change_y > 0)
			direction = Constants.UP_FACING;
		else
			direction = Constants.NEUTRAL_FACING;
	}

	public void selectCurrentImages() {
		if (direction == Constants.RIGHT_FACING)
			currentImages = moveRight;
		else if (direction == Constants.LEFT_FACING)
			currentImages = moveLeft;
		else if (direction == Constants.UP_FACING)
			currentImages = moveUp;
		else if (direction == Constants.DOWN_FACING)
			currentImages = moveDown;
		else
			currentImages = standNeutral;
	}

	public void advanceToNextImage() {
		index++;
		if (index >= currentImages.length)
			index = 0;
		image = currentImages[index];
	}
}
