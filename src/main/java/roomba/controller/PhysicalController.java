package roomba.controller;

import roomba.model.PhysicalModel;
import roomba.util.mvcbase.ControllerBase;
import roomba.util.mvcbase.ObservableValue.ValueChangeListener;

import java.util.Queue;

public class PhysicalController extends ControllerBase<PhysicalModel> {

    public PhysicalController(PhysicalModel model) {
        super(model);
    }

    public void subscribeToQueueChanges(ValueChangeListener<Queue<String>> listener) {
        model.inputQueue.onChange(listener);
    }

    public void enqueue(String item) {
        Queue<String> currentQueue = model.inputQueue.getValue();
        currentQueue.offer(item);
        setValue(model.inputQueue, currentQueue);

    }

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
