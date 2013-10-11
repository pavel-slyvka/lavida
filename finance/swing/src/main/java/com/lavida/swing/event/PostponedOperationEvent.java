package com.lavida.swing.event;

import org.springframework.context.ApplicationEvent;

/**
 * The PostponedOperationEvent
 * <p/>
 * Created: 10.10.13 21:26.
 *
 * @author Ruslan.
 */
public class PostponedOperationEvent extends ApplicationEvent {
    /**
     * Create a new ApplicationEvent.
     *
     * @param source the component that published the event (never {@code null})
     */
    public PostponedOperationEvent(Object source) {
        super(source);
    }
}
