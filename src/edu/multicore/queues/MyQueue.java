package edu.multicore.queues;

/**
 * Created by pratik1 on 7/27/15.
 */
public interface MyQueue<T> {
    public boolean enq(T value);
    public T deq();


}
