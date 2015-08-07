package edu.multicore.queues.jqueues;

import edu.multicore.queues.MyQueue;

import java.util.Random;

/**
 * Created by pratik1 on 8/5/15.
 */
public class WrapperSimpleTree<T> implements MyQueue<T> {

    PQueue pq;
    Random r;
    int size;
    boolean useRandomPriority;
    public WrapperSimpleTree(PQueue p, int logRange, boolean useRandomPriority){
        size = logRange;
        this.pq = p;
        if(useRandomPriority)
            r = new Random(4393);
    }

    @Override
    public boolean enq(T value) {
        int priority = 1;
        if(useRandomPriority)
            priority = r.nextInt(1 << size);
        pq.add(value, priority);

        return false;
    }

    @Override
    public T deq() {
        return (T) pq.removeMin();
    }

    @Override
    public boolean isEmpty() throws NoSuchMethodException {
        return pq.isEmpty();
    }
}
