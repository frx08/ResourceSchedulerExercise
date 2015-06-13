package io.cruc.resourcescheduler;

import io.cruc.resourcescheduler.external.Gateway;
import io.cruc.resourcescheduler.external.Message;

import java.util.Random;

import org.apache.log4j.Logger;

/**
 * Custom implementation of the gateway interface
 */
public class GatewayImpl implements Gateway {
    private final static Logger log = Logger.getLogger(GatewayImpl.class);

    @Override
    public void send(Message msg) {
        try {
            Thread.sleep(new Random().nextInt(5000));
            msg.completed();
        } catch (InterruptedException e) {
            log.error(e.getMessage());
        }
    }

}
