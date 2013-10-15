package com.lavida.swing.event;

import org.springframework.context.ApplicationEvent;

/**
 * The ConcurrentOperationCompleteEvent
 * <p/>
 * Created: 15.10.13 10:13.
 *
 * @author Ruslan.
 */
public class ConcurrentOperationCompleteEvent extends ApplicationEvent {

    /**
     * Create a new ApplicationEvent.
     *
     * @param source the component that published the event (never {@code null})
     */
    public ConcurrentOperationCompleteEvent(Object source) {
        super(source);
    }
}
