package roomba;

import ch.qos.logback.classic.Level;
import com.pi4j.plugin.mock.provider.spi.MockSpi;
import processing.core.PApplet;
import roomba.controller.PhysicalController;
import roomba.model.PhysicalModel;
import roomba.util.Pi4JContext;
import roomba.view.GameField;
import roomba.view.PhysicalScanner;

import org.slf4j.LoggerFactory;

/**
 * The main class for the "Roomba in Trouble" Java application.
 */
public class RoombaInTrouble {

    /**
     * The main entry point for the application.
     *
     * @param args Command line arguments (not used).
     */
    public static void main(String[] args) {

        // Get the logger for the MockSpi class and changed log level to Warning only (just for the MockSpi class)
        var mockSpiLogger = LoggerFactory.getLogger(MockSpi.class);
        var logbackLogger = (ch.qos.logback.classic.Logger) mockSpiLogger;
        logbackLogger.setLevel(Level.WARN);


        PhysicalModel model = new PhysicalModel();

        PhysicalController controller = new PhysicalController(model);
        PhysicalScanner pui = new PhysicalScanner(controller, Pi4JContext.createContext());

        GameField gameField = new GameField(pui);
        PApplet.runSketch(new String[] { "Game Field" }, gameField);
    }

}
