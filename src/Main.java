import edu.multicore.queues.MyQueue;
import edu.multicore.queues.jqueues.DualSynchQueue;
import edu.multicore.queues.jqueues.SimpleLinear;
import edu.multicore.queues.jqueues.SimpleTree;
import edu.multicore.queues.jqueues.WrapperSimpleTree;
import edu.multicore.queues.singleProducerSingleConsumer.SingleQueue;
import edu.multicore.queues.unbounded.UnboundedQueue;
import edu.multicore.queues.utils.*;
import hw4.q1.LockFreeQueue;
import hw4.q1.LockQueue;
import hw4.q2.WrappedCoarseGrainedListSet;

public class Main {

    public static void main(String[] args) {
        Settings s = Settings.getInstance();
        s.setLog(true);
//        runWrappedCoarseGrainedListSet();
//        runLockedFreeRR();
//        runSimpleTree();
//        runLockedFree();
          runSimpleLinear();
    }

    public static void runSimpleLinear(){
        SimpleLinear s = new SimpleLinear(10);
        WrapperSimpleTree w = new WrapperSimpleTree(s, 10, true);
        Benchmark b = new Benchmark(5, 1, 10000, w);
        b.runBenchmark();
    }


    public static void runSimpleTree(){
        SimpleTree t = new SimpleTree(10);
        WrapperSimpleTree mq = new WrapperSimpleTree(t, 6, true);

        Benchmark b = new Benchmark(5, 1, 1000, mq);
        b.runBenchmark();
    }

    public static void runLockedFree(){
        LockFreeQueue mq = new LockFreeQueue();
        Benchmark b = new Benchmark(5, 1, 1000, mq);
        b.runBenchmark();
    }

    public static void runLockedFreeRR(){
        LockFreeQueue mq = new LockFreeQueue();
        RoundRobin r = new RoundRobin(5, mq.getClass(), true, false);
        Benchmark b = new Benchmark(5, 5, 100, r);
        b.runBenchmark();
    }

    public static void runWrappedCoarseGrainedListSet(){
        WrappedCoarseGrainedListSet mq = new WrappedCoarseGrainedListSet();
        RoundRobin r = new RoundRobin(10, mq.getClass(), true, false );
//        Benchmark b = new Benchmark(10,5, 100000, r);
        Benchmark b = new Benchmark(10,5, 100000, mq);
        b.runBenchmark();
        try {
            System.out.println(mq.isEmpty());
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

    }



    public static void runDualSynchQueue(){
        DualSynchQueue mq = new DualSynchQueue();
        RoundRobin r = new RoundRobin(10, mq.getClass());
        Benchmark b = new Benchmark(10,5, 100000, r);
        b.runBenchmark();

    }



    public static void runLockFreeQueue_RRmode(){
        LockFreeQueue mq = new LockFreeQueue();
        RoundRobin r = new RoundRobin(10, mq.getClass());
        Benchmark b = new Benchmark(10, 1, 1000000, r);
        b.runBenchmark();
    }

    public static void runLockFreeQueue(){
        LockFreeQueue mq = new LockFreeQueue();
        Benchmark b = new Benchmark(2, 2, 1000000, mq);
        b.runBenchmark();
//        System.out.println("Empty count: " + mq.getEmptyCounter());
        System.out.println("done");
    }

    public static void runUnboundedQueue() {
        LockQueue mq = new LockQueue();
        Benchmark b = new Benchmark(10, 10, 1000000, mq);
        b.runBenchmark();
        System.out.println("Empty count: " + mq.getEmptyCounter());
        System.out.println("done");
    }

    public static void runSingleQueue() {
        SingleQueue<Message> mq = new SingleQueue<Message>(100);//default algo
        Benchmark benchmark = new Benchmark(1, 1, 100000, mq);
        benchmark.runBenchmark();
        mq.getEmptyCounter();
        System.out.println(mq.getEmptyCounter());
        System.out.println(mq.getFullCounter());
        System.out.println("Done");
    }


    public static void runLogic(String[] args) {
        MyQueue mq = new SingleQueue<Message>(100);//default algo
        final int NUM_OF_ENQ_DEQUE = 1000;
        int numProducerThread = 1;
        int numConsumerThread = 1;
        if (args.length < 3) {
            System.err.println("Provide 3 arguments");
            System.err.println("\t(1) <algorithm>: ");
            System.err.println("\t(2) <numProducerThread>: the number of producer thread");
            System.err.println("\t(3) <numConsumerThread>: the number of consumer thread");
            System.err.println("\t(*) Running with default values");

        } else {
            if (args[0].equals("singleQueue")) {
                mq = new SingleQueue<Message>(2);
            } else if (args[0].equals("unboundedQueue")) {
                mq = new UnboundedQueue<Message>();
            } else {
                System.err.println("ERROR: no such algorithm implemented");
                System.exit(-1);
            }
            numProducerThread = Integer.parseInt(args[1]);
            numConsumerThread = Integer.parseInt(args[2]);
        }
    }

}
