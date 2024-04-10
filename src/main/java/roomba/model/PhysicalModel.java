package roomba.model;

import roomba.util.mvcbase.ObservableValue;

import java.util.LinkedList;
import java.util.Queue;

public class PhysicalModel {

    // ObservableValue representing the input queue
    public ObservableValue<Queue<String>> inputQueue = new ObservableValue<>(new LinkedList<>());

}
