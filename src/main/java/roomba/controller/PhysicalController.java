package roomba.controller;

import roomba.model.PhysicalModel;
import roomba.util.mvcbase.ControllerBase;

public class PhysicalController extends ControllerBase<PhysicalModel> {

    public PhysicalController(PhysicalModel model) {
        super(model);
    }

    public void addInputToQueue(String input) {
        //check if this implementation is correct.
        model.inputQueue.offer(input);
    }

}
