package io.cruc.resourcescheduler.exceptions;

import io.cruc.resourcescheduler.MessageImpl;

public class CancelledGroupException extends Exception {

    /**
     * Exception raised when the scheduler receives a cancellation message for a group.
     */
    private static final long serialVersionUID = 1856248106337067203L;

    public CancelledGroupException(MessageImpl msg) {
        super("Message:" + msg.getContent() + " not sent! Messages from group " + msg.getGroupId() + " has now been cancelled");
    }

}
