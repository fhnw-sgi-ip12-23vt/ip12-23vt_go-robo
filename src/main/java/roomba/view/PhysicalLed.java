package roomba.view;

import com.pi4j.context.Context;
import roomba.catalog.components.base.PIN;
import roomba.catalog.components.base.SimpleLed;
import roomba.controller.LedController;
import roomba.model.LedModel;
import roomba.util.mvcbase.PuiBase;
import java.util.logging.Logger;

/**
 * The PhysicalScanner class represents a physical scanner component used in the Roomba application.
 * It is responsible for initializing and managing interactions with the RFID scanner component.
 */
public class PhysicalLed extends PuiBase<LedModel, LedController> {
    private static final Logger LOGGER = Logger.getLogger(PhysicalLed.class.getName());

    public LedController getController() {
        return controller;
    }

    private final LedController controller;
    private SimpleLed greenLED;
    private SimpleLed yellowLED;
    private SimpleLed blueLED;

    public enum LEDType {
        GREEN, YELLOW, BLUE
    }

    /**
     * Constructor for PhysicalScanner.
     *
     * @param controller The associated PhysicalController.
     * @param pi4J       The Pi4J Context.
     */
    public PhysicalLed(LedController controller, Context pi4J) {
        super(controller, pi4J);
        this.controller = controller;
        controller.saveInModel(greenLED, blueLED, yellowLED);

    }

    /**
     * Initializes the parts of the physical scanner, in this case, the RFID scanner.
     */
    @Override
    public void initializeParts() {
        greenLED = new SimpleLed(pi4J, PIN.D17);
        blueLED = new SimpleLed(pi4J, PIN.D22);
        yellowLED = new SimpleLed(pi4J, PIN.D27);

    }

    public void ledOn(LEDType typ) {
        controller.ledOn(typ);

    }

    public void ledOff(LEDType typ) {
        controller.ledOff(typ);
    }
    public void blink(LEDType typ) {
        controller.blinkLedD1(typ);
    }

    public void ledReset(LEDType typ) {
        controller.ledReset(typ);
    }
}
