package roomba.controller;

import com.pi4j.Pi4J;
import com.pi4j.crowpi.components.RfidComponent;
import roomba.util.Pi4JContext;

import java.time.Duration;
import java.util.logging.Logger;

public class RfidScanTest {
    public static void main(String[] args) {
        Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

        final var pi4j = Pi4JContext.createContext();

        // Initialize RFID component
        final var rfid = new RfidComponent(pi4j);

        logger.info("Start, Scan (15sec)");

        // Register event listener to detect card in proximity
        rfid.onCardDetected(card -> {
            // Print serial number and capacity of approached card
            logger.info("Detected");
            System.out.println("Detected card with serial " + card.getSerial() + " and capacity of " + card.getCapacity() + " bytes");
        });

        // Wait for 15 seconds while handling events before exiting
        delay(Duration.ofSeconds(15));

        logger.info("Done");

        pi4j.shutdown();
    }
    private static void delay(Duration duration) {
        try {
            Thread.sleep(duration.toMillis());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

}
