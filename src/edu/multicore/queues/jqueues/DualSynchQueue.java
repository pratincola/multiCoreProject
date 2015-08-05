package edu.multicore.queues.jqueues;

import edu.multicore.queues.MyQueue;

import java.util.EmptyStackException;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by rkhunte on 8/4/2015.
 * Reference: Herlihy, Shavit et al, The Art of Multiprocessor Programming
 */
public class DualSynchQueue<T> implements MyQueue<T> {
    AtomicReference<Node> head, tail;
    public DualSynchQueue(){
        Node dummy = new Node(null, NodeType.ITEM);
        head = new AtomicReference<Node>(dummy);
        tail = new AtomicReference<Node>(dummy);
    }
    public boolean enq(T value){
        Node offer = new Node(value, NodeType.ITEM);
        while (true) {
            Node local_tail = tail.get();
            Node local_head = head.get();
            //Check if queue is empty or if it contains items that are already enqueued
            if (local_head == local_tail || local_tail.type == NodeType.ITEM) {
                //If so, check if the values are consistent
                Node n = local_tail.next.get();
                if (local_tail == tail.get()){
                    //Ensure that it is still the last
                    if (n != null){
                        //Not the last, advance tail, and try again
                        tail.compareAndSet(local_tail, n);
                    }
                    //If it is the last, go ahead and set the next to point to the new node
                    else if (local_tail.next.compareAndSet(n, offer)) {
                        //Try to advance the tail
                        tail.compareAndSet(local_tail, offer);
                        //Wait for dequeuer to take the item
                        while (offer.item.get() == value);
                        //Housekeeping: first make head consistent
                        local_head = head.get();
                        //If you are indeed the next node, remove thyself by making thy a sentinel
                        if (offer == local_head.next.get())
                            head.compareAndSet(local_head, offer);
                        return true;
                    }
                }
            }
            //Reservations by dequeuers are waiting to be fulfilled
            else {
                //Get the next node
                Node n = local_head.next.get();
                //First make sure head and tail are consistent, and the next field doesn't point to null ie empty queue
                if (local_tail != tail.get() || local_head != head.get() || n == null){
                    continue;
                }
                //Try to put your item for the dequeuer to take
                boolean success = n.item.compareAndSet(null, value);
                //Try to advance head anyway
                head.compareAndSet(local_head, n);
                //If successful return, else retry
                if (success)
                    return true;
            }
        }
    }
    public T deq() throws EmptyStackException {
        T return_val;
        Node offer = new Node(null, NodeType.RESERVATION);
        while (true) {
            Node local_head = head.get();
            Node local_tail = tail.get();
            //Check if queue is empty or if it contain reservations already for dequeueing
            if (local_head == local_tail || local_tail.type == NodeType.RESERVATION) {
                //If so confirm that the values are consistent
                Node n = local_tail.next.get();
                if (local_tail == tail.get()){
                    //Ensure that it is still the last
                    if (n != null) {
                        //Not the last, advance tail, and try again
                        tail.compareAndSet(local_tail, n);
                    }
                    //If it is the last, go ahead, and set the next to point to the new node
                    else if (local_tail.next.compareAndSet(n, offer)) {
                        //Try to advance tail
                        tail.compareAndSet(local_tail, offer);
                        //Wait for enquer to put an item
                        while (offer.item.get() == null);
                        return_val = offer.item.get();
                        //Housekeeping: first make head consistent
                        local_head = head.get();
                        //If you are indeed the next node, remove thyself by making thy a sentinel
                        if (offer == local_head.next.get())
                            head.compareAndSet(local_head, offer);
                        return return_val;
                    }
                }
            }
            //Items deposited by enquers are waiting to be dequeued
            else {
                //Get the next node
                Node n = local_head.next.get();
                //First make sure head and tail are consistent, and the next field doesn't point to null ie empty queue
                if (local_tail != tail.get() || local_head != head.get() || n == null){
                    continue;
                }
                //Try to take item from the enquer
                return_val = n.item.get();
                boolean success = n.item.compareAndSet(return_val, null);
                //Try to advance head anyway
                head.compareAndSet(local_head, n);
                if(success) {
                    return return_val;
                }
            }
        }
    }

    @Override
    public boolean isEmpty() throws NoSuchMethodException {
       throw new NoSuchElementException();
    }

    private enum NodeType {ITEM, RESERVATION};
    private class Node {
        volatile NodeType type;
        volatile AtomicReference<T> item;
        volatile AtomicReference<Node> next;
        Node(T myItem, NodeType myType) {
            item = new AtomicReference<T>(myItem);
            next = new AtomicReference<Node>(null);
            type = myType;
        }
    }
}


