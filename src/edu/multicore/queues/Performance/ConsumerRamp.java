package edu.multicore.queues.Performance;

import edu.multicore.queues.MultiOneToOne.OneToOneQueue;
import edu.multicore.queues.MultiWorkStealing.WorkStealingQueue;
import edu.multicore.queues.MyQueue;
import edu.multicore.queues.utils.*;
import hw4.q1.LockFreeQueue;

import java.io.*;

/**
 * Created by jtharp on 8/6/2015.
 */
public class ConsumerRamp {

    private static Consumer[] consumers;
    private static Producer[] producers;
    private static MyQueue<Integer> queue;

    private static int iterations = 1000000;
    private static RoundRobin rr;

    private static boolean writeToFile = true;

    public static void main(String args[]) throws Exception {
        int maxConsumers = 10;
        int results[] = new int[maxConsumers];

        String filename = "consumerRamp.csv";

//        writeToFile = true;
        Settings.getInstance().setLog(false);
        Benchmark benchmark;

        intFile(filename, maxConsumers);

        writeQueue("Workstealing", filename);
        for(int i = 1; i <= maxConsumers; i++){
            System.out.println("Workstealing");

            WorkStealingSingleP_MultiC(i);
            benchmark = new Benchmark(producers, consumers, queue, iterations);
            benchmark.runBenchmark();

            writeResults(benchmark, "WorkStealing", filename);
        }
        writeln(filename);



        writeQueue("WorkstealingLocked", filename);
        for(int i = 1; i <= maxConsumers; i++){
            System.out.println("WorkstealingLocked");

            LockedWorkStealingSingleP_MultiC(i);
            benchmark = new Benchmark(producers, consumers, queue, iterations);
            benchmark.runBenchmark();

            writeResults(benchmark, "WorkStealingLocked", filename);
        }
        writeln(filename);

        writeQueue("LockFreeRR", filename);
        for(int i = 1; i <= maxConsumers; i++){
            System.out.println("LockFreeRR");

            LockFreeMultiRR(i);
            benchmark = new Benchmark(3, i, iterations, rr);
            benchmark.runBenchmark();

            writeResults(benchmark, "LockFreeRR", filename);
        }
        writeln(filename);

        writeQueue("LockFree", filename);
        for(int i = 1; i <= maxConsumers; i++){
            System.out.println("LockFreeRR");

            LockFree();
            benchmark = new Benchmark(3, i, iterations, rr);
            benchmark.runBenchmark();

            writeResults(benchmark, "LockFree", filename);
        }
        writeln(filename);






    }

    private static void intFile(String filename, int max) throws IOException {
        if(!writeToFile)
            return;
        File f = new File(filename);
        if(f.exists() && !f.isDirectory()){
            f.delete();
        }

        printHeader(filename, max);
    }

    private static void printHeader(String filename, int max) throws IOException {
        PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(filename, true)));
        writer.print("Queue");

        for(int i = 0; i < max; i++){
            writer.print("," + i);
        }
        writer.println();

        writer.flush();
        writer.close();
    }

    private static void writeResults(Benchmark benchmark, String name, String filename) throws IOException {
        if(!writeToFile)
            return;
        PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(filename, true)));
        writer.format(",%d", benchmark.getAverageTime());
        writer.close();
    }

    private static void writeQueue(String name, String filename) throws IOException {
        if(!writeToFile)
            return;
        PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(filename, true)));
        writer.format("%s", name);
        writer.close();

    }

    private static void writeln(String filename) throws IOException {
        if(!writeToFile)
            return;
        PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(filename, true)));
        writer.println();
        writer.close();
    }

    private static void WorkStealingSingleP_MultiC(int numc) throws Exception {
        producers = new Producer[1];
        consumers = new Consumer[numc];

        for(int i = 0; i < producers.length; i++){
            producers[i] = new Producer(iterations / producers.length, null, i);
        }

        for(int i = 0; i < consumers.length; i++){
            consumers[i] = new Consumer(iterations / producers.length, null, i);
        }
        queue = new WorkStealingQueue<>(producers, consumers, 100, QueueEnum.LockFree);
    }


    private static void LockedWorkStealingSingleP_MultiC(int num) throws Exception {
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

    private static void LockFreeMultiRR(int num){
        queue = new LockFreeQueue(100);
        rr = new RoundRobin(num, queue.getClass());
    }

    private static void LockFree(){
        queue = new LockFreeQueue();
    }

}
