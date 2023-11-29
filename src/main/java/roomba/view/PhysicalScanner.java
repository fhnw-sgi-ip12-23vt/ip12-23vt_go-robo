package roomba.view;

import com.pi4j.context.Context;

import roomba.catalog.components.SimpleRFID;
import roomba.catalog.components.base.PIN;
import roomba.controller.PhysicalController;
import roomba.model.PhysicalModel;
import roomba.util.mvcbase.PuiBase;

public class PhysicalScanner extends PuiBase<PhysicalModel, PhysicalController> {

    //TODO IMPLEMENT RFID CHIP LOGIC into SimpleRFID
    protected SimpleRFID rfid;
    public PhysicalController controller;

    public PhysicalScanner(PhysicalController controller, Context pi4J) {
        super(controller, pi4J);
        this.controller = controller;
    }

    @Override
    public void initializeParts() {
        //PIN.D22 is a placeholder
        rfid = new SimpleRFID(pi4J, PIN.D22);
    }


    @Override
    public void setupUiToActionBindings(PhysicalController controller) {
        rfid.onScan(() -> controller.enqueue("PLACEHOLDER"));

        
    }

    // TODO
    // RFID read
}
