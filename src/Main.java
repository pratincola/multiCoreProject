import edu.multicore.queues.MyQueue;
import edu.multicore.queues.singleProducerSingleConsumer.SingleQueue;
import edu.multicore.queues.unbounded.UnboundedQueue;
import edu.multicore.queues.utils.*;
import hw4.LockQueue;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {

//        runLogic(args);
/*      //Single lock
        runSingleQueue();
*/
        LockQueue mq = new LockQueue();
        Benchmark b = new Benchmark(1,1, 1000000, mq);
        b.runBenchmark();
        System.out.println(mq.getEmptyCounter());
        System.out.println("done");

    }

        public static void runSingleQueue(){
                SingleQueue<Message> mq = new SingleQueue<Message>(100);//default algo
                Benchmark benchmark = new Benchmark(1, 1, 1000, mq);
                benchmark.runBenchmark();
                mq.getEmptyCounter();
                System.out.println(mq.getEmptyCounter());
                System.out.println(mq.getFullCounter());
                System.out.println("Done");
        }


        public static void runLogic(String [] args){
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

                }
                else{
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
