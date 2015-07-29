package edu.multicore.queues.utils;

import edu.multicore.queues.MyQueue;

/**
 * Created by pratik1 on 7/27/15.
 */
public class Producer extends Worker{

    MyQueue q;
    int ctr = 0;
    int numMessages;
    public Producer(MyQueue q, int numMessages){
        this.q = q;
        this.numMessages = numMessages;
    }

    @Override
    public void run() {
        while(numMessages > 0) {
            Message p = produce();
            q.enq(p);
            System.out.println("enq: " + p.toString());
            --numMessages;
        }
    }

    private Message produce(){
        Message m = new Message(++ctr, "Example message.");
        return m;
    }
}
