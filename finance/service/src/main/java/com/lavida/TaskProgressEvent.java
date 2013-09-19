package com.lavida;

import org.springframework.context.ApplicationEvent;

/**
 * TaskProgressEvent
 * Created: 14:50 19.09.13
 *
 * @author Pavel
 */
public class TaskProgressEvent extends ApplicationEvent {
    private static int eventHashCounter = 0;

    private TaskProgressType type;
    private int eventHash = ++eventHashCounter;

    public enum TaskProgressType {
        COMPLETE
    }

    /**
     * Create a new ApplicationEvent.
     *
     * @param source the component that published the event (never {@code null})
     */
    public TaskProgressEvent(Object source, TaskProgressType type) {
        super(source);
        this.type = type;
    }

    public TaskProgressType getType() {
        return type;
    }

    public int getEventHash() {
        return eventHash;
    }
}
