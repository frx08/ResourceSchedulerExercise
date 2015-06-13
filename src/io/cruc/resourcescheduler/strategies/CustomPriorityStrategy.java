package io.cruc.resourcescheduler.strategies;

import io.cruc.resourcescheduler.MessageImpl;

/**
 * {@inheritDoc}
 * This implementation defines the priority by the group id ascending order.
 */
public class CustomPriorityStrategy implements PriorityStrategy {

    @Override
    public int getPriority(MessageImpl msg) {
        return msg.getGroupId();
    }

}
