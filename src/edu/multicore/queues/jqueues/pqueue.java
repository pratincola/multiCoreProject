package edu.multicore.queues.jqueues;

/**
 * Created by pratik1 on 8/5/15.
 */

interface PQueue<T> {

    void add(T item, int priority);

    T removeMin();

    boolean isEmpty();

    int getRange();
}