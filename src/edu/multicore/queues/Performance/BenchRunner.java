package edu.multicore.queues.Performance;

import edu.multicore.queues.MultiOneToOne.OneToOneQueue;
import edu.multicore.queues.MultiWorkStealing.WorkStealingQueue;
import edu.multicore.queues.MyQueue;
import edu.multicore.queues.singleProducerSingleConsumer.SingleQueue;
import edu.multicore.queues.utils.*;
import hw4.q1.LockFreeQueue;

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

    public static void main(String args[]) throws Exception {

        String filename = "multi.csv";

        Settings.getInstance().setLog(false);
        Benchmark benchmark;

        intFile(filename);

        OneToOneBenchSetupMulti();
        benchmark = new Benchmark(producers, consumers, queue, iterations);
        benchmark.runBenchmark();

        writeResults(benchmark, "OneToOne", filename);

        WorkStealingSetupMulti();
        benchmark = new Benchmark(producers, consumers, queue, iterations);
        benchmark.runBenchmark();

        writeResults(benchmark, "WorkStealing", filename);


        LockFree();
        benchmark = new Benchmark(2, 2, iterations, queue);
        benchmark.runBenchmark();

        writeResults(benchmark, "LockFree", filename);

        LockFreeMultiRR();
        benchmark = new Benchmark(5, 5, iterations, rr);
        benchmark.runBenchmark();

        writeResults(benchmark, "LockFreeRoundRobin", filename);

        SingQueue();
        benchmark = new Benchmark(1, 1, iterations, queue);
        benchmark.runBenchmark();

        writeResults(benchmark, "SingleQueue", filename);

/////////////////////////////////////////////////////////////////////////////
        filename = "SingleProducerMulticonsumer.csv";

        intFile(filename);

        WorkStealingSingleP_MultiC();
        benchmark = new Benchmark(producers, consumers, queue, iterations);
        benchmark.runBenchmark();

        writeResults(benchmark, "WorkStealing", filename);

        LockFree();
        benchmark = new Benchmark(1, 5, iterations, queue);
        benchmark.runBenchmark();

        writeResults(benchmark,"LockFree", filename);

        LockFreeMultiRR();
        benchmark = new Benchmark(1, 5, iterations, rr);
        benchmark.runBenchmark();

        writeResults(benchmark, "LockFreeRoundRobin", filename);
    }

    private static void intFile(String filename) throws IOException {
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
        queue = new WorkStealingQueue<>(producers, consumers, 100, false);
    }

    private static void WorkStealingSetupMulti() throws  Exception {
        producers = new Producer[5];
        consumers = new Consumer[5];

        for(int i = 0; i < producers.length; i++){
            producers[i] = new Producer(iterations / producers.length, null, i);
            consumers[i] = new Consumer(iterations / producers.length, null, i);
        }
        queue = new WorkStealingQueue<>(producers, consumers, 100, false);
    }

    private static void LockFreeMultiRR(){
        queue = new LockFreeQueue();
        rr = new RoundRobin(5, queue.getClass());
    }

    private static void LockFree(){
        queue = new LockFreeQueue();
    }

    private static void SingQueue(){
        queue = new SingleQueue<Integer>(100);
    }
}
