package roomba.catalog.components;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import roomba.catalog.components.base.DigitalSensor;
import roomba.catalog.components.base.PIN;
import com.pi4j.context.Context;
import com.pi4j.io.gpio.digital.DigitalInput;
import com.pi4j.io.gpio.digital.DigitalState;
import com.pi4j.io.gpio.digital.PullResistance;

import java.time.Duration;
import static com.pi4j.io.gpio.digital.DigitalInput.DEFAULT_DEBOUNCE;


public class SimpleRFID extends DigitalSensor {
    private final ExecutorService executor;
    private Runnable onScan;

    public SimpleRFID(Context pi4j, PIN address) {
        this(pi4j, address, Duration.ofMillis(500));
    }

    public SimpleRFID(Context pi4j, PIN address, Duration debounce) {
        super(pi4j,
                DigitalInput.newConfigBuilder(pi4j)
                        .id("BCM" + address)
                        .name("RFID Scanner #" + address)
                        .address(address.getPin())
                        .debounce(debounce.toMillis() * 1000) // Convert duration to microseconds
                        .pull(PullResistance.PULL_DOWN)
                        .build());

        executor = Executors.newSingleThreadExecutor();

        digitalInput.addListener(digitalStateChangeEvent -> {
            if (onScan != null && getState() == DigitalState.HIGH) {
                executor.submit(onScan);
            }
        });
    }

    public void onScan(Runnable task) {
        onScan = task;
    }

    private DigitalState getState() {
        return digitalInput.state();
    }

    @Override
    public void reset() {
        onScan = null;
        if (executor != null) {
            executor.shutdown();
        }
    }
}
