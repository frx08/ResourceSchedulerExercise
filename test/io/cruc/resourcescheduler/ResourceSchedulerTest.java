package io.cruc.resourcescheduler;

import static org.junit.Assert.assertEquals;
import io.cruc.resourcescheduler.exceptions.CancelledGroupException;
import io.cruc.resourcescheduler.exceptions.TerminatedGroupException;
import io.cruc.resourcescheduler.external.Gateway;
import io.cruc.resourcescheduler.strategies.CustomPriorityStrategy;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class ResourceSchedulerTest {
    Gateway gateway;

    @Before
    public void setup() {
        gateway = new GatewayImpl();
    }

    @Test
    public void testDeliveryOrder() throws InterruptedException, TerminatedGroupException, CancelledGroupException {
        // single resource case as docs
        ResourceScheduler scheduler = new ResourceScheduler(gateway, 1);

        // async response observer
        MessageQueueObserver observer = new MessageQueueObserver(4);
        scheduler.addObserver(observer);

        scheduler.submitMessage(new MessageImpl("1", 2));
        scheduler.submitMessage(new MessageImpl("2", 1));
        scheduler.submitMessage(new MessageImpl("3", 2));
        scheduler.submitMessage(new MessageImpl("4", 3));

        // wait for all tasks
        observer.waitUntilUpdateIsCalled();
        scheduler.shutdown();

        List<String> actual = observer.getResult();
        List<String> expected = Arrays.asList("1", "3", "2", "4");
        assertEquals("gateway sent order", expected, actual);
    }

    @Test(expected = TerminatedGroupException.class)
    public void testTerminatedGroupException() throws TerminatedGroupException, CancelledGroupException, InterruptedException {
        ResourceScheduler scheduler = new ResourceScheduler(gateway, 5);
        scheduler.submitMessage(new MessageImpl("1", 1, true));
        scheduler.submitMessage(new MessageImpl("2", 1));
        scheduler.shutdown();
    }

    @Test(expected = CancelledGroupException.class)
    public void testCancelledGroupException() throws TerminatedGroupException, CancelledGroupException, InterruptedException {
        ResourceScheduler scheduler = new ResourceScheduler(gateway, 5);
        scheduler.cancelGroup(1);
        scheduler.submitMessage(new MessageImpl("1", 1));
        scheduler.shutdown();
    }

    @Test
    public void testCustomPriorityStrategy() throws TerminatedGroupException, CancelledGroupException, InterruptedException {
        CustomPriorityStrategy priorityStrategy = new CustomPriorityStrategy();
        // single resource case with custom strategy
        ResourceScheduler scheduler = new ResourceScheduler(gateway, 1, priorityStrategy);

        // async response observer
        MessageQueueObserver observer = new MessageQueueObserver(3);
        scheduler.addObserver(observer);

        scheduler.submitMessage(new MessageImpl("1", 3));
        scheduler.submitMessage(new MessageImpl("2", 2));
        scheduler.submitMessage(new MessageImpl("3", 1));

        // wait for all tasks
        observer.waitUntilUpdateIsCalled();
        scheduler.shutdown();

        List<String> actual = observer.getResult();
        List<String> expected = Arrays.asList("1", "3", "2");
        assertEquals("gateway sent order", expected, actual);
    }
}
