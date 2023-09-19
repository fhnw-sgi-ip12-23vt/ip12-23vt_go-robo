package superMario;

import processing.core.PApplet;
import processing.core.PImage;

public class Coin extends AnimatedSprite {
	public Coin(PApplet pApplet, PImage img, float scale) {
		super(pApplet, img, scale);
		standNeutral = new PImage[4];
		standNeutral[0] = pApplet.loadImage("../../resources/img/gold1.png");
		standNeutral[1] = pApplet.loadImage("../../resources/img/gold2.png");
		standNeutral[2] = pApplet.loadImage("../../resources/img/gold3.png");
		standNeutral[3] = pApplet.loadImage("../../resources/img/gold4.png");
		currentImages = standNeutral;
	}
}
