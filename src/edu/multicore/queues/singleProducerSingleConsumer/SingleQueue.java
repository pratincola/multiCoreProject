package edu.multicore.queues.singleProducerSingleConsumer;

import edu.multicore.queues.MyQueue;

/**
 * Created by pratik1 on 7/27/15.
 */
public class SingleQueue<T> implements MyQueue {

    int head = 0;   // slot for get
    int tail = 0;   // empty slot  for put
    T [] items;

    public SingleQueue(int size) {
        head = 0; tail = 0;
        items = (T[]) new Object[size];
    }

    @Override
    public boolean enq(Object value) {
        while (tail - head == items.length) {}; //busywait
        items[tail % items.length] = (T)value;
        tail++;
        return true;
    }

    @Override
    public Object deq() {
        while (tail - head == 0) {}; // busywait
        Object x = items[head % items.length];
        head++;
        return x;
    }
}


