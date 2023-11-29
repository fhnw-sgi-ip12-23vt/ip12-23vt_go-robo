package roomba;

import processing.core.PApplet;
import roomba.controller.PhysicalController;
import roomba.model.PhysicalModel;
import roomba.util.Pi4JContext;
import roomba.view.PhysicalScanner;

public class RoombaInTrouble {

    private static PhysicalController controller;
    private static PhysicalScanner pui;

    public static void main(String[] args) {
        PhysicalModel model = new PhysicalModel();

        controller = new PhysicalController(model);

        pui = new PhysicalScanner(controller, Pi4JContext.createContext());
        
        GameField gameField = new GameField(pui);
        PApplet.runSketch(new String[] { "Game Field" }, gameField);
    }

    public void stop() {
        controller.shutdown();
        pui.shutdown();
    }
}
