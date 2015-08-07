package edu.multicore.queues.jqueues;


import edu.multicore.queues.utils.Bin;

public class SimpleLinear<T> implements PQueue<T>{
    int range;
    Bin<T>[] pqueue;
    public SimpleLinear(int range) {
        this.range = range;
        pqueue = (Bin<T>[])new Bin[range];
        for (int i = 0; i < pqueue.length; i++){
            pqueue[i] = new Bin<T>();
        }
    }

    /**
     * Add item to heap.
     * @param item item to add
     * @param key item's value
     */
    public void add(T item, int key) {
        pqueue[key].put(item);
    }


    /**
     * Return and remove least item
     * @return least item
     */
    public T removeMin() {
        for (int i = 0; i < range; i++) {
            T item = pqueue[i].get();
            if (item != null) {
                return item;
            }
        }
        return null;
    }

    @Override
    public boolean isEmpty() {
        boolean result = true;

        for (int i = 0; i < range; i++) {
            if (!pqueue[i].isEmpty()){
                result = false;
               break;
            }
        }
        return result;
    }
}
