package roomba.controller;

import roomba.model.PhysicalModel;
import roomba.util.mvcbase.ControllerBase;
import roomba.util.mvcbase.ObservableValue.ValueChangeListener;

import java.util.Queue;

public class PhysicalController extends ControllerBase<PhysicalModel> {

    /**
     * Constructor for PhysicalController.
     *
     * @param model The PhysicalModel instance to be associated with this controller.
     */
    public PhysicalController(PhysicalModel model) {
        super(model);
    }

    /**
     * Subscribe to changes in the input queue.
     *
     * @param listener The listener to be notified when the input queue changes.
     */
    public void subscribeToQueueChanges(ValueChangeListener<Queue<String>> listener) {
        model.inputQueue.onChange(listener);
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
            return dequeuedItem;
        } else {
            return null;
        }
    }
}
