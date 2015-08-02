package hw4.q1;

import edu.multicore.queues.MyQueue;

import java.util.concurrent.atomic.AtomicReference;

public class LockFreeQueue<T> implements MyQueue<T> {
    private AtomicReference<SentinelNode<T>> head, tail;
    public LockFreeQueue(){
        SentinelNode<T> dummy = new SentinelNode<>(null);
        head = new AtomicReference<>(dummy);
        tail = new AtomicReference<>(dummy);
    }

    public boolean enq(T value) {
        SentinelNode<T> new_element = new SentinelNode<>(value);
        new_element.next = null;
        // Keep trying until enqueue is done
        while(true){
            SentinelNode<T> local_tail = tail.get();
            SentinelNode<T> next_element = local_tail.next;
            if(local_tail == tail.get()){
                //Tail pointing to last node?
                if(next_element == null){
                    if (tail.compareAndSet(next_element, new_element))
                        break;
                }
                //Tail was not pointing to last node, so try to swing it over
                else
                    tail.compareAndSet(local_tail, next_element);
            }
        }
        //After a successful enqueue, swing tail to the inserted node
        tail.compareAndSet(tail.get(), new_element);
        return true;
    }

    public T deq() {
        //Keep trying until dequeue is done
        T return_val;
        while(true){
            SentinelNode<T> local_head = head.get();
            SentinelNode<T> local_tail = tail.get();
            SentinelNode<T> next_element = local_head.next;
            //Are head, tail, and next consistent?
            if (local_head == head.get()){
                //Is queue empty or tail falling behind?
                if (local_head == local_tail){
                    //Is queue empty?
                    if (next_element == null) {
                        return null;
                    }
                    //try to advance tail since it is lagging
                    tail.compareAndSet(local_tail, next_element);
                }
                //You are ready for dequeue operation
                else {
                    //Read value before atomic CAS operation, else
                    //someone else will free the next node
                    return_val = next_element.value;
                    //Try to swing head to the next node, and complete the dequeue
                    if (head.compareAndSet(local_head, next_element)){
                        break;
                    }
                }
            }
        }
        return return_val;
    }

    private static class SentinelNode<T>{
        final T value;
        volatile SentinelNode<T> next;
        SentinelNode(T value){
            this.value = value;
        }
    }
}
