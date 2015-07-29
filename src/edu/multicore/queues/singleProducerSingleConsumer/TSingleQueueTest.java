package edu.multicore.queues.singleProducerSingleConsumer;

import edu.multicore.queues.MyQueue;
import org.junit.Test;

import java.util.NoSuchElementException;

import static org.junit.Assert.*;

/**
 * Created by pratik1 on 7/28/15.
 */
public class TSingleQueueTest {
    protected MyQueue q = new SingleQueue(3);

    @Test
    public void testEnqueueThenDequeue() {
        String message = "hello";
        q.enq(message);
        assertEquals(q.deq(), message);
    }

    @Test
    public void testFiftyInThenFiftyOut() {
        for (int i = 0; i < 50; i++) {
            q.enq(i);
        }
        for (int i = 0; i < 50; i++) {
            assertEquals(((Integer)q.deq()).intValue(), i);
        }
    }

}