package io.cruc.resourcescheduler;

import io.cruc.resourcescheduler.exceptions.CancelledGroupException;
import io.cruc.resourcescheduler.exceptions.TerminatedGroupException;
import io.cruc.resourcescheduler.external.Gateway;
import io.cruc.resourcescheduler.external.Message;
import io.cruc.resourcescheduler.strategies.DefaultPriorityStrategy;
import io.cruc.resourcescheduler.strategies.PriorityStrategy;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Observable;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author f.cruciani
 * This class implements the resource scheduling logic defined in the Resource Scheduler Exercise document.
 */
public class ResourceScheduler extends Observable {
    private ThreadPoolExecutor executor;
    private Gateway gateway;
    private PriorityStrategy priorityStrategy;

    // stores the cancelled groups
    private Set<Integer> cancellatedGroups = new HashSet<Integer>();
    // stores the terminated groups
    private Set<Integer> terminatedGroups = new HashSet<Integer>();

    /**
     * Works just like {@link #ResourceScheduler(Gateway, int, PriorityStrategy)}
     * except the {@link PriorityStrategy} is presumed to be {@link DefaultPriorityStrategy}. 
     * @see #ResourceScheduler(Gateway, int, PriorityStrategy)
     */
    public ResourceScheduler(Gateway gateway, int resources) {
        this(gateway, resources, new DefaultPriorityStrategy());
    }

    /**
     * Receives {@link Message} and queues them up if they cannot be processed directly.
     * As a resource become available, sends the {@link Message} to the {@link Gateway} according to a {@link PriorityStrategy}
     * @param gateway
     * @param resources
     * @param priorityStrategy
     */
    public ResourceScheduler(Gateway gateway, int resources, PriorityStrategy priorityStrategy) {
        this.gateway = gateway;
        this.priorityStrategy = priorityStrategy;
        // priority queue of runnable tasks to implement the multi-resource scheduling priority logic
        BlockingQueue<Runnable> queue = new PriorityBlockingQueue<Runnable>(resources, new PriorityComparator());
        executor = new ThreadPoolExecutor(resources, resources, 60, TimeUnit.SECONDS, queue);
    }

    /**
     * Add message to the delivery queue.
     * @param msg
     * @throws TerminatedGroupException
     *          If further Messages belonging to a terminated group are received.
     * @throws CancelledGroupException
     *          If further Messages belonging to a cancelled group are received.
     */
    public void submitMessage(final MessageImpl msg) throws TerminatedGroupException, CancelledGroupException {
        // check if own group has been cancelled
        if (cancellatedGroups.contains(msg.getGroupId())) {
            throw new CancelledGroupException(msg);
        }
        // check if own group has been terminated
        if (terminatedGroups.contains(msg.getGroupId())) {
            throw new TerminatedGroupException(msg);
        }
        // termination message received
        if (msg.isLast()) {
            terminatedGroups.add(msg.getGroupId());
        }
        // add message to the priority queue according to the priority strategy defined
        executor.execute(new PriorityRunnable(priorityStrategy.getPriority(msg)) {
            @Override
            public void run() {
                // notify observer when message is sent to gateway. testing purpose.
                setChanged();
                notifyObservers(msg);
                gateway.send(msg);
            }
        });
    }

    /**
     * Tells the scheduler that a group of messages has now been cancelled.
     * @param groupId
     */
    public void cancelGroup(int groupId) {
        cancellatedGroups.add(groupId);
    }

    /**
     * Shuts down the thread executor.
     * @throws InterruptedException
     */
    public void shutdown() throws InterruptedException {
        executor.shutdown();
        executor.awaitTermination(60, TimeUnit.SECONDS);
    }

    /**
     * Comparator used to order {@link PriorityRunnable} inside the thread pool's priority queue.
     */
    private class PriorityComparator implements Comparator<Runnable> {
        @Override
        public int compare(Runnable o1, Runnable o2) {
            return ((PriorityRunnable) o1).getPriority().compareTo(((PriorityRunnable) o2).getPriority());
        }
    }

    /**
     * Prioritized implementation of {@link Runnable}.
     */
    private abstract class PriorityRunnable implements Runnable {
        private int priority;

        public PriorityRunnable(int priority) {
            this.priority = priority;
        }

        public Integer getPriority() {
            return priority;
        }
    }

}
