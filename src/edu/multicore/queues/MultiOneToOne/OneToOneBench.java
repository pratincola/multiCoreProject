package edu.multicore.queues.MultiOneToOne;

import edu.multicore.queues.utils.Benchmark;
import edu.multicore.queues.utils.Consumer;
import edu.multicore.queues.utils.Producer;
import edu.multicore.queues.utils.Settings;

public class OneToOneBench {

    int numProducers;

    public static void main(String args[]) throws Exception {
        int iterations = 1000000;

        Settings.getInstance().setLog(false);

        Producer[] producers = new Producer[5];
        Consumer[] consumers = new Consumer[5];

        for(int i = 0; i < producers.length; i++){
            producers[i] = new Producer(iterations / producers.length, null, i);
            consumers[i] = new Consumer(iterations / producers.length, null, i);
        }
        OneToOneQueue<Integer> queue = new OneToOneQueue<Integer>(producers, consumers, 100, false);

        Benchmark benchmark = new Benchmark(producers, consumers, queue, iterations);
        benchmark.runBenchmark();
        System.out.println("Done");
    }
}
