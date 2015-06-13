package io.cruc.resourcescheduler.strategies;

import io.cruc.resourcescheduler.MessageImpl;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * {@inheritDoc}
 * This implementation defines the priority by the order 
 * in which the first message is received from a the group.
 */
public class DefaultPriorityStrategy implements PriorityStrategy {
    private Map<Integer, Integer> groupsPriority = new HashMap<Integer, Integer>();

    @Override
    public int getPriority(MessageImpl msg) {
        Integer groupPriority = groupsPriority.get(msg.getGroupId());
        if (groupPriority == null) {
            if (groupsPriority.size() == 0) {
                groupsPriority.put(msg.getGroupId(), 1);
                return 1;
            } else {
                int maxPriority = Collections.max(groupsPriority.values());
                groupsPriority.put(msg.getGroupId(), maxPriority + 1);
                return maxPriority + 1;
            }
        }
        return groupPriority;
    }

}
