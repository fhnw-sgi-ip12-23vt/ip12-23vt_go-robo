package roomba.view;

import com.pi4j.context.Context;

import roomba.catalog.components.SimpleRFID;
import roomba.controller.PhysicalController;
import roomba.model.PhysicalModel;
import roomba.util.mvcbase.PuiBase;

public class PhysicalScanner extends PuiBase<PhysicalModel, PhysicalController> {

    // Instance variable representing the RFID scanner
    protected SimpleRFID rfid;

    // Reference to the associated controller
    public PhysicalController controller;

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
        rfid = new SimpleRFID(pi4J);
    }

    /**
     * Sets up UI-to-action bindings by associating RFID scan events with controller actions.
     *
     * @param controller The associated PhysicalController.
     */
    @Override
    public void setupUiToActionBindings(PhysicalController controller) {
        // Associates the RFID scan event with the enqueue method in the controller
        rfid.onScan(serial -> controller.enqueue(serial));
        // Alternatively, you can use a lambda expression: rfid.onScan(controller::enqueue);
    }
}
