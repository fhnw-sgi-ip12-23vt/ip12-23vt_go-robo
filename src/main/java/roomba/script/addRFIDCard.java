package roomba.script;

import com.pi4j.crowpi.components.RfidComponent;
import processing.core.PApplet;

import java.util.logging.Level;
import java.util.logging.Logger;

public class addRFIDCard extends PApplet {
    Button[] buttons = new Button[9];
    String textTop = "Klicken Sie die gewollte Funktion an und halten sie die Karte für 3 Sekunden über den Scanenr";
    TextBox textBoxBottom;
    RfidComponent rfid;
    private static final Logger LOGGER = Logger.getLogger(addRFIDCard.class.getName());

    String[] buttonNames = new String[9];

    public addRFIDCard(RfidComponent rfid) {
        this.rfid = rfid;
    }

    public void settings() {
        size(1200, 250);
    }

    public void setup() {
        float buttonWidth = (width) / buttons.length;

        // set button names
        buttonNames[0] = "Rechts";
        buttonNames[1] = "Links";
        buttonNames[2] = "Vorne";
        buttonNames[3] = "Umdrehen";
        buttonNames[4] = "Restart";
        buttonNames[5] = "Schwierigkeit:\nEinfach";
        buttonNames[6] = "Schwierigkeit:\nMittel";
        buttonNames[7] = "Schwierigkeit:\nSchwer";
        buttonNames[8] = "Moduswechsel";

        for (int i = 0; i < buttons.length; i++) {
            buttons[i] = new Button(i * buttonWidth, 80, buttonWidth, 50, buttonNames[i]);
        }
        textBoxBottom = new TextBox(50, height - 40, width - 100, 30, "");
    }

    public void draw() {
        background(255);
        textSize(24);
        textAlign(CENTER, CENTER);
        text(textTop, width / 2, 25);
        textBoxBottom.display();
        for (Button button : buttons) {
            button.display();
        }
    }

    private addRFIDCard getWindow(){
        return this;
    }

    public void mousePressed() {
        for (Button button : buttons) {
            if (button.isMouseInside()) {
                button.onClick();
            }
        }
    }

    // Method to update textbox text
    void updateTextBoxText(String newText) {
        textBoxBottom.setText(newText);
        redraw();
    }

    class Button {
        float x, y, w, h;
        String label;

        Button(float x, float y, float w, float h, String label) {
            this.x = x;
            this.y = y;
            this.w = w;
            this.h = h;
            this.label = label;
        }

        void display() {
            stroke(0);
            fill(200);
            rect(x, y, w, h);
            fill(0);
            textAlign(CENTER, CENTER);
            textSize(16);
            text(label, x + w / 2, y + h / 2);
        }

        boolean isMouseInside() {
            return mouseX >= x && mouseX <= x + w && mouseY >= y && mouseY <= y + h;
        }

        void onClick() {
            updateTextBoxText("Halten Sie die Karte für die Funktion " +label + " über den Scanner");
            switch (label) {
                case "Rechts":
                    rfidIDImport.getID("RFID_RIGHT", getWindow(), rfid);
                    break;
                case "Links":
                    rfidIDImport.getID("RFID_LEFT", getWindow(), rfid);
                    break;
                case "Vorne":
                    rfidIDImport.getID("RFID_UP", getWindow(), rfid);
                    break;
                case "Umdrehen":
                    rfidIDImport.getID("RFID_DOWN", getWindow(), rfid);
                    break;
                case "Restart":
                    rfidIDImport.getID("RFID_RESET", getWindow(), rfid);
                    break;
                case "Schwierigkeit:\nEinfach":
                    rfidIDImport.getID("RFID_EASY", getWindow(), rfid);
                    break;
                case "Schwierigkeit:\nMittel":
                    rfidIDImport.getID("RFID_MEDIUM", getWindow(), rfid);
                    break;
                case "Schwierigkeit:\nSchwer":
                    rfidIDImport.getID("RFID_HARD", getWindow(), rfid);
                    break;
                case "Moduswechsel":
                    rfidIDImport.getID("RFID_TURN", getWindow(), rfid);
                    break;
            }

        }
    }

    class TextBox {
        float x, y, w, h;
        String text;

        TextBox(float x, float y, float w, float h, String placeholder) {
            this.x = x;
            this.y = y;
            this.w = w;
            this.h = h;
            this.text = placeholder;
        }

        void display() {
            stroke(0);
            fill(255);
            rect(x, y, w, h);
            fill(0);
            textAlign(LEFT, CENTER);
            textSize(16);
            text(text, x + 5, y + h / 2);
        }

        void setText(String newText) {
            this.text = newText;
        }
    }

    public static void main(String[] args) {
        PApplet.main("TenButtonsWithText");
    }


    public static void openDialog(RfidComponent rfid) {
        addRFIDCard nw = new addRFIDCard(rfid);
        PApplet.runSketch(new String[]{"Dialog"}, nw);
    }
}
