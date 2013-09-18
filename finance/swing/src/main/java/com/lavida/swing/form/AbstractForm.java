package com.lavida.swing.form;

import com.lavida.swing.LocaleHolder;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.swing.*;
import java.awt.*;

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

    @Resource
    protected LocaleHolder localeHolder;

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
    }

    protected abstract void initializeComponents();

    public void showWarningMessage(String titleKey, String messageKey) {
        JOptionPane.showMessageDialog(form,
                messageSource.getMessage(messageKey, null, localeHolder.getLocale()),
                messageSource.getMessage(titleKey, null, localeHolder.getLocale()),
                JOptionPane.WARNING_MESSAGE);
    }

    public void showInformationMessage(String titleKey, String messageBody) {
        JOptionPane.showMessageDialog(form, messageBody,
                messageSource.getMessage(titleKey, null, localeHolder.getLocale()),
                JOptionPane.INFORMATION_MESSAGE);

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

    public JFrame getForm() {
        return form;
    }

    @Override
    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    /**
     * Shows a YES_NO confirm dialog.
     * @param titleKey  the title of the dialog from the locale resource bundle
     * @param messageBody the body of the dialog from the locale resource bundle
     * @return
     */
    public int showConfirmDialog(String titleKey, String messageBody) {
        return JOptionPane.showConfirmDialog(form,
                messageSource.getMessage(messageBody, null, localeHolder.getLocale()),
                messageSource.getMessage(titleKey, null, localeHolder.getLocale()),
                JOptionPane.YES_NO_OPTION);
    }

    /**
     *  Shows input dialog.
     * @param titleKey   the title of the messageDialog.
     * @param messageKey the contents of the messageDialog.
     * @param icon       the icon to be displayed.
     * @param selectionValues the array of values to be displayed as a {@link javax.swing.JComboBox}.
     * @param initialSelectionValue the initial selected value in the  {@link javax.swing.JComboBox}.
     * @return  the selected value {@link java.lang.Object}.
     */
    public Object showInputDialog(String titleKey, String messageKey, Icon icon, Object[] selectionValues, Object initialSelectionValue) {
        return JOptionPane.showInputDialog(form,
                messageSource.getMessage(messageKey, null, localeHolder.getLocale()),
                messageSource.getMessage(titleKey, null, localeHolder.getLocale()),
                JOptionPane.QUESTION_MESSAGE, icon, selectionValues, initialSelectionValue);
    }

}
