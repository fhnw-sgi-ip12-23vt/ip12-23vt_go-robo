package roomba.controller;

import roomba.model.PhysicalModel;
import roomba.util.mvcbase.ControllerBase;
import roomba.view.GameField;
import java.beans.PropertyChangeListener;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The PhysicalController class represents the controller component
 * for managing physical interactions in the Roomba application.
 * It is responsible for enqueueing and dequeueing items in the input queue.
 */
public class PhysicalController extends ControllerBase<PhysicalModel> {

    private static final Logger LOGGER = Logger.getLogger(PhysicalController.class.getName());

    /**
     * Constructor for PhysicalController.
     *
     * @param model The PhysicalModel instance to be associated with this controller.
     */
    public PhysicalController(PhysicalModel model) {
        super(model);
        this.addPropertyChangeListener(model);
    }

    public Queue<String> getQueue() {
        return model.inputQueue;
    }

    public void enqueue(String st) {
        model.inputQueue.add(st);
        this.triggerPropertyChange(st, "", "");
        LOGGER.log(Level.INFO, "queue:  " + model.inputQueue.peek());

    }

    public void setGm(GameField gm) {
        assert gm != null;
        model.addGameField(gm);
    }


    private final java.beans.PropertyChangeSupport support = new java.beans.PropertyChangeSupport(this);

    public void addPropertyChangeListener(PropertyChangeListener p) {
        support.addPropertyChangeListener(p);
    }

    // Methode, die die Property-Änderung auslöst
    void triggerPropertyChange(String propertyName, Object oldValue, Object newValue) {
        support.firePropertyChange(propertyName, oldValue, newValue);
    }


}
