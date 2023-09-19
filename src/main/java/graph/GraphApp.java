package graph;

import processing.core.PApplet;

public class GraphApp extends PApplet {

    private Node origin;
    private Node actual;
    public static void main(String[] args) {
        PApplet.main(new String[] { GraphApp.class.getName() });
    }

    public void settings() {
        size(640, 360);
    }

    public void setup() {
        fill(0, 255, 0);
        reset();
    }

    void reset() {
        origin= new Node((int)random(0,width), (int)random(0,height));
        actual= origin;
    }

    public void draw() {
        background(150);
        Node cur= origin;
        while (cur!=null){
           ellipse(cur.getX(), cur.getY(), 20,20);
           Node next= cur.getNext();
           if (next !=null){
               fill(255,0,0);
               line(cur.getX(), cur.getY(), next.getX(), next.getY());
           }
           cur= next;
        }
    }

    public void mousePressed() {
        Node n= new Node(mouseX, mouseY);
        actual.setNext(n);
        actual= n;
    }

    public void keyPressed() {
        if (key == 'd'){
            reset();
        }
    }
}
