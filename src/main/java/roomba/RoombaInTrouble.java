package roomba;

import processing.core.PApplet;
import roomba.controller.PhysicalController;
import roomba.model.PhysicalModel;
import roomba.util.Pi4JContext;
import roomba.view.PhysicalScanner;

/**
 * The main class for the "Roomba in Trouble" Java application.
 */
public class RoombaInTrouble {

    private static PhysicalController controller;
    private static PhysicalScanner pui;

    /**
     * The main entry point for the application.
     *
     * @param args Command line arguments (not used).
     */
    public static void main(String[] args) {
        PhysicalModel model = new PhysicalModel();

        controller = new PhysicalController(model);
        pui = new PhysicalScanner(controller, Pi4JContext.createContext());

        GameField gameField = new GameField(pui);
        PApplet.runSketch(new String[] { "Game Field" }, gameField);
    }

}
