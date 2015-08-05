package edu.multicore.queues.Performance;

import edu.multicore.queues.MultiOneToOne.OneToOneQueue;
import edu.multicore.queues.MultiWorkStealing.WorkStealingQueue;
import edu.multicore.queues.MyQueue;
import edu.multicore.queues.utils.Benchmark;
import edu.multicore.queues.utils.Consumer;
import edu.multicore.queues.utils.Producer;
import edu.multicore.queues.utils.Settings;

import java.io.*;

/**
 * Created by jtharp on 8/4/2015.
 */
public class BenchRunner {
    private static Consumer[] consumers;
    private static Producer[] producers;
    private static MyQueue<Integer> queue;

    private static int iterations = 1000000;

    public static void main(String args[]) throws Exception {

        String multiFilename = "multi.csv";

        Settings.getInstance().setLog(false);
        Benchmark benchmark;

        intFile(multiFilename);

        OneToOneBenchSetup();
        benchmark = new Benchmark(producers, consumers, queue, iterations);
        benchmark.runBenchmark();

        writeResults(benchmark, "OneToOne", multiFilename);

        WorkStealingSetup();
        benchmark = new Benchmark(producers, consumers, queue, iterations);
        benchmark.runBenchmark();

        writeResults(benchmark,"WorkStealing", multiFilename);

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
    }

    private static void writeResults(Benchmark benchmark, String name, String filename) throws IOException {

        PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(filename, true)));
        writer.format("%s,%d,%d,%d\n", name, benchmark.getAverageTime(), benchmark.getAverageFull(), benchmark.getAverageEmpty());
        writer.close();
    }

    private static void OneToOneBenchSetup() throws Exception {
        producers = new Producer[5];
        consumers = new Consumer[5];

        for(int i = 0; i < producers.length; i++){
            producers[i] = new Producer(iterations / producers.length, null, i);
            consumers[i] = new Consumer(iterations / producers.length, null, i);
        }
        queue = new OneToOneQueue<Integer>(producers, consumers, 100);
    }

    private static void WorkStealingSetup() throws  Exception {
        producers = new Producer[5];
        consumers = new Consumer[5];

        for(int i = 0; i < producers.length; i++){
            producers[i] = new Producer(iterations / producers.length, null, i);
            consumers[i] = new Consumer(iterations / producers.length, null, i);
        }
        queue = new WorkStealingQueue<>(producers, consumers, 100);
    }
}
