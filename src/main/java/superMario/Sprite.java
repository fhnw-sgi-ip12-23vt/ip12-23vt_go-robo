package superMario;

import processing.core.PApplet;
import processing.core.PImage;

public class Sprite {
	PApplet pApplet;
	PImage image;
	float center_x, center_y;
	float change_x, change_y;
	float w, h;

	public Sprite(PApplet pApplet, String filename, float scale, float x, float y) {
		this.pApplet = pApplet;
		image = pApplet.loadImage(filename);
		w = image.width * scale;
		h = image.height * scale;
		center_x = x;
		center_y = y;
		change_x = 0;
		change_y = 0;
	}

	public Sprite(PApplet pApplet, String filename, float scale) {
		this(pApplet, filename, scale, 0, 0);
	}

	public Sprite(PApplet pApplet, PImage img, float scale) {
		this.pApplet = pApplet;
		image = img;
		w = image.width * scale;
		h = image.height * scale;
		center_x = 0;
		center_y = 0;
		change_x = 0;
		change_y = 0;
	}

	public void display() {
		pApplet.image(image, center_x, center_y, w, h);
	}

	public void update() {
		center_x += change_x;
		center_y += change_y;
	}

	void setLeft(float left) {
		center_x = left + w / 2;
	}

	float getLeft() {
		return center_x - w / 2;
	}

	void setRight(float right) {
		center_x = right - w / 2;
	}

	float getRight() {
		return center_x + w / 2;
	}

	void setTop(float top) {
		center_y = top + h / 2;
	}

	float getTop() {
		return center_y - h / 2;
	}

	void setBottom(float bottom) {
		center_y = bottom - h / 2;
	}

	float getBottom() {
		return center_y + h / 2;
	}
}
