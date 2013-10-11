package com.lavida.swing.event;

import org.springframework.context.ApplicationEvent;

/**
 * The ChangedFieldEvent
 * <p/>
 * Created: 01.10.13 21:30.
 *
 * @author Ruslan.
 */
public class ChangedFieldEvent extends ApplicationEvent {
    /**
     * Create a new ApplicationEvent.
     *
     * @param source the component that published the event (never {@code null})
     */
    public ChangedFieldEvent(Object source) {
        super(source);
    }
}
