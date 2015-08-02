package edu.multicore.queues.utils;

import edu.multicore.queues.MyQueue;

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

    public void runBenchmark(){

        long start = System.nanoTime();

        Thread[] pthreads = new Thread[numProducers];
        Thread[] cthreads = new Thread[numConsumers];

        for(int i = 0; i < numConsumers; i++){
            cthreads[i] = new Thread(consumers[i]);
            cthreads[i].start();
        }

        for(int i = 0; i < numProducers; i++){
            pthreads[i] = new Thread(producers[i]);
            pthreads[i].start();
        }

        for(int i = 0; i < numProducers; i++){
            try {
                pthreads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        for(int i = 0; i < numConsumers; i++){
            try {
                cthreads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        long end = System.nanoTime();
        double time = (double)(end - start)/ (1000000000.0) ;

        System.out.println("Time spent: " + time + " secs");
    }
}


