package roomba.catalog.components;

import com.pi4j.context.Context;
import com.pi4j.io.gpio.digital.DigitalOutput;

import roomba.catalog.components.base.DigitalActuator;
import roomba.catalog.components.base.PIN;

public class SimpleRFID extends DigitalActuator {
//TODO IMPLEMENT RFID CHIP LOGIC


    /**
     * Creates a new SimpleLed component with a custom BCM pin.
     *
     * @param pi4j    Pi4J context
     * @param address Custom BCM pin address
     */
    public SimpleRFID(Context pi4j, PIN address) {
        super(pi4j,
              DigitalOutput.newConfigBuilder(pi4j)
                      .id("BCM" + address)
                      .name("LED #" + address)
                      .address(address.getPin())
                      .build());
        logDebug("Created new SimpleLed component");
    }


    public void onDown(Runnable task) {
        onDown = task;
    }

    private Runnable onDown;

}
