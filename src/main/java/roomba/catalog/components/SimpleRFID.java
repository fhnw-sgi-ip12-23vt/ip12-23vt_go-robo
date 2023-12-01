package roomba.catalog.components;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

import com.pi4j.context.Context;
import com.pi4j.crowpi.components.RfidComponent;
import java.util.logging.Logger;

/**
 * The SimpleRFID class provides a simple interface for interacting with RFID components.
 */
public class SimpleRFID {
    private final ExecutorService executor;
    private final RfidComponent rfid;
    private Consumer<String> onScan;
    private Logger logger;

    /**
     * Constructs a SimpleRFID instance.
     *
     * @param pi4j The Pi4J context.
     */
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

    /**
     * Sets the callback to be executed on RFID card scan.
     *
     * @param scanCallback The callback function taking a string parameter representing the card's serial number.
     */
    public void onScan(Consumer<String> scanCallback) {
        this.onScan = scanCallback;
    }

}
