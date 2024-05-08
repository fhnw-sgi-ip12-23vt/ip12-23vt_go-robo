package roomba.script;

import java.io.*;
import com.pi4j.crowpi.components.RfidComponent;
import com.pi4j.context.Context;
import roomba.util.Pi4JContext;

public class rfidIDImport {
    private static final String CONFIG_FILE = "/home/pi/target/classes/main/java/app.properties";

    private static void addRfidCard(String key, String idToAdd, NewWindow window) {
        String tempFilePath = CONFIG_FILE + ".temp";

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
        File oldFile = new File(CONFIG_FILE + ".old");
        originalFile.renameTo(oldFile);
        File tempFile = new File(tempFilePath);
        if (tempFile.renameTo(originalFile)) {
            oldFile.delete();
            window.updateTextBoxText("Vorgang abgeschlossen");
        } else {
            window.updateTextBoxText("Vorgang fehlgeschlagen");
            oldFile.renameTo(originalFile);
        }
    }

    public static void getID(String key, NewWindow window, RfidComponent rfid) {
        window.updateTextBoxText("Vorgang" + key );
        rfid.waitForAnyCard(rfidCard -> {
            addRfidCard(key, rfidCard.getSerial(), window);
        });
    }

}

