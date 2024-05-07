package roomba.controller;

import roomba.catalog.components.base.SimpleLed;
import roomba.model.LedModel;
import roomba.util.mvcbase.ControllerBase;
import roomba.view.PhysicalLed;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The PhysicalController class represents the controller component
 * for managing physical interactions in the Roomba application.
 * It is responsible for enqueueing and dequeueing items in the input queue.
 */
public class LedController extends ControllerBase<LedModel> {

    private static final Logger LOGGER = Logger.getLogger(LedController.class.getName());

    private final LedModel model;
    /**
     * Controller needs a Model.
     *
     * @param model Model managed by this Controller
     */
    public LedController(LedModel model) {
        super(model);
        this.model = model;
    }

    public void saveInModel(SimpleLed g, SimpleLed b, SimpleLed y){
        model.greenLED = g;
        model.blueLED = b;
        model.yellowLED = y;
    }

    public void blinkLedD1(PhysicalLed.LEDType typ){
        try {
            ledOn(typ);
            Thread.sleep(1000); // Sleep for 1000 milliseconds (1 second)
        } catch (InterruptedException e) {
            ledReset(typ);
            // Handle the InterruptedException, if necessary
        }


    }


    public void ledOn(PhysicalLed.LEDType typ) {
        switch (typ) {
        case GREEN -> model.greenLED.on();
        case YELLOW -> model.yellowLED.on();
        case BLUE -> model.blueLED.on();
        default -> LOGGER.log(Level.INFO, "no LED typ");
        }

    }

    public void ledOff(PhysicalLed.LEDType typ) {
        switch (typ) {
        case GREEN -> {
            if (model.greenLED.isOn()) model.greenLED.off();
        }
        case YELLOW -> {
            if (model.yellowLED.isOn()) model.yellowLED.off();
        }
        case BLUE -> {
            if (model.blueLED.isOn()) model.blueLED.off();
        }
        default -> LOGGER.log(Level.INFO, "no LED typ");
        }
    }

    public void ledReset(PhysicalLed.LEDType typ) {
        switch (typ) {
        case GREEN -> model.greenLED.reset();
        case YELLOW -> model.yellowLED.reset();
        case BLUE -> model.blueLED.reset();
        default -> LOGGER.log(Level.INFO, "no LED typ");
        }
    }




}
