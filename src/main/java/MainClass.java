import demoApp.DemoApp;
import processing.core.PApplet;

/*Author: Michael Job*/
public class MainClass extends PApplet {
    public static void main(String[] args){
        //define which ProcessingApp should run on start (also import it)

        PApplet.main(DemoApp.class, args);
        //PApplet.main(PongApp.class, args);
    }
}
