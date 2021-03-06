package edu.multicore.queues.MultiWorkStealing;

import edu.multicore.queues.MyQueue;
import edu.multicore.queues.utils.Consumer;
import edu.multicore.queues.utils.Producer;
import edu.multicore.queues.utils.QueueEnum;
import hw4.q1.LockFreeQueue;
import hw4.q1.LockQueue;
import hw4.q2.WrappedCoarseGrainedListSet;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.AbstractQueue;
import java.util.Iterator;

/**
 * Created by jtharp on 8/3/2015.
 */
public class WorkStealingQueue<E> extends AbstractQueue<E> implements MyQueue<E>   {
    private final Producer[] producers;
    private final Consumer[] consumers;
    MyQueue[] queues;
    int currentRotation;

    public WorkStealingQueue(Producer[] producers, Consumer[] consumers, int size, QueueEnum qtype) throws Exception {
        queues = new MyQueue[consumers.length];
        currentRotation = 0;

        for(int i = 0; i < consumers.length; i++){

            MyQueue<E> q;
            if(qtype == QueueEnum.WrappedArrayBlocking)
                q = new WrappedArrayDeq<E>(size);
            else if(qtype == QueueEnum.LockFree)
                q = new LockFreeQueue<>(size);
            else if(qtype == QueueEnum.Locked){
                q = new LockQueue<>(size);
            }
            else{
                throw new Exception("No valid queue specified");
            }

            queues[i] = q;

            consumers[i].setQueue(this);
        }

        for(int i = 0; i < producers.length; i++){

            producers[i].setQueue(this);
        }



        this.producers = producers;
        this.consumers = consumers;
    }

    @Override
    public Iterator<E> iterator() {
        return null;
    }

    @Override
    public int size() {

        throw new NotImplementedException();
    }

    @Override
    public boolean isEmpty(){
        for(int i = 0; i < queues.length; i++){
            try {
                if(!queues[i].isEmpty()){
                    return false;
                }
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    @Override
    public boolean offer(E e) {
        int i = this.getQueueIndex();
        return queues[i].enq(e);
    }

    @Override
    public E poll() {

        //This depends if we want to try to use a single threaded queue
        //or non thread safe queue for each consumer.
        //One reason to do that might be performance. The normal case is
        //each consumer uses their own queue and the odd case is stealing from
        //another queue. So only that case needs to be locked
        //For now just use a thread safe queue

        int i = this.getQueueIndex();

        Object obj = queues[i].deq();
        if(obj == null){
            //work steal
            for(int j = 0; j < queues.length; j++){
                if(j == i)
                    continue;
                obj = queues[j].deq();
                if(obj != null)
                    return (E) obj;
            }
        }

        return null;
    }

    private int getQueueIndex(){
        //Hack to get the correct queue index.
        //Relies on the fact Consumer Threads are created first
        //in the benchmark code.
        int id = (int)Thread.currentThread().getId();
        if(id > this.consumers.length - 1){
            id = id - this.consumers.length;
        }
        return id;
    }

    @Override
    public boolean enq(E value) {
        return this.offer(value);
    }

    @Override
    public E deq() {
        return this.poll();
    }

    @Override
    public E peek() {
        E item;
        int peekRotation = (currentRotation + 1) % queues.length;
        for(int i = 0; i < queues.length; i++){
            if(null != (item = ((WrappedArrayDeq<E>)queues[peekRotation]).peek())){
                return item;
            }
            peekRotation = (peekRotation + 1) % queues.length;
        }
        return null;
    }
}
