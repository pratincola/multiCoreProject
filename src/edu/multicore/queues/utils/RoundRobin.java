package edu.multicore.queues.utils;

import edu.multicore.queues.MyQueue;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by pratik1 on 8/3/15.
 */
public class RoundRobin<M> {

    int numQueues;
    private final List<MyQueue> queues;
    private final ThreadLocal<MyQueue> threadQueue = new ThreadLocal<>();
    private int current = 0;


    public RoundRobin(int numQueues, Class c){

        this.numQueues = numQueues;
        this.queues = new ArrayList<>();
        Settings.getInstance().setRr(true);

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
        threadQueue.set(queues.get(current));
    }

    public MyQueue getFirst(){
        return queues.get(0);
    }

    public  MyQueue getCurr(){
        return queues.get(current);
    }

    public MyQueue getNext(){

        current = (current + 1) % queues.size();

        return queues.get(current);

    }
}
