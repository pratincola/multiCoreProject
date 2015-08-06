package edu.multicore.queues.utils;

import edu.multicore.queues.MyQueue;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.IntUnaryOperator;


/**
 * Created by pratik1 on 8/3/15.
 */
public class RoundRobin<M> {

    int numQueues;
    private final List<MyQueue> queues;
    private AtomicInteger currentConsumer = new AtomicInteger(0);
    private AtomicInteger currentProducer = new AtomicInteger(0);
    private int currQueue = 0;
    private boolean rrProducer;
    private boolean rrConsumer;

    public RoundRobin(int numQueues, Class c){

        this.numQueues = numQueues;
        this.queues = new ArrayList<>();
        Settings.getInstance().setConsumerRr(true);

        for(int i = 0; i < numQueues; i++){
            try {
                MyQueue m = (MyQueue) c.newInstance();
                queues.add(m);

            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    public RoundRobin(int numQueues, Class c, boolean rrConsumer, boolean rrProducer){

        this.numQueues = numQueues;
        this.queues = new ArrayList<>();
        this.rrConsumer = rrConsumer;
        this.rrProducer = rrProducer;

        Settings s = Settings.getInstance();
        if(rrConsumer) s.setConsumerRr(true);
        if(rrProducer) s.setRrProducer(true);

        for(int i = 0; i < numQueues; i++){
            try {
                MyQueue m = (MyQueue) c.newInstance();
                queues.add(m);

            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

    }


    public MyQueue getFirst(){
        return queues.get(0);
    }

    public  MyQueue getCurrConsumer(){
        return queues.get(currentConsumer.get());
    }
    public  MyQueue getCurrProducer(){
        return queues.get(currentProducer.get());
    }

    public AtomicInteger getCurrentConsumerQueueId() {
        return currentConsumer;
    }
    public AtomicInteger getCurrentProducerQueueId() {
        return currentProducer;
    }

    public MyQueue getQueue(int i){
        currQueue = (i)%queues.size();
        return queues.get(currQueue);
    }

    public MyQueue getNextQueueConsumer(){

        IntUnaryOperator i = (x) -> (x+1)%queues.size();
        currentConsumer.getAndUpdate(i);

        return queues.get(currentConsumer.get());

    }

    public MyQueue getNextQueueProducer(){
        IntUnaryOperator i = (x) -> (x+1)%queues.size();
        currentProducer.getAndUpdate(i);

        return queues.get(currentProducer.get());

    }


    public boolean isRrProducer() {
        return rrProducer;
    }


    public boolean isRrConsumer() {
        return rrConsumer;
    }


}
