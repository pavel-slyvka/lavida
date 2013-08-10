package com.lavida.swing.form;

import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.swing.*;
import java.awt.*;
import java.util.Locale;

/**
 * AbstractForm
 * <p/>
 * Created: 20:25 08.08.13
 *
 * @author Pavel
 */
public abstract class AbstractForm implements MessageSourceAware {
    protected JFrame form;
    protected Container rootContainer;

    @Resource
    protected MessageSource messageSource;

    protected Locale locale = new Locale.Builder().setLanguage("ru").setRegion("RU").setScript("Cyrl").build();

    @PostConstruct
    public void init() {
        form = new JFrame();
        initializeForm();
        rootContainer = form.getContentPane();
        initializeComponents();
    }

    protected void initializeForm() {
        form.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        form.setResizable(false);
//        form.setBounds(200, 200, 400, 200);
//        form.setTitle(LOGIN_FORM_NAME_RU);
    }

    protected abstract void initializeComponents();

    public void showMessage(String titleKey, String messageKey) {
        JOptionPane.showMessageDialog(form,
                messageSource.getMessage(messageKey, null, locale),
                messageSource.getMessage(titleKey, null, locale),
                JOptionPane.DEFAULT_OPTION);
    }

    public void show() {
        form.setVisible(true);
    }

    public void hide() {
        form.dispose();
    }

    public void update() {
        form.repaint();
    }
    @Override
    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }
}
