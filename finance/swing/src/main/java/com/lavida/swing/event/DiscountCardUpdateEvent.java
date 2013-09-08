package com.lavida.swing.event;

import org.springframework.context.ApplicationEvent;

/**
 * The event for DiscountCardServiceSwingWrapper.
 * Created: 12:07 08.09.13
 *
 * @author Ruslan
 */
public class DiscountCardUpdateEvent extends ApplicationEvent {

    /**
     * Create a new ApplicationEvent.
     *
     * @param source the component that published the event (never {@code null})
     */
    public DiscountCardUpdateEvent(Object source) {
        super(source);
    }
}
