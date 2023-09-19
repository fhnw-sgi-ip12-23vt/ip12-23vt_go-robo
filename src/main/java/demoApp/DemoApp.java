package demoApp;

import processing.core.PApplet;

/*Author: Michael Job*/
public class DemoApp extends PApplet {

    public static void main(String[] args) {
        PApplet.main(new String[] { DemoApp.class.getName() });
    }

    public float x = 100;

    public void settings(){
        size(500,500);
    }

    public void setup(){
        background(0);
        text("Hello Processing in IntelliJ IDEA", 90, 100);
    }

    public void draw(){
        stroke(255,30,30);
        line(x, x*1.25F,x*3,x*1.25F);
        x++;
    }
}
