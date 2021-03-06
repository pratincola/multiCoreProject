package edu.multicore.queues.utils;

import edu.multicore.queues.MyQueue;

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
    RoundRobin rr;


    public Producer(int numIterations, MyQueue q, int id){
        this.q = q;
        this.numIterations = numIterations;
        this.id = id;

        startTime = 0;
        endTime = 0;
        elapsedTime = 0;

    }

    public Producer(int numIterations, MyQueue q, int id, RoundRobin rr){
        this.q = q;
        this.numIterations = numIterations;
        this.id = id;
        this.rr = rr;
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
        Settings s = Settings.getInstance();
        boolean log = s.isLog();

        startTime = System.nanoTime();
        while(i < numIterations ) {
            Message p = produce();
            try {
                boolean enqueued = q.enq(i);
                if(!enqueued){
                    --ctr;
                    totalFull++;
                }
				else{
					if(log)
						System.out.println("Producer " + id + " enq: " + i);
					i++;
				}
                if(log) System.out.println("Producer " + id + " Queue: " + ((rr==null)? id : (!rr.isRrProducer())?id:rr.getCurrentProducerQueueId()));
                if (s.isRrProducer()) {
                    q = rr.getNextQueueProducer();
                }

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
