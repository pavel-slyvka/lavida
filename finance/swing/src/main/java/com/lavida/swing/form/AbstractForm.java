package com.lavida.swing.form;

import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;

import javax.annotation.Resource;
import javax.swing.*;
import java.util.Locale;

/**
 * AbstractForm
 * <p/>
 * Created: 20:25 08.08.13
 *
 * @author Pavel
 */
public class AbstractForm extends JFrame implements MessageSourceAware {

    @Resource
    protected MessageSource messageSource;

    protected Locale locale = new Locale.Builder().setLanguage("ru").setRegion("RU").setScript("Cyrl").build();

    public void showMessage() {

    }

    public void hideForm() {
        dispose();
    }

    @Override
    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }
}
