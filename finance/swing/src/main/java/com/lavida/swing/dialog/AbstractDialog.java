package com.lavida.swing.dialog;

import com.lavida.swing.service.UserSettingsService;
import com.lavida.swing.preferences.user.UsersSettingsHolder;
import com.lavida.swing.LocaleHolder;
import com.lavida.swing.form.MainForm;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;

/**
 * Created: 10:58 15.08.13
 *
 * @author Ruslan
 */
public abstract class AbstractDialog implements MessageSourceAware {
    private static final KeyStroke ESCAPE_STROKE =
            KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
    public static final String DISPATCH_WINDOW_CLOSING_ACTION_MAP_KEY =
            "com.lavida.swing.dispatch:WINDOW_CLOSING";

    protected JDialog dialog;

    protected Container rootContainer;

    @Resource
    protected MessageSource messageSource;

    @Resource
    protected LocaleHolder localeHolder;

    @Resource
    protected UsersSettingsHolder usersSettingsHolder;

    @Resource
    protected UserSettingsService userSettingsService;

    @Resource
    protected MainForm mainForm;

    @PostConstruct
    public void init() {
        dialog = new JDialog(mainForm.getForm(),true); // modal true
        initializeForm();
        rootContainer = dialog.getContentPane();
        initializeComponents();
        installEscapeCloseOperation(dialog);
    }

    protected void initializeForm() {
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setResizable(false);
    }

    protected abstract void initializeComponents();

    public void showWarningMessage(String titleKey, String messageKey) {
        JOptionPane.showMessageDialog(dialog,
                messageSource.getMessage(messageKey, null, localeHolder.getLocale()),
                messageSource.getMessage(titleKey, null, localeHolder.getLocale()),
                JOptionPane.WARNING_MESSAGE);
    }

    public void showInformationMessage(String titleKey, String messageBody) {
        JOptionPane.showMessageDialog(dialog, messageBody,
                messageSource.getMessage(titleKey, null, localeHolder.getLocale()),
                JOptionPane.INFORMATION_MESSAGE);

    }

    /**
     * Shows confirm dialog.
     *
     * @param titleKey   the title of the messageDialog.
     * @param messageKey the contents of the messageDialog.
     */
    public int showConfirmDialog(String titleKey, String messageKey) {
        return JOptionPane.showConfirmDialog(dialog,
                messageSource.getMessage(messageKey, null, localeHolder.getLocale()),
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
        return JOptionPane.showInputDialog(dialog,
                messageSource.getMessage(messageKey, null, localeHolder.getLocale()),
                messageSource.getMessage(titleKey, null, localeHolder.getLocale()),
                JOptionPane.QUESTION_MESSAGE, icon, selectionValues, initialSelectionValue);
    }

    public void hide() {
        dialog.dispose();
    }

    public void show() {
        dialog.setVisible(true);
    }

    public void installEscapeCloseOperation(final JDialog dialog) {
        Action dispatchClosing = new AbstractAction() {
            public void actionPerformed(ActionEvent event) {
                dialog.dispatchEvent(new WindowEvent(
                        dialog, WindowEvent.WINDOW_CLOSING
                ));
            }
        };
        JRootPane root = dialog.getRootPane();
        root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                ESCAPE_STROKE, DISPATCH_WINDOW_CLOSING_ACTION_MAP_KEY
        );
        root.getActionMap().put(DISPATCH_WINDOW_CLOSING_ACTION_MAP_KEY, dispatchClosing
        );
    }

    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public void setLocaleHolder(LocaleHolder localeHolder) {
        this.localeHolder = localeHolder;
    }

    public MainForm getMainForm() {
        return mainForm;
    }

    public JDialog getDialog() {
        return dialog;
    }
}
