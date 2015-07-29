package edu.multicore.queues.utils;

import edu.multicore.queues.MyQueue;

/**
 * Created by pratik1 on 7/27/15.
 */
public class Consumer extends Worker {

    MyQueue q;
    int numMessages;
    public Consumer(MyQueue q, int numMessages){
        this.q = q;
        this.numMessages = numMessages;
    }

    @Override
    public void run() {
        while(numMessages > 0){
            Message m = (Message) q.deq();
            consume(m);
            System.out.println("deque: " + m.toString());
            --numMessages;
        }
    }

    private void consume(Message m){

        m.getId();
        m.getMessage();
    }
}
