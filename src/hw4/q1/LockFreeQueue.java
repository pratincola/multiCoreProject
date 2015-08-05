package hw4.q1;


import edu.multicore.queues.MyQueue;
import edu.multicore.queues.utils.EmptyException;

import java.util.concurrent.atomic.AtomicReference;

public class LockFreeQueue<T> implements MyQueue<T> {
    private AtomicReference<LockFreeQueue.Node> head, tail;
    private Node dummy;
    public LockFreeQueue(){
        dummy = new LockFreeQueue.Node(null);
        head = new AtomicReference<LockFreeQueue.Node>(dummy);
        tail = new AtomicReference<LockFreeQueue.Node>(dummy);
    }



    @Override
    public boolean enq(T value) {

        Node node = new Node(value);

        while(true){
            Node last = tail.get();
            Node next = last.next.get();
            if(last == tail.get()){
                if(next == null){
                    if(last.next.compareAndSet(next, node)){
                        tail.compareAndSet(last, node);
                        return true;
                    }
                }
                else{
                    tail.compareAndSet(last, next);
                }
            }
        }
    }

    @Override
    public T deq() {

        while(true){
            Node first = head.get();
            Node last = tail.get();
            Node next = null;
            if(first.next != null)
                next = first.next.get();
            if(first == head.get()){
                if(first == last){
                    if(next == null){
//                        try {
//                            throw new EmptyException("Empty");
//                        } catch (EmptyException e) {
////                            e.printStackTrace();
//                        }
                      return null;        // return null or throw EmptyException..
                    }
                    tail.compareAndSet(last, next);
                }
                else{
                    T value = null;
                    if(next != null)
                        value = next.value;
                    if(next == null)
                        next = dummy;
                    if(head.compareAndSet(first, next)){
                        return value;
                    }

                }
            }
        }
    }

    @Override
    public boolean isEmpty() throws NoSuchMethodException {
        return (head.get().next.get() == null);
    }

    public class Node {

        public T value;
        public AtomicReference<Node> next;

        public Node(T value) {
            this.value = value;
            this.next = new AtomicReference<Node>(null);
        }
    }
    public static void main(String[] args) {
        LockFreeQueue l = new LockFreeQueue();
        System.out.println(l.head);
        System.out.println(l.tail);
        System.out.println(l.enq(5));
        System.out.println(l.enq(6));
        System.out.println(l.enq(7));
        System.out.println(l.enq(8));
        System.out.println(l.deq());
        System.out.println(l.deq());
        System.out.println(l.enq(9));
        System.out.println(l.enq(10));
        System.out.println(l.deq());
        System.out.println(l.deq());
        System.out.println(l.deq());

    }

}