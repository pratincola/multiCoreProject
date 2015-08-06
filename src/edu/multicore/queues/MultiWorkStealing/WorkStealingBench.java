package edu.multicore.queues.MultiWorkStealing;

import edu.multicore.queues.utils.*;

/**
 * Created by jtharp on 8/3/2015.
 */
public class WorkStealingBench {
    public static void main(String args[]) throws Exception {
        int iterations = 1000000;

        Settings.getInstance().setLog(false);

        Producer[] producers = new Producer[5];
        Consumer[] consumers = new Consumer[5];

        for(int i = 0; i < producers.length; i++){
            producers[i] = new Producer(iterations / producers.length, null, i);
            consumers[i] = new Consumer(iterations / producers.length, null, i);
        }
        WorkStealingQueue<Integer> queue = new WorkStealingQueue<Integer>(producers, consumers, 1000, QueueEnum.LockFree);

        Benchmark benchmark = new Benchmark(producers, consumers, queue, iterations);
        benchmark.runBenchmark();
        System.out.println("Done");
    }
}
