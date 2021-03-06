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
    private boolean stop;
    private long numEmpty;

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
        numEmpty = 0;
        int i = 0;
        boolean log = s.isLog();
        stop = false;

        while(!stop){
            try{
//                Message m = (Message);
                Object m = q.deq();
                if(m == null){
                    //Thread.yield();
                    numEmpty++;
                }
                if(log) System.out.println("Consumer " + id + " Queue: " + ((rr==null)? id:((!rr.isRrConsumer())?id:rr.getCurrentConsumerQueueId())) + " deque: " + ((m==null)? "null":m.toString()));
                if (s.isConsumerRr()) {
                    q = rr.getNextQueueConsumer();
                }

            }
            catch (Exception e){
                if(log)
                    System.out.println("Consumer exception: " + e);
                e.printStackTrace();
            }
//            try {
//                Thread.sleep(0, 1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
            i++;
        }
        if(log) System.out.println("Stopped consumer");

    }

    public void stop() {
        this.stop = true;
    }

    public long getNumEmpty(){
        return numEmpty;
    }

    public void start() {
        this.stop = false;
    }
}
