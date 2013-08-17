package com.lavida.swing.dialog;

import com.lavida.swing.LocaleHolder;
import com.lavida.swing.form.MainForm;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.swing.*;
import java.awt.*;

/**
 * Created: 10:58 15.08.13
 *
 * @author Ruslan
 */
public abstract class AbstractDialog implements MessageSourceAware {
    protected JDialog dialog;
    protected Container rootContainer;

    @Resource
    protected MessageSource messageSource;

    @Resource
    protected LocaleHolder localeHolder;

    @Resource
    protected MainForm mainForm;

    @PostConstruct
    public void init() {
        dialog = new JDialog(mainForm.getForm(),true); // modal true
        initializeForm();
        rootContainer = dialog.getContentPane();
        initializeComponents();
    }

    protected void initializeForm() {
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setResizable(false);
    }

    protected abstract void initializeComponents();

    public void showMessage(String titleKey, String messageKey) {
        JOptionPane.showMessageDialog(dialog,
                messageSource.getMessage(messageKey, null, localeHolder.getLocale()),
                messageSource.getMessage(titleKey, null, localeHolder.getLocale()),
                JOptionPane.WARNING_MESSAGE);
    }

    public void hide() {
        dialog.dispose();
    }

    public void show() {
        dialog.setVisible(true);
    }



    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public void setLocaleHolder(LocaleHolder localeHolder) {
        this.localeHolder = localeHolder;
    }

    public LocaleHolder getLocaleHolder() {
        return localeHolder;
    }

    public MainForm getMainForm() {
        return mainForm;
    }
}
