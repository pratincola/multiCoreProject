package edu.multicore.queues.singleProducerSingleConsumer;

import edu.multicore.queues.MyQueue;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by pratik1 on 7/27/15.
 */
public class SingleQueue<T> implements MyQueue {

    AtomicInteger head ; // slot for get
    AtomicInteger tail ; // empty slot  for put
    int emptyCounter;
    int fullCounter;
    T [] items;

    public int getEmptyCounter() {
        return emptyCounter;
    }

    public int getFullCounter() {
        return fullCounter;
    }

    public SingleQueue(int size) {
        head = new AtomicInteger(0);
        tail = new AtomicInteger(0);
        emptyCounter = 0;
        fullCounter = 0;
        items = (T[]) new Object[size];
    }

    @Override
    public boolean enq(Object value) {
        boolean full = false;
        while (tail.get() - head.get() == items.length) {
            full = true;
        }; //busywait
        if (full){
            fullCounter++;
            full = false;
        }
        items[tail.get() % items.length] = (T)value;
        tail.getAndIncrement();
        return true;
    }

    @Override
    public Object deq() {
        boolean empty = false;
        while (tail.get() - head .get()== 0) {
            empty = true;
        }; // busywait
        if (empty) {
            emptyCounter++;
            empty = false;
        }
        Object x = items[head.get() % items.length];
        head.getAndIncrement();
        return x;
    }

    @Override
    public boolean isEmpty() throws NoSuchMethodException {
        return (tail.get() == head.get());
    }
}


