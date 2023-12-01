package roomba.catalog.components;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

import com.pi4j.context.Context;
import com.pi4j.crowpi.components.RfidComponent;
import java.util.logging.Logger;
import java.time.Duration;

public class SimpleRFID {
    private final ExecutorService executor;
    private final RfidComponent rfid;
    private Consumer<String> onScan;
    private Logger logger;

    public SimpleRFID(Context pi4j) {
        rfid = new RfidComponent(pi4j);
       // logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

        executor = Executors.newSingleThreadExecutor();

        rfid.onCardDetected(card -> {
            // Print serial number and capacity of approached card
            logger.info("Detected");
            System.out.println("Detected card with serial " + card.getSerial() + " and capacity of "
                    + card.getCapacity() + " bytes");

            executor.submit(() -> onScan.accept(card.getSerial()));

        });
    }

    public void onScan(Consumer<String> scanCallback) {
        this.onScan = scanCallback;
    }

}
