package io.cruc.resourcescheduler.strategies;

import io.cruc.resourcescheduler.MessageImpl;

/**
 * Interface that defines the implementation of a priority strategy.
 */
public interface PriorityStrategy {
    public int getPriority(MessageImpl msg);
}
