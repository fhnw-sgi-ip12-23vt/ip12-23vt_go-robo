package roomba.view;

import com.pi4j.context.Context;
import com.pi4j.crowpi.components.RfidComponent;
import roomba.catalog.components.base.PIN;
import roomba.catalog.components.base.SimpleLed;
import roomba.controller.PhysicalController;
import roomba.model.PhysicalModel;
import roomba.util.mvcbase.PuiBase;


import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The PhysicalScanner class represents a physical scanner component used in the Roomba application.
 * It is responsible for initializing and managing interactions with the RFID scanner component.
 */
public class PhysicalScanner extends PuiBase<PhysicalModel, PhysicalController> {
    private static final Logger LOGGER = Logger.getLogger(PhysicalScanner.class.getName());

    public PhysicalController getController() {
        return controller;
    }

    private final PhysicalController controller;
    private RfidComponent rfid;
    private SimpleLed led;

    /**
     * Constructor for PhysicalScanner.
     *
     * @param controller The associated PhysicalController.
     * @param pi4J       The Pi4J Context.
     */
    public PhysicalScanner(PhysicalController controller, Context pi4J) {
        super(controller, pi4J);
        this.controller = controller;
    }

    /**
     * Initializes the parts of the physical scanner, in this case, the RFID scanner.
     */
    @Override
    public void initializeParts() {
        rfid = new RfidComponent(pi4J);
        led = new SimpleLed(pi4J, PIN.D17);
    }

    /**
     * Sets up UI-to-action bindings by associating RFID scan events with controller actions.
     *
     * @param controller The associated PhysicalController.
     */
    @Override
    public void setupUiToActionBindings(PhysicalController controller) {

        rfid.onCardDetected(rfidCard -> {
                LOGGER.log(Level.INFO, "Card:  " + rfidCard);

                controller.enqueue(rfidCard.getSerial());
            }
        );

    }

    public void ledOn() {
        led.on();
    }

    public void ledOff() {
        if (led.isOn()) {
            led.off();
        }
    }

    public void ledReset() {
        led.reset();
    }
}
