package edu.multicore.queues.Performance;

import edu.multicore.queues.MultiOneToOne.OneToOneQueue;
import edu.multicore.queues.MultiWorkStealing.WorkStealingQueue;
import edu.multicore.queues.MyQueue;
import edu.multicore.queues.multipleProducerMultipleConsumer.CQueue;
import edu.multicore.queues.singleProducerSingleConsumer.SingleQueue;
import edu.multicore.queues.utils.*;
import hw4.q1.LockFreeQueue;
import hw4.q1.LockQueue;

import java.io.*;

/**
 * Created by jtharp on 8/4/2015.
 */
public class BenchRunner {
    private static Consumer[] consumers;
    private static Producer[] producers;
    private static MyQueue<Integer> queue;

    private static int iterations = 1000000;
    private static RoundRobin rr;

    private static boolean writeToFile = true;

    public static void main(String args[]) throws Exception {

        String filename = "multi.csv";

//        writeToFile = true;
        Settings.getInstance().setLog(false);
        Benchmark benchmark;

        intFile(filename);

        System.out.println("Multi Multi");
        System.out.println("OneToOne");
        OneToOneBenchSetupMulti();
        benchmark = new Benchmark(producers, consumers, queue, iterations);
        benchmark.runBenchmark();

        writeResults(benchmark, "OneToOne", filename);


        System.out.println("Workstealing");
        WorkStealingSetupMulti();
        benchmark = new Benchmark(producers, consumers, queue, iterations);
        benchmark.runBenchmark();

        writeResults(benchmark, "WorkStealing", filename);

        System.out.println("Lockfree");
        LockFree();
        benchmark = new Benchmark(5, 5, iterations, queue);
        benchmark.runBenchmark();

        writeResults(benchmark, "LockFree", filename);

        System.out.println("Lockfree RR");
        LockFreeMultiRR();
        benchmark = new Benchmark(5, 5, iterations, rr);
        benchmark.runBenchmark();

        writeResults(benchmark, "LockFreeRoundRobin", filename);


        System.out.println("Locked Queue");
        LockedQueue();
        benchmark = new Benchmark(5, 5, iterations, queue);
        benchmark.runBenchmark();

        writeResults(benchmark, "LockedQueue", filename);

        System.out.println("LockedWorkStealing");
        LockedWorkStealingQueue();
        benchmark = new Benchmark(5, 5, iterations, queue);
        benchmark.runBenchmark();

        writeResults(benchmark, "WorkStealingLocked", filename);

        System.out.println("Single Queue 1 - 1");
        SingQueue();
        benchmark = new Benchmark(1, 1, iterations, queue);
        benchmark.runBenchmark();

        writeResults(benchmark, "SingleQueue", filename);


/////////////////////////////////////////////////////////////////////////////
        filename = "SingleProducerMulticonsumer.csv";

        intFile(filename);

        System.out.println("Single Producer");

        System.out.println("Workstealing");
        WorkStealingSingleP_MultiC();
        benchmark = new Benchmark(producers, consumers, queue, iterations);
        benchmark.runBenchmark();

        writeResults(benchmark, "WorkStealing", filename);

        System.out.println("Lockfree");
        LockFree();
        benchmark = new Benchmark(1, 5, iterations, queue);
        benchmark.runBenchmark();

        writeResults(benchmark, "LockFree", filename);

        System.out.println("LockfreeRR");
        LockFreeMultiRR();
        benchmark = new Benchmark(1, 5, iterations, rr);
        benchmark.runBenchmark();

        writeResults(benchmark, "LockFreeRoundRobin", filename);

        System.out.println("Locked Queue");
        LockedQueue();
        benchmark = new Benchmark(1, 5, iterations, queue);
        benchmark.runBenchmark();

        writeResults(benchmark, "LockedQueue", filename);

        System.out.println("LockedWorkStealing");
        LockedWorkStealingSingleP_MultiC();
        benchmark = new Benchmark(1, 5, iterations, queue);
        benchmark.runBenchmark();

        writeResults(benchmark, "WorkStealingLocked", filename);
    }

    private static void CQueueInit() {
//        queue = new CQueue();
    }

    private static void LockedQueue() {
        queue = new LockQueue<Integer>();
    }

    private static void intFile(String filename) throws IOException {
        if(!writeToFile)
            return;
        File f = new File(filename);
        if(f.exists() && !f.isDirectory()){
            f.delete();
        }

        printHeader(filename);
    }

    private static void printHeader(String filename) throws IOException {
        PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(filename, true)));
        writer.println("Queue,Time,Full,Empty");
        writer.flush();
    }

    private static void writeResults(Benchmark benchmark, String name, String filename) throws IOException {
        if(!writeToFile)
            return;
        PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(filename, true)));
        writer.format("%s,%d,%d,%d\n", name, benchmark.getAverageTime(), benchmark.getAverageFull(), benchmark.getAverageEmpty());
        writer.close();
    }

    private static void OneToOneBenchSetupMulti() throws Exception {
        producers = new Producer[5];
        consumers = new Consumer[5];

        for(int i = 0; i < producers.length; i++){
            producers[i] = new Producer(iterations / producers.length, null, i);
            consumers[i] = new Consumer(iterations / producers.length, null, i);
        }
        queue = new OneToOneQueue<Integer>(producers, consumers, 100, false);
    }

    private static void WorkStealingSingleP_MultiC() throws Exception {
        producers = new Producer[1];
        consumers = new Consumer[5];

        for(int i = 0; i < producers.length; i++){
            producers[i] = new Producer(iterations / producers.length, null, i);
        }

        for(int i = 0; i < consumers.length; i++){
            consumers[i] = new Consumer(iterations / producers.length, null, i);
        }
        queue = new WorkStealingQueue<>(producers, consumers, 100, QueueEnum.LockFree);
    }

    private static void LockedWorkStealingSingleP_MultiC() throws Exception {
        producers = new Producer[1];
        consumers = new Consumer[5];

        for(int i = 0; i < producers.length; i++){
            producers[i] = new Producer(iterations / producers.length, null, i);
        }

        for(int i = 0; i < consumers.length; i++){
            consumers[i] = new Consumer(iterations / producers.length, null, i);
        }
        queue = new WorkStealingQueue<>(producers, consumers, 100, QueueEnum.Locked);
    }

    private static void WorkStealingSetupMulti() throws  Exception {
        producers = new Producer[5];
        consumers = new Consumer[5];

        for(int i = 0; i < producers.length; i++){
            producers[i] = new Producer(iterations / producers.length, null, i);
            consumers[i] = new Consumer(iterations / producers.length, null, i);
        }
        queue = new WorkStealingQueue<>(producers, consumers, 100, QueueEnum.LockFree);
    }

    private static void LockedWorkStealingQueue() throws Exception {
        producers = new Producer[5];
        consumers = new Consumer[5];

        for(int i = 0; i < producers.length; i++){
            producers[i] = new Producer(iterations / producers.length, null, i);
            consumers[i] = new Consumer(iterations / producers.length, null, i);
        }
        queue = new WorkStealingQueue<>(producers, consumers, 100, QueueEnum.Locked);
    }

    private static void LockFreeMultiRR(){
        queue = new LockFreeQueue(100);
        rr = new RoundRobin(5, queue.getClass());
    }

    private static void LockFree(){
        queue = new LockFreeQueue();
    }

    private static void SingQueue(){
        queue = new SingleQueue<Integer>(100);
    }
}
