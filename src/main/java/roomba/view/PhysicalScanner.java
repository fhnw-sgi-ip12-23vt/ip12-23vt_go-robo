package roomba.view;

import com.pi4j.context.Context;

import roomba.catalog.components.SimpleRFID;
import roomba.controller.PhysicalController;
import roomba.model.PhysicalModel;
import roomba.util.mvcbase.PuiBase;

public class PhysicalScanner extends PuiBase<PhysicalModel, PhysicalController> {

    protected SimpleRFID rfid;
    public PhysicalController controller;

    public PhysicalScanner(PhysicalController controller, Context pi4J) {
        super(controller, pi4J);
        this.controller = controller;
    }

    @Override
    public void initializeParts() {
        rfid = new SimpleRFID(pi4J);
    }

    @Override
    public void setupUiToActionBindings(PhysicalController controller) {
        rfid.onScan(serial -> controller.enqueue(serial));
        // or for lamba expression rfid.onScan(controller::enqueue);

    }

}
