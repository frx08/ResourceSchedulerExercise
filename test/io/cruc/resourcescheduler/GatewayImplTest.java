package io.cruc.resourcescheduler;

import static org.junit.Assert.assertTrue;
import io.cruc.resourcescheduler.external.Gateway;

import org.junit.Before;
import org.junit.Test;

public class GatewayImplTest {
    Gateway gateway;

    @Before
    public void setup() {
        gateway = new GatewayImpl();
    }

    @Test
    public void testGatewaySend() {
        MessageImpl msg = new MessageImpl("1", 1);
        gateway.send(msg);
        assertTrue(msg.isCompleted());
    }
}
