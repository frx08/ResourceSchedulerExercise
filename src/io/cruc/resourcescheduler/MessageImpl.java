package io.cruc.resourcescheduler;

import io.cruc.resourcescheduler.external.Message;

import org.apache.log4j.Logger;

/**
 * Custom implementation of the message interface
 */
public class MessageImpl implements Message {
    private final static Logger log = Logger.getLogger(MessageImpl.class);

    private String content;
    private int groupId;
    private boolean isLast;
    private boolean completed;

    /**
     * @param msg
     * 
     * @param groudId
     */
    public MessageImpl(String content, int groupId) {
        this.content = content;
        this.groupId = groupId;
        isLast = false;
        completed = false;
    }

    /**
     * @param msg
     * 
     * @param groudId
     * 
     * @param isLast
     */
    public MessageImpl(String content, int groupId, boolean isLast) {
        this.content = content;
        this.groupId = groupId;
        this.isLast = isLast;
        completed = false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void completed() {
        completed = true;
        log.debug("message:" + content + ", group:" + groupId + " completed");
    }

    /**
     * @return message content
     */
    public String getContent() {
        return content;
    }

    /**
     * @param msg
     */
    public void setContent(String msg) {
        this.content = msg;
    }

    /**
     * @return group id
     */
    public int getGroupId() {
        return groupId;
    }

    /**
     * @param groupId
     */
    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    /**
     * @return is the last in the group
     */
    public boolean isLast() {
        return isLast;
    }

    /**
     * @return completed status
     */
    public boolean isCompleted() {
        return completed;
    }

}
