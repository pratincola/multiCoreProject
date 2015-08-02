package edu.multicore.queues.multipleProducerMultipleConsumer;

/**
 * Created by pratik1 on 7/28/15.
 */


public class LLSC {
    ObjPointer p;

    public LLSC(Object x) {
        p.obj = x;
        p.version = 0;
    }

    public synchronized void load_linked(ObjPointer local) {
        local.obj = p.obj;
        local.version = p.version;
    }

    public synchronized boolean store_conditional(ObjPointer local, Object newObj) {

        if ((p.obj == local.obj) && (p.version == local.version)) {
            p.obj = newObj;
            p.version++;

            return true;
        }
        return false;
    }
}