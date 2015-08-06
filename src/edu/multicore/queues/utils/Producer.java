package edu.multicore.queues.utils;

import edu.multicore.queues.MyQueue;

import java.util.Queue;

/**
 * Created by pratik1 on 7/27/15.
 */
public class Producer extends Worker{

    MyQueue q;
    int ctr = 0;
    final int numIterations;
    private long startTime;
    private long endTime;
    private long elapsedTime;
    private int id;
    private long totalFull;


    public Producer(int numIterations, MyQueue q, int id){
        this.q = q;
        this.numIterations = numIterations;
        this.id = id;

        startTime = 0;
        endTime = 0;
        elapsedTime = 0;

    }

    public long getStartTime() {
        return startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public long getElapsedTime() {
        return elapsedTime;
    }

    public void setQueue(MyQueue queue){
        this.q = queue;
    }

    @Override
    public void run() {
        totalFull = 0;
        int i = 0;
        boolean log = Settings.getInstance().isLog();

        startTime = System.nanoTime();
        while(i < numIterations ) {
            Message p = produce();
            try {
                boolean enqueued = q.enq(p.getId());
                if(!enqueued){
                    totalFull++;
                }
                if(log)
                    System.out.println("Producer " + id + " enq: " + p.toString());
            }
            catch (Exception e){
                if(log)
                    System.out.println("Producer exception: " + e);
            }
//            try {
//                Thread.sleep(0, 1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
            i++;
        }
        endTime = System.nanoTime();
        elapsedTime = endTime - startTime;
        if(log)
            System.out.println("producer" + id + " time took: " + elapsedTime);
    }

    private Message produce(){
        Message m = new Message(++ctr, "Example message.");
        return m;
    }

    public long getNumFull() {
        return totalFull;
    }
}
