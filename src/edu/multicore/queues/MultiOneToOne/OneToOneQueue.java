package edu.multicore.queues.MultiOneToOne;

import edu.multicore.queues.MyQueue;
import edu.multicore.queues.utils.Consumer;
import edu.multicore.queues.utils.Producer;
import hw4.q1.LockFreeQueue;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.AbstractQueue;
import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.Queue;

/**
 * Created by jtharp on 8/1/2015.
 */
public class OneToOneQueue<E> extends AbstractQueue<E> implements MyQueue<E> {

    MyQueue[] queues;
    int currentRotation;

    public OneToOneQueue(Producer[] producers, Consumer[] consumers, int size) throws Exception {
        if(producers.length != consumers.length)
            throw new Exception("Producers and Consumers must be the same length");

        queues = new MyQueue[producers.length];
        currentRotation = 0;

        for(int i = 0; i < producers.length; i++){

            //Not a thread safe queue
            //Not needed for a one to one producer to consumer
//            MyQueue<E> q = new WrappedArrayDeq<E>(size);
            MyQueue<E> q = new LockFreeQueue<>();
            queues[i] = q;

            producers[i].setQueue(this);
            consumers[i].setQueue(q);
        }
    }

    @Override
    public Iterator<E> iterator() {
        return null;
    }

    @Override
    public int size() {
        //Might not be the exact size due to threading
        //and multiple queues.
        //Only way to get exact size is to introduce a lock on each queue
        //Get the size of each queue.
        //And unlock

        int size = 0;
        for(int i = 0; i < queues.length; i++){
            size += ((WrappedArrayDeq)queues[i]).size();
        }
        return size;
    }

    @Override
    public boolean offer(E e) {
        boolean offered = false;

        currentRotation = (currentRotation + 1) % queues.length;

        for(int i = 0; i < queues.length; i++){
            if(queues[currentRotation].enq(e)){
                offered = true;
                break;
            }
            currentRotation = (currentRotation + 1)  % queues.length;
        }
        return offered;
    }

    @Override
    public E poll() {
        //In this data structure individual consumers have their own queue to poll from
        throw new NotImplementedException();
//        return null;
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

    @Override
    public boolean isEmpty(){
        for(int i = 0; i < queues.length; i++){
            try {
                if(!queues[i].isEmpty())
                    return false;
            } catch (NoSuchMethodException e) {
                return false;
            }
        }
        return true;
    }
}
