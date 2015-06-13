package io.cruc.resourcescheduler;

import static org.junit.Assert.assertEquals;
import io.cruc.resourcescheduler.strategies.CustomPriorityStrategy;
import io.cruc.resourcescheduler.strategies.DefaultPriorityStrategy;

import org.junit.Test;

public class PriorityStrategyTest {
    @Test
    public void testDefaultPriorityStrategy() {
        DefaultPriorityStrategy strategy = new DefaultPriorityStrategy();
        assertEquals("strategy priority", 1, strategy.getPriority(new MessageImpl("1", 2)));
        assertEquals("strategy priority", 2, strategy.getPriority(new MessageImpl("2", 1)));
        assertEquals("strategy priority", 1, strategy.getPriority(new MessageImpl("3", 2)));
        assertEquals("strategy priority", 3, strategy.getPriority(new MessageImpl("4", 3)));
    }

    @Test
    public void testCustomPriorityStrategy() {
        CustomPriorityStrategy strategy = new CustomPriorityStrategy();
        assertEquals("strategy priority", 3, strategy.getPriority(new MessageImpl("1", 3)));
        assertEquals("strategy priority", 2, strategy.getPriority(new MessageImpl("2", 2)));
        assertEquals("strategy priority", 1, strategy.getPriority(new MessageImpl("3", 1)));
    }
}
