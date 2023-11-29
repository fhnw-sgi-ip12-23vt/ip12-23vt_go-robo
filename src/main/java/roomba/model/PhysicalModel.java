package roomba.model;

import java.util.LinkedList;
import java.util.Queue;

import roomba.util.mvcbase.ObservableValue;

public class PhysicalModel {

    public ObservableValue<Queue<String>> inputQueue = new ObservableValue<>(new LinkedList<>());

}
