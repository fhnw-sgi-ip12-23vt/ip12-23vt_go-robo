package roomba.model;

import roomba.util.mvcbase.ObservableValue;
import java.util.LinkedList;
import java.util.Queue;

/**
 * The PhysicalModel class represents the model component for managing physical interactions in the Roomba application.
 * It contains an observable queue of input items.
 */
public class PhysicalModel {
    /**
     * An observable queue of input items.
     */
    public ObservableValue<Queue<String>> inputQueue = new ObservableValue<>(new LinkedList<>());
}
