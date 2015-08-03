package edu.multicore.queues.MultiOneToOne;

import edu.multicore.queues.MyQueue;

import java.util.ArrayDeque;

/**
 * Created by jtharp on 8/3/2015.
 */
public class WrappedArrayDeq<E> implements MyQueue<E>{

    private final ArrayDeque<E> queue;

    public WrappedArrayDeq(int capacity){
        this.queue = new ArrayDeque<E>(capacity);
    }
    @Override
    public boolean enq(E value) {
        return queue.offer(value);
    }

    @Override
    public E deq() {
        return queue.poll();
    }

    public int size(){
        return this.queue.size();
    }

    public E peek(){
        return this.queue.peek();
    }
}
