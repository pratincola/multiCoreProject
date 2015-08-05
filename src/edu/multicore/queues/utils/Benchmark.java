package edu.multicore.queues.utils;

import edu.multicore.queues.MyQueue;

import java.security.InvalidAlgorithmParameterException;
import java.util.InvalidPropertiesFormatException;
import java.util.Queue;

/**
 * Created by jtharp on 7/29/2015.
 */
public class Benchmark {

    private final int numProducers;
    private final int numConsumers;
    private final int iterations;
    private final MyQueue queue;

    private Producer[] producers;
    private Consumer[] consumers;

    private RoundRobin roundRobin;

    private long totalTime;
    private long totalEmpty;
    private long totalFull;
    final int benchIterations = 10;

    public Benchmark(int numProducers, int numConsumers, int iterations, MyQueue queue){
        this.numProducers = numProducers;
        this.numConsumers = numConsumers;
        this.iterations = iterations;
        this.queue = queue;

        producers = new Producer[numProducers];
        consumers = new Consumer[numConsumers];

        for(int i = 0; i < numProducers; i++){
            producers[i] = new Producer(iterations / numProducers, queue, i);
        }

        for(int i = 0; i < numConsumers; i++){
            consumers[i] = new Consumer( iterations ,queue, i);
        }
    }
	
	public Benchmark(Producer[] producers, Consumer[] consumers, MyQueue<Integer> queue, int iterations){
        this.numProducers = producers.length;
        this.numConsumers = consumers.length;
        this.iterations = iterations;
        this.queue = queue;
        this.producers = producers;
        this.consumers = consumers;
    }

    public Benchmark(int numProducers, int numConsumers, int iterations, RoundRobin r){
        Settings.getInstance().setRr(true);
        this.numProducers = numProducers;
        this.numConsumers = numConsumers;
        this.iterations = iterations;
        this.queue = r.getFirst();


        producers = new Producer[numProducers];
        consumers = new Consumer[numConsumers];

        for(int i = 0; i < numProducers; i++){
            producers[i] = new Producer(iterations / numProducers, r.getNext(), i);
        }

        for(int i = 0; i < numConsumers; i++){
            consumers[i] = new Consumer( iterations, queue, i, r);
        }

    }

    public void runBenchmark(){
//        Settings.getInstance().setLog(false);
        for(int k = 0; k < benchIterations; k++) {

            long start = System.nanoTime();

            IdThread[] pthreads = new IdThread[numProducers];
            IdThread[] cthreads = new IdThread[numConsumers];

            int i = 0;
            for (i = 0; i < numConsumers; i++) {
                cthreads[i] = new IdThread(i, consumers[i]);
                consumers[i].start();
                cthreads[i].start();
            }

            for (int j = i; j < numProducers + i; j++) {
                pthreads[j - i] = new IdThread(j, producers[j - i]);
                pthreads[j - i].start();
            }

            for (i = 0; i < numProducers; i++) {
                try {
                    pthreads[i].join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

//        for(i = 0; i < numConsumers; i++){
//            try {
//                cthreads[i].join();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
            try {
                while (!queue.isEmpty()) {
                }
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }

            long end = System.nanoTime();
            double time = (double) (end - start) / (1000000.0);

            totalTime += time;

            for(i = 0; i < numConsumers; i++){
                consumers[i].stop();
                totalEmpty += consumers[i].getNumEmpty();
            }

            for (i = 0; i < numProducers; i++) {
                totalFull += producers[i].getNumFull();
            }

            for(i = 0; i < numConsumers; i++){
                try {
                    cthreads[i].join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        Settings.getInstance().setRr(false);
        //System.out.println("Time spent: " + time + " milliseconds");

        System.out.println("Time spent: " + getAverageTime() + " milliseconds");
        System.out.println("Number of times queue was full: " + getAverageFull());
        System.out.println("Number of times queue was empty: " + getAverageEmpty());
    }

    public long getAverageTime(){
        return totalTime / benchIterations;
    }

    public long getAverageFull(){
        return totalFull / benchIterations;
    }

    public long getAverageEmpty(){
        return totalEmpty / benchIterations;
    }
}


