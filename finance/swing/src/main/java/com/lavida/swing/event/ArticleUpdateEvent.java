package com.lavida.swing.event;

import org.springframework.context.ApplicationEvent;

/**
 * ArticleServiceSwingWrapper
 * Created: 22:09 16.08.13
 *
 * @author Pavel
 */
public class ArticleUpdateEvent extends ApplicationEvent {

    /**
     * Create a new ApplicationEvent.
     *
     * @param source the component that published the event (never {@code null})
     */
    public ArticleUpdateEvent(Object source) {
        super(source);
    }
}
