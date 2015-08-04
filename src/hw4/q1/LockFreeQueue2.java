package hw4.q1;

import edu.multicore.queues.MyQueue;

import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicStampedReference;

public class LockFreeQueue2<T> implements MyQueue<T> {
    private AtomicStampedReference<LockFreeQueue2.Node<T>> head, tail;

    public LockFreeQueue2(){
        LockFreeQueue2.Node<T> dummy = new LockFreeQueue2.Node(null, null);
        head = new AtomicStampedReference<Node<T>>(dummy, 0);
        tail = new AtomicStampedReference<Node<T>>(dummy, 0);
    }

    public void seek(){
        Node<T> h = head.getReference();
        while(h != null){
            System.out.println("Seek: " + h.item);
            h = h.next.getReference();
        }
    }

    public boolean enq(T value) {
        Node<T> new_element = new Node<>(value, null);
        // Keep trying until enqueue is done
        Node<T> local_tail;
        int[] stampHolder;
        Node<T> next_element;
        while(true){
            local_tail = tail.getReference();
            stampHolder = new int[1];
            next_element = local_tail.next.getReference();

            if(local_tail == tail.getReference()){
                //Tail pointing to last node?
                if(next_element == null){
                    if(tail.getReference().next.compareAndSet(next_element, new_element, stampHolder[0], stampHolder[0] + 1))
                        break;
                }
                //Tail was not pointing to last node, so try to swing it over
                else
                    // ------------------------------Possible bug...---------------------------------------------
                    tail.compareAndSet(local_tail, next_element, next_element.next.getStamp() , tail.getStamp() + 1);
            }
        }
        //After a successful enqueue, swing tail to the inserted node
        // ------------------------------Possible bug...---------------------------------------------
        boolean b = tail.compareAndSet(local_tail, new_element, stampHolder[0], tail.getStamp() + 1);
        System.out.println("changing tail pointer: " + b);
        return true;
    }

    public T deq() {
        //Keep trying until dequeue is done
        T return_val;
        Node<T> local_head;
        Node<T> local_tail;
        Node<T> next_element;
        while(true){
            local_head = head.getReference();
            local_tail = tail.getReference();
            next_element = local_head.next.getReference();
            //Are head, tail, and next consistent?
            if (local_head == head.getReference()){
                //Is queue empty or tail falling behind?
                if (local_head == local_tail){
                    //Is queue empty?
                    if (next_element == null) {
                        return null;
                    }
                    //try to advance tail since it is lagging
                    // ------------------------------Possible bug...---------------------------------------------
                    tail.compareAndSet(local_tail,next_element, next_element.next.getStamp(), tail.getStamp()+1);
                }
                //You are ready for dequeue operation
                else {
                    //Read value before atomic CAS operation, else
                    //someone else will free the next node
                    return_val = next_element.item;
                    //Try to swing head to the next node, and complete the dequeue
                    // ------------------------------Possible bug...---------------------------------------------
                    if (head.compareAndSet(local_head, next_element, next_element.next.getStamp(), head.getStamp() + 1)){
                        break;
                    }
                }
            }
        }
        return return_val;
    }

    @Override
    public boolean isEmpty() throws NoSuchMethodException {
        throw new NoSuchElementException("Not implemented");
    }


    private static class Node <T> {
        final T item;
        volatile AtomicStampedReference<Node<T>> next;

        public Node(T item, LockFreeQueue2.Node<T> next) {
            this.item = item;
            this.next = new AtomicStampedReference<Node<T>>(next,0);
        }
    }


    public static void main(String[] args){
        LockFreeQueue2 l = new LockFreeQueue2();
        System.out.println(l.head);
        System.out.println(l.tail);
        l.seek();
        System.out.println(l.enq(5));
        System.out.println(l.enq(6));

        l.seek();

//
//        System.out.println(l.enq(8));
//        System.out.println(l.enq(9));
//        System.out.println(l.enq(10));
//        l.seek();
    }
}
