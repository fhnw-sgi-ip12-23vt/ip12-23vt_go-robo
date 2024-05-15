package roomba.script;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.pi4j.crowpi.components.RfidComponent;

public class rfidIDImport {
    private static final String CONFIG_FILE = "/home/pi/target/classes/app.properties";
    private static final Logger LOGGER = Logger.getLogger(rfidIDImport.class.getName());

    private static void addRfidCard(String key, String idToAdd, addRFIDCard window) {
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
        File originalFile = new File(CONFIG_FILE);
        File oldFile = new File(oldFilePath);
        originalFile.renameTo(oldFile);
        File tempFile = new File(tempFilePath);
        if (tempFile.renameTo(originalFile)) {
            LOGGER.log(Level.INFO, "Rename succeeded, deleting " + oldFilePath);
            oldFile.delete();
            window.updateTextBoxText("Vorgang abgeschlossen" + " (" + key + ")");
            LOGGER.log(Level.INFO, "RFID import and file update completed");
            //set pi user as owner of new file
            try {
                ProcessBuilder pb = new ProcessBuilder("chown", "pi", CONFIG_FILE);
                pb.start();
            }catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            LOGGER.log(Level.INFO, "Rename failed, returning to old app.properties");
            window.updateTextBoxText("Vorgang fehlgeschlagen");
            LOGGER.log(Level.INFO,"Renamed " + oldFile + " to " + originalFile);
            oldFile.renameTo(originalFile);
        }
    }

    public static void getID(String key, addRFIDCard window, RfidComponent rfid) {
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

