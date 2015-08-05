package edu.multicore.queues.utils;

import java.util.ArrayList;
import java.util.List;

public class Bin<T> {
    List<T> list;
    public Bin() {
        list = new ArrayList<T>();
    }

    public synchronized void put(T item) {
        list.add(item);
    }

    public synchronized T get() {
        try {
            return list.remove(0);
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    public synchronized boolean isEmpty() {
        return list.isEmpty();
    }

}

