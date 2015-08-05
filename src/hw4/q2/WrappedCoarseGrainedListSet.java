package hw4.q2;

import edu.multicore.queues.MyQueue;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by pratik1 on 8/4/15.
 */
public class WrappedCoarseGrainedListSet implements MyQueue {
    CoarseGrainedListSet l = new CoarseGrainedListSet();
//    ArrayList a = new ArrayList();
    Random r = new Random();
    AtomicInteger a = new AtomicInteger();

    public WrappedCoarseGrainedListSet(){
        a.set(0);
    }
    @Override
    public boolean enq(Object value)
    {
//        a.add(value);
        a.incrementAndGet();
        return l.add(value);
    }

    @Override
    public Object deq() {
        if(a.get() == 0){
            return null;
        }
        int i = r.nextInt(a.get());
        Object val = a.decrementAndGet();
//        System.out.println(val);
        return l.remove(val);
    }

    @Override
    public boolean isEmpty() throws NoSuchMethodException {
        return a.get() == 0;
    }
}
