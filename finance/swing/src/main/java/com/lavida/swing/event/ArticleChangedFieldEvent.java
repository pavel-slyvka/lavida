package com.lavida.swing.event;

import org.springframework.context.ApplicationEvent;

/**
 * The ArticleChangedFieldEvent
 * <p/>
 * Created: 01.10.13 21:30.
 *
 * @author Ruslan.
 */
public class ArticleChangedFieldEvent extends ApplicationEvent {
    /**
     * Create a new ApplicationEvent.
     *
     * @param source the component that published the event (never {@code null})
     */
    public ArticleChangedFieldEvent(Object source) {
        super(source);
    }
}
