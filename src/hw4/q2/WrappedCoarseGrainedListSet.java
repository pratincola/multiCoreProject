package hw4.q2;

import edu.multicore.queues.MyQueue;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by pratik1 on 8/4/15.
 */
public class WrappedCoarseGrainedListSet implements MyQueue {
    CoarseGrainedListSet l = new CoarseGrainedListSet();
    ArrayList a = new ArrayList();
    Random r;

    @Override
    public boolean enq(Object value)
    {
        a.add(value);
        return l.add(value);
    }

    @Override
    public Object deq() {
        r = new Random();
        int i = r.nextInt(a.size());
        Object val = a.remove(i);
        System.out.println(val);
        return l.remove(val)? 1:0;
    }

    @Override
    public boolean isEmpty() throws NoSuchMethodException {
        return a.isEmpty();
    }
}
