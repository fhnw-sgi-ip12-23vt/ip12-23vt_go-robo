package roomba.controller;

import roomba.model.PhysicalModel;
import roomba.util.mvcbase.ControllerBase;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The PhysicalController class represents the controller component for managing physical interactions in the Roomba application.
 * It is responsible for enqueueing and dequeueing items in the input queue.
 */
public class PhysicalController extends ControllerBase<PhysicalModel> {

    private static final Logger logger = Logger.getLogger(PhysicalController.class.getName());

    /**
     * Constructor for PhysicalController.
     *
     * @param model The PhysicalModel instance to be associated with this controller.
     */
    public PhysicalController(PhysicalModel model) {
        super(model);
    }

    public roomba.util.mvcbase.ObservableValue<Queue<String>> getQueue() {
        return model.inputQueue;
    }

    /**
     * Enqueue an item into the input queue.
     *
     * @param item The item to be enqueued.
     */
    public void enqueue(String item) {
        Queue<String> currentQueue = model.inputQueue.getValue();
        currentQueue.offer(item);
        setValue(model.inputQueue, currentQueue);
        logger.log(Level.INFO, "Queued item " + item);
        logger.log(Level.INFO, String.valueOf(model.inputQueue.getValue().size()));
    }

    /**
     * Dequeue an item from the input queue.
     *
     * @return The dequeued item or null if the queue is empty.
     */
    public String dequeue() {
        Queue<String> currentQueue = model.inputQueue.getValue();
        if (!currentQueue.isEmpty()) {
            String dequeuedItem = currentQueue.poll();
            setValue(model.inputQueue, currentQueue);
            logger.log(Level.INFO, "Dequeued item " + dequeuedItem);

            return dequeuedItem;
        } else {
            return null;
        }
    }
}
