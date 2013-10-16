package com.lavida.swing.exception;

import com.lavida.service.remote.google.LavidaGoogleException;
import com.lavida.swing.LocaleHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;

import javax.swing.*;

/**
 * The ExceptionHandler
 * <p/>
 * Created: 08.10.13 15:12.
 *
 * @author Ruslan.
 */
public class ExceptionHandler implements Thread.UncaughtExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(ExceptionHandler.class);

    private MessageSource messageSource;

    private LocaleHolder localeHolder;

    public ExceptionHandler(MessageSource messageSource, LocaleHolder localeHolder) {
        this.messageSource = messageSource;
        this.localeHolder = localeHolder;
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        if (e instanceof LavidaSwingRuntimeException) {
            handleLavidaSwingRuntimeException((LavidaSwingRuntimeException) e);
        } else if (e instanceof NumberFormatException) {
            handleNumberFormatException();
        } else if (e instanceof RuntimeException) {
            logger.error(e.getMessage(), e);
            handleRuntimeException();
        } else {
            logger.error(e.getMessage(), e);
            handleError();
        }
    }

    private void handleLavidaGoogleException(LavidaGoogleException e) {
        String titleKey;
        String messageKey;
        switch (e.getErrorCode()) {

            default:
                titleKey = "mainForm.exception.message.dialog.title";
                messageKey = "mainForm.exception.service.load.google.spreadsheet.table.data";
                break;

        }
        JOptionPane.showMessageDialog(null,
                messageSource.getMessage(messageKey, null, localeHolder.getLocale()),
                messageSource.getMessage(titleKey, null, localeHolder.getLocale()),
                JOptionPane.ERROR_MESSAGE);
    }


    private void handleError() {
        JOptionPane.showMessageDialog(null,
                messageSource.getMessage("error.unknown.error.message", null, localeHolder.getLocale()),
                messageSource.getMessage("mainForm.exception.message.dialog.title", null, localeHolder.getLocale()),
                JOptionPane.ERROR_MESSAGE);
        System.exit(1);
    }

    private void handleRuntimeException() {
        JOptionPane.showMessageDialog(null,
                messageSource.getMessage("error.unknown.exception.message", null, localeHolder.getLocale()),
                messageSource.getMessage("mainForm.exception.message.dialog.title", null, localeHolder.getLocale()),
                JOptionPane.ERROR_MESSAGE);
    }

    private void handleNumberFormatException() {
        JOptionPane.showMessageDialog(null,
                messageSource.getMessage("error.numberFormat.exception.message", null, localeHolder.getLocale()),
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

            case LavidaSwingRuntimeException.JAXB_EXCEPTION:
                titleKey = "mainForm.exception.message.dialog.title";
                messageKey = "mainForm.exception.xml.JAXB.message";
                break;

            case LavidaSwingRuntimeException.IO_EXCEPTION:
                titleKey = "mainForm.exception.message.dialog.title";
                messageKey = "mainForm.exception.io.xml.file";
                break;

            case LavidaSwingRuntimeException.PRINTER_EXCEPTION:
                titleKey = "mainForm.exception.message.dialog.title";
                messageKey = "mainForm.handler.print.exception.message";
                break;

            case LavidaSwingRuntimeException.DATE_FORMAT_EXCEPTION:
                titleKey = "mainForm.exception.message.dialog.title";
                messageKey = "sellDialog.handler.saleDate.not.correct.format";
                break;

            case LavidaSwingRuntimeException.UNAUTHORIZED:
                titleKey = "mainForm.exception.message.dialog.title";
                messageKey = "exception.handler.google.authentication.message";
                break;

            case LavidaSwingRuntimeException.NOT_FOUND_SPREADSHEET_LIST:
                titleKey = "mainForm.exception.message.dialog.title";
                messageKey = "exception.handler.google.spreadsheetList.notFount.message";
                break;

            case LavidaSwingRuntimeException.NOT_FOUND_SPREADSHEET:
                titleKey = "mainForm.exception.message.dialog.title";
                messageKey = "exception.handler.google.spreadsheet.notFount.message";
                break;

            case LavidaSwingRuntimeException.EMPTY_SPREADSHEET_NAME:
                titleKey = "mainForm.exception.message.dialog.title";
                messageKey = "exception.handler.google.spreadsheet.emptyName.message";
                break;

            case LavidaSwingRuntimeException.NOT_FOUND_WORKSHEET:
                titleKey = "mainForm.exception.message.dialog.title";
                messageKey = "exception.handler.google.spreadsheet.emptyName.message";
                break;

            case LavidaSwingRuntimeException.NOT_FOUND_COLUMN_MAPPING:
                titleKey = "mainForm.exception.message.dialog.title";
                messageKey = "exception.handler.google.worksheet.damaged.message";
                break;

            default:
                titleKey = "mainForm.exception.message.dialog.title";
                messageKey = "mainForm.exception.service.load.google.spreadsheet.table.data";
                break;
        }
        JOptionPane.showMessageDialog(null,
                messageSource.getMessage(messageKey, null, localeHolder.getLocale()),
                messageSource.getMessage(titleKey, null, localeHolder.getLocale()),
                JOptionPane.ERROR_MESSAGE);
    }

}
