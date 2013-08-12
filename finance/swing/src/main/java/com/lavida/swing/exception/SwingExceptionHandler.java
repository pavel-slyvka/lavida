package com.lavida.swing.exception;

import com.lavida.swing.LocaleHolder;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.swing.*;

/**
 * SwingExceptionHandler
 * <p/>
 * Created: 21:26 11.08.13
 *
 * @author Pavel
 */
@Component
public class SwingExceptionHandler implements Thread.UncaughtExceptionHandler {

    @Resource
    private MessageSource messageSource;

    @Resource
    private LocaleHolder localeHolder;

    @PostConstruct
    public void init() {
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        if (e instanceof LavidaSwingRuntimeException) {
            handleLavidaSwingRuntimeException((LavidaSwingRuntimeException) e);
        } else if (e instanceof RuntimeException) {
            handleRuntimeException((RuntimeException) e);
        } else {
            handleError(e);
        }
    }

    private void handleError(Throwable e) {
        // todo add log message
        e.printStackTrace();
        JOptionPane.showMessageDialog(null,
                messageSource.getMessage("error.unknown.error.message", null, localeHolder.getLocale()),
                messageSource.getMessage("mainForm.exception.message.dialog.title", null, localeHolder.getLocale()),
                JOptionPane.ERROR_MESSAGE);
        System.exit(1);
    }

    private void handleRuntimeException(RuntimeException e) {
        // todo add log message
        e.printStackTrace();
        JOptionPane.showMessageDialog(null,
                messageSource.getMessage("error.unknown.exception.message", null, localeHolder.getLocale()),
                messageSource.getMessage("mainForm.exception.message.dialog.title", null, localeHolder.getLocale()),
                JOptionPane.ERROR_MESSAGE);
    }

    private void handleLavidaSwingRuntimeException(LavidaSwingRuntimeException e) {
        String titleKey;
        String messageKey;
        switch (e.getErrorCode()) {
            case LavidaSwingRuntimeException.GOOGLE_IO_EXCEPTION:
                titleKey = "mainForm.exception.message.dialog.title";
                messageKey = "mainForm.exception.io.load.google.spreadsheet.table.data";
                break;

            case LavidaSwingRuntimeException.GOOGLE_SERVICE_EXCEPTION:
                titleKey = "mainForm.exception.message.dialog.title";
                messageKey = "mainForm.exception.service.load.google.spreadsheet.table.data";
                break;

            default:
                titleKey = "mainForm.exception.message.dialog.title";
                messageKey = "mainForm.exception.service.load.google.spreadsheet.table.data";
                break;
        }
        // todo add log message
        JOptionPane.showMessageDialog(e.getForm().getForm(),
                messageSource.getMessage(messageKey, null, localeHolder.getLocale()),
                messageSource.getMessage(titleKey, null, localeHolder.getLocale()),
                JOptionPane.ERROR_MESSAGE);
    }
}