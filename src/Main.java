import edu.multicore.queues.MyQueue;
import edu.multicore.queues.singleProducerSingleConsumer.SingleQueue;
import edu.multicore.queues.unbounded.UnboundedQueue;
import edu.multicore.queues.utils.Consumer;
import edu.multicore.queues.utils.Message;
import edu.multicore.queues.utils.Producer;
import edu.multicore.queues.utils.Worker;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {

        ArrayList<Thread> tc = new ArrayList<Thread>();
        MyQueue<Message> mq = new SingleQueue<Message>(100);//default algo
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

        for (int i = 0; i < numProducerThread; i++) {
            Producer p = new Producer(mq, NUM_OF_ENQ_DEQUE);
            Thread t = new Thread(p);
            tc.add(t);
        }

        for (int i = 0; i < numConsumerThread; i++) {
            Consumer c = new Consumer(mq, NUM_OF_ENQ_DEQUE);
            Thread t = new Thread(c);
            tc.add(t);
        }

        // Start all the threads.
        for (Thread t : tc){
            t.start();
        }
    }
}
