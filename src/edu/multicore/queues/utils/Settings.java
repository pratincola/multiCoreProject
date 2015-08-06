package edu.multicore.queues.utils;

/**
 * Created by jtharp on 8/3/2015.
 */
public class Settings {


    private static Settings obj;
    private boolean log;
    private boolean rrConsumer = false;
    private boolean rrProducer = false;

    public static  synchronized Settings getInstance(){
        if(obj == null){
            obj = new Settings();
        }
        return obj;
    }

    public void setConsumerRr(boolean rr) {
        this.rrConsumer = rr;
    }

    public boolean isConsumerRr() {
        return rrConsumer;
    }

    public boolean isRrProducer() {
        return rrProducer;
    }

    public void setRrProducer(boolean rrProducer) {
        this.rrProducer = rrProducer;
    }

    public boolean isLog() {
        return log;
    }

    public void setLog(boolean log) {
        this.log = log;
    }
}
