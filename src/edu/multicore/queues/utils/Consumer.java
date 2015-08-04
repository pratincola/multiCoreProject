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

        if(true){
            rrConsume();
        }
        else{
            consume();
        }

    }

    public void rrConsume(){
        int i = 0;
        while(i < numMessages){
            Integer m = (Integer)q.deq();
            System.out.println("Consumer " + id + " deque: " + m);
            q = rr.getNext();
        }
    }


    public void consume(){
        int i = 0;
        boolean log = Settings.getInstance().isLog();

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
        }
        System.out.println("Stopped consumer");

    }
}
