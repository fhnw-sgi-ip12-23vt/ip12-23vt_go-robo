package roomba;

import processing.core.PApplet;
import processing.core.PImage;

public class Goal extends AnimatedSprite {
	public Goal(PApplet pApplet, PImage img, float scale) {
		super(pApplet, img, scale);
		standNeutral = new PImage[4];
        //TODO change img
		standNeutral[0] = pApplet.loadImage("../../resources/img/charger.png");
		standNeutral[1] = pApplet.loadImage("../../resources/img/charger.png");
		standNeutral[2] = pApplet.loadImage("../../resources/img/charger.png");
		standNeutral[3] = pApplet.loadImage("../../resources/img/charger.png");
		currentImages = standNeutral;
	}
}
