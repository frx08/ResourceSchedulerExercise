package io.cruc.resourcescheduler.external;

/**
 * 3rd party supplied Gateway interface.
 */
public interface Gateway {
    public void send(Message msg);
}
