package roomba.model;

import java.util.LinkedList;
import java.util.Queue;

import roomba.util.mvcbase.ObservableValue;

public class PhysicalModel {

    // ObservableValue representing the input queue
    public ObservableValue<Queue<String>> inputQueue = new ObservableValue<>(new LinkedList<>());

}
