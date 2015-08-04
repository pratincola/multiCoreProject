package edu.multicore.queues.utils;

import edu.multicore.queues.MyQueue;

/**
 * Created by pratik1 on 7/27/15.
 */
public class Consumer extends Worker {

    MyQueue q;
    final int numMessages;
    int id;
    RoundRobin rr;

    public Consumer(int numMessages, MyQueue q, int id){
        this.q = q;
        this.numMessages = numMessages;
        this.id = id;
    }

    public Consumer(int numMessages, MyQueue q, int id, RoundRobin rr){
        this.q = q;
        this.numMessages = numMessages;
        this.id = id;
        this.rr = rr;
    }

    public void setQueue(MyQueue queue){
        this.q = queue;
    }


    @Override
    public void run() {
        Settings s = Settings.getInstance();

        int i = 0;
        boolean log = s.isLog();

        while(i < numMessages ){
            try{
//                Message m = (Message);
                Integer m = (Integer) q.deq();
                if(m == null){
//                    Thread.yield();
                }
                else {
                    if(log)
                        System.out.println("Consumer " + id + " deque: " + m.toString());
                }
            }
            catch (Exception e){
                if(log)
                    System.out.println("Consumer exception: " + e);
            }
            i++;
            if(s.isRr()){
                q = rr.getNext();
            }
        }
        System.out.println("Stopped consumer");

    }

}
