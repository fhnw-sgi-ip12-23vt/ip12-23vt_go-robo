package roomba.script;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.pi4j.crowpi.components.RfidComponent;
import com.pi4j.context.Context;
import roomba.util.Pi4JContext;

public class rfidIDImport {
    private static final String CONFIG_FILE = "/home/pi/target/classes/main/java/app.properties";
    private static final Logger LOGGER = Logger.getLogger(rfidIDImport.class.getName());

    private static void addRfidCard(String key, String idToAdd, NewWindow window) {
        String tempFilePath = CONFIG_FILE + ".temp";
        String oldFilePath = CONFIG_FILE + ".old";

        try (BufferedReader reader = new BufferedReader(new FileReader(CONFIG_FILE));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains(key)) {
                    line = line + ", " + idToAdd;
                }
                // Write the line (either modified or not) to the temporary file
                writer.write(line + System.getProperty("line.separator"));
            }
        } catch (IOException e) {
            // Handle file reading or writing errors
            e.printStackTrace();
        }

        // Replace the original file with the temporary file
        LOGGER.log(Level.INFO, "Creating Path for " + CONFIG_FILE);
        File originalFile = new File(CONFIG_FILE);
        LOGGER.log(Level.INFO, "Creating Path for " + oldFilePath);
        File oldFile = new File(oldFilePath);
        LOGGER.log(Level.INFO, "Renaming File " + CONFIG_FILE + " to " + oldFilePath);
        originalFile.renameTo(oldFile);
        LOGGER.log(Level.INFO, "Creating Path for " + tempFilePath);
        File tempFile = new File(tempFilePath);
        if (tempFile.renameTo(originalFile)) {
            LOGGER.log(Level.INFO, "Rename succeeded, deleting " + oldFilePath);
            oldFile.delete();
            window.updateTextBoxText("Vorgang abgeschlossen");
            LOGGER.log(Level.INFO, "RFID import and file update completed");
        } else {
            LOGGER.log(Level.INFO, "Rename failed, returning to old app.properties");
            window.updateTextBoxText("Vorgang fehlgeschlagen");
            LOGGER.log(Level.INFO,"Renamed " + oldFile + " to " + originalFile);
            oldFile.renameTo(originalFile);
        }
    }

    public static void getID(String key, NewWindow window, RfidComponent rfid) {
        window.updateTextBoxText("Vorgang" + key );
        LOGGER.log(Level.INFO, "Creating RFID Event handler");
        rfid.waitForAnyCard(rfidCard -> {
            LOGGER.log(Level.INFO, "ADDING " + rfidCard.getSerial() + " to app.properties");
            addRfidCard(key, rfidCard.getSerial(), window);
            LOGGER.log(Level.INFO, "COMPLETED " + rfidCard.getSerial() + " to app.properties");
        });
        LOGGER.log(Level.INFO, "Finished RFID Import");
    }

}

