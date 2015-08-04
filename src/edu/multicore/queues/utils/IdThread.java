package edu.multicore.queues.utils;

/**
 * Created by jtharp on 8/3/2015.
 */
public class IdThread extends Thread {
    private final int myid;
    private final Runnable runnable;

    public IdThread(int threadId, Runnable runnable){
        super();
        this.myid = threadId;
        this.runnable = runnable;
    }

    @Override
    public void run()
    {
        runnable.run();
    }

    @Override
    public long getId(){
        return this.myid;
    }
}
