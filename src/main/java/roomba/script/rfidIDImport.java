package roomba.script;

import java.util.Scanner;
import java.io.*;
import com.pi4j.crowpi.components.RfidComponent;
import com.pi4j.context.Context;
import roomba.util.Pi4JContext;

public class rfidIDImport {
    private static final String CONFIG_FILE = "/home/pi/target/classes/main/java/app.properties";
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input  = "";

        while(!input.equals("exit")){
            printOptions();
            input = scanner.nextLine();

            switch (input) {
                case "1":
                    getID("RFID_RIGHT");
                    break;
                case "2":
                    getID("RFID_LEFT");
                    break;
                case "3":
                    getID("RFID_UP");
                    break;
                case "4":
                    getID("RFID_DOWN");
                    break;
                case "5":
                    getID("RFID_RESET");
                    break;
                case "6":
                    getID("RFID_EASY");
                    break;
                case "7":
                    getID("RFID_MEDIUM");
                    break;
                case "8":
                    getID("RFID_HARD");
                    break;
                case "9":
                    getID("RFID_TURN");
                    break;
                default:
                    System.out.println("Ungültige Eingabe");
            }

        }
    }

    private static void printOptions(){
        System.out.println("\n");
        System.out.println("Für welche Karte soll eine weiter ID eingelesen werden?");
        System.out.println("Geben sie die zugehörige Nummer ein \n");
        System.out.println("1. Rechte Bewegungskarte");
        System.out.println("2. Linke Bewegungskarte");
        System.out.println("3. Gerade Bewegungskarte");
        System.out.println("4. Rückwärts Bewegungskarte");
        System.out.println("5. Level Restart Karte");
        System.out.println("6. Level Auswahl Karte: Einfach");
        System.out.println("7. Level Auswahl Karte: Mittel");
        System.out.println("8. Level Auswwahl Karte: Schwer");
        System.out.println("9. Moduswechsel Karte \n");
        System.out.println("'exit' zum beenden eingeben");
    }

    private static void addRfidCard(String key, String idToAdd) {
        System.out.printf("Die ID" + idToAdd + "wird hinzugefügt");
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
        originalFile.delete();
        File tempFile = new File(tempFilePath);
        if (tempFile.renameTo(originalFile)) {
            System.out.println("Vorgang abgeschlossen\n.");
        } else {
            System.out.println("Ein Fehler is aufgetreten.\n");
        }
    }

    private static void getID(String key) {
        Context pi4j = Pi4JContext.createContext();
        RfidComponent rfid = new RfidComponent(pi4j);
        final String[] id = {""};
        rfid.onCardDetected(rfidCard -> {
            addRfidCard(key, rfidCard.getSerial());
        });
    }
}