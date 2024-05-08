package roomba.model;

import roomba.view.GameField;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.LinkedList;
import java.util.Queue;

/**
 * The PhysicalModel class represents the model component for managing physical interactions in the Roomba application.
 * It contains an observable queue of input items.
 */
public class PhysicalModel implements PropertyChangeListener {
    private GameField gm;
    /**
     * An observable queue of input items.
     */
    public Queue<String> inputQueue = new LinkedList<>();
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        System.out.println("Event hinzugefügt zur Queue: " + evt.getPropertyName());
        gm.handleInput(inputQueue.poll());
    }

    public void addGameField(GameField gm) {
        this.gm = gm;
    }

}
