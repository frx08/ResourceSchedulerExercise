package io.cruc.resourcescheduler;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.CountDownLatch;

/**
 * Observer defined for test purposes, waits for all messages to be sent to the gateway, 
 * and then show the delivery order.
 */
public class MessageQueueObserver implements Observer {
    private CountDownLatch latch;
    private List<String> sentOrder = new ArrayList<String>();

    public MessageQueueObserver(int queueLength) {
        latch = new CountDownLatch(queueLength);
        sentOrder = new ArrayList<String>();
    }

    public void waitUntilUpdateIsCalled() throws InterruptedException {
        latch.await();
    }

    @Override
    public void update(Observable o, Object arg) {
        latch.countDown();
        sentOrder.add(((MessageImpl) arg).getContent());
    }

    public List<String> getResult() {
        return sentOrder;
    }

}
