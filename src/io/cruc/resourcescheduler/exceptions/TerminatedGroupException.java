package io.cruc.resourcescheduler.exceptions;

import io.cruc.resourcescheduler.MessageImpl;

public class TerminatedGroupException extends Exception {

    /**
     * Exception raised when the scheduler receives a termination message for an already terminated group.
     */
    private static final long serialVersionUID = 1856248106337067203L;

    public TerminatedGroupException(MessageImpl msg) {
        super("Message:" + msg.getContent() + " not sent! Termination Message already received for group " + msg.getGroupId());
    }

}
