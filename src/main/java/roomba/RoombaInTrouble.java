package roomba;

import ch.qos.logback.classic.Level;
import com.pi4j.context.Context;
import com.pi4j.plugin.mock.provider.spi.MockSpi;
import org.slf4j.LoggerFactory;
import processing.core.PApplet;
import roomba.controller.LedController;
import roomba.controller.PhysicalController;
import roomba.model.LedModel;
import roomba.model.PhysicalModel;
import roomba.util.Pi4JContext;
import roomba.view.GameField;
import roomba.view.PhysicalLed;
import roomba.view.PhysicalScanner;


/**
 * The main class for the "Roomba in Trouble" Java application.
 */
public final class RoombaInTrouble {

    /**
     * The main entry point for the application.
     *
     * @param args Command line arguments (not used).
     */
    public static void main(String[] args) {

        var mockSpiLogger = LoggerFactory.getLogger(MockSpi.class);
        var logbackLogger = (ch.qos.logback.classic.Logger) mockSpiLogger;
        logbackLogger.setLevel(Level.WARN);

        Context context = Pi4JContext.createContext();
        PhysicalModel model = new PhysicalModel();
        PhysicalController controller = new PhysicalController(model);
        PhysicalScanner pui = new PhysicalScanner(controller, context);
        LedModel lm = new LedModel();
        LedController lc = new LedController(lm);
        PhysicalLed pl = new PhysicalLed(lc, context);


        GameField gameField = new GameField(pui, pl);
        PApplet.runSketch(new String[] {"Game Field"}, gameField);
    }
    private RoombaInTrouble() {
        throw new AssertionError("Utility class should not be instantiated");
    }


}
