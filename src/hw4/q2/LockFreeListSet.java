package hw4.q2;


import java.util.concurrent.atomic.AtomicMarkableReference;

public class LockFreeListSet<T> implements ListSet<T> {
    final Node<T> head, tail;
    public LockFreeListSet(){
        head = new Node<>(null, Integer.MIN_VALUE);
        tail = new Node<>(null, Integer.MAX_VALUE);
        while(!head.next.compareAndSet(null, tail, false, false));
    }
    public boolean add(T value) {
        Node<T> predecessor, current, successor;
        boolean[] marked = {false};
        boolean successor_check;
        int add_key = value.hashCode();
        retry: while (true) {
            //Every thread's local predecessor and current nodes
            predecessor = head;
            current = predecessor.next.getReference();
            while(true) {
                successor = current.next.get(marked);
                //check if the next node is marked for deletion
                while (marked[0]) {
                    successor_check = predecessor.next.compareAndSet(current, successor, false, false);
                    //If you cant set traverse again
                    if (!successor_check) continue retry;
                    //If you have checked update the current and successor nodes until you reach an unmarked one
                    current = successor;
                    successor = current.next.get(marked);
                }
                //You have current with the highest key with an unmarked successor node
                if (current.key >= add_key){
                    //If the key is equal you are trying to add a duplicate
                    if (current.key == add_key) return false;
                    else{
                        //Else add the element to the list
                        Node<T> new_addition = new Node<>(value, add_key);
                        new_addition.next = new AtomicMarkableReference<>(current, false);
                        if (predecessor.next.compareAndSet(current, new_addition, false, false))
                            return true;
                    }
                }
                predecessor = current;
                current = successor;
            }
        }
    }
    public boolean remove(T value) {
        Node<T> predecessor, current, successor;
        boolean[] marked = {false};
        boolean successor_check, mark_for_rem;
        int rem_key = value.hashCode();
        retry: while (true) {
            //Every thread's local predecessor and current nodes
            predecessor = head;
            current = predecessor.next.getReference();
            while(true) {
                successor = current.next.get(marked);
                //check if the next node is marked for deletion
                while (marked[0]) {
                    successor_check = predecessor.next.compareAndSet(current, successor, false, false);
                    if (!successor_check) continue retry;
                    current = successor;
                    successor = current.next.get(marked);
                }
                if (current.key >= rem_key){
                    //If the key is not equal you are trying to remove a non-existing node
                    if (current.key != rem_key) return false;
                    else{
                        //Mark the element for deletion
                        Node<T> remove_this = current.next.getReference();
                        mark_for_rem = current.next.attemptMark(remove_this, true);
                        if (!mark_for_rem) continue;
                        predecessor.next.compareAndSet(current, remove_this, false, false);
                        return true;
                    }
                }
                predecessor = current;
                current = successor;
            }
        }
    }
    public boolean contains(T value) {
        boolean [] marked = {false};
        int key = value.hashCode();
        Node<T> current = head;
        while(current.key < key) {
            current = current.next.getReference();
            Node successor = current.next.get(marked);
        }
        return (current.key == key && !marked[0]);
    }

    static class Node<T> {
        final T item;
        final int key;
        volatile AtomicMarkableReference<Node<T>> next;
        public Node(T item, int key) {
            this.item = item;
            this.key = key;
            this.next = new AtomicMarkableReference<Node<T>>(null, false);
        }
    }
}
