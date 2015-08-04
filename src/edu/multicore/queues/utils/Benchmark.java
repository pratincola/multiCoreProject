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

        long start = System.nanoTime();

        IdThread[] pthreads = new IdThread[numProducers];
        IdThread[] cthreads = new IdThread[numConsumers];

        int i = 0;
        for(i = 0; i < numConsumers; i++){
            cthreads[i] = new IdThread(i, consumers[i]);
            cthreads[i].start();
        }

        for(int j = i; j < numProducers + i; j++){
            pthreads[j-i] = new IdThread(j, producers[j-i]);
            pthreads[j-i].start();
        }

        for(i = 0; i < numProducers; i++){
            try {
                pthreads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        for(i = 0; i < numConsumers; i++){
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


