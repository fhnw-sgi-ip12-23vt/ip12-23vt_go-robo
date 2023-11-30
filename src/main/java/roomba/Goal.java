package roomba;

import processing.core.PApplet;
import processing.core.PImage;

public class Goal extends AnimatedSprite {
	public Goal(PApplet pApplet, PImage img, float scale) {
		super(pApplet, img, scale);
		standNeutral = new PImage[4];
		
		standNeutral[0] = ImageLoader.loadImage(pApplet, "img/goal/battery-frame0.png");
        standNeutral[1] = ImageLoader.loadImage(pApplet, "img/goal/battery-frame1.png");
        standNeutral[2] = ImageLoader.loadImage(pApplet, "img/goal/battery-frame2.png");
        standNeutral[3] = ImageLoader.loadImage(pApplet, "img/goal/battery-frame3.png");
        
		currentImages = standNeutral;
	}
}
