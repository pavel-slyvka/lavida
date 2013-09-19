package com.lavida.swing.handler;

import com.google.gdata.util.ServiceException;
import com.lavida.service.UserSettingsService;
import com.lavida.service.entity.DiscountCardJdo;
import com.lavida.service.settings.user.UsersSettingsHolder;
import com.lavida.swing.LocaleHolder;
import com.lavida.swing.dialog.AllDiscountCardsDialog;
import com.lavida.swing.service.DiscountCardServiceSwingWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.swing.*;
import java.awt.print.PrinterException;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * The handler for the {@link com.lavida.swing.dialog.AllDiscountCardsDialog}.
 * Created: 11:34 06.09.13
 *
 * @author Ruslan
 */
@Component
public class AllDiscountCardsDialogHandler {
    private static final Logger logger = LoggerFactory.getLogger(AllDiscountCardsDialogHandler.class);

    @Resource
    private AllDiscountCardsDialog dialog;

    @Resource
    private DiscountCardServiceSwingWrapper discountCardServiceSwingWrapper;

    @Resource
    protected MessageSource messageSource;

    @Resource
    protected LocaleHolder localeHolder;

    @Resource
    private UsersSettingsHolder usersSettingsHolder;

    @Resource
    private UserSettingsService userSettingsService;

    public void activateButtonClicked() {
        if (dialog.getTableModel().getSelectedCard() != null) {
            DiscountCardJdo discountCardJdo = dialog.getTableModel().getSelectedCard();
            if (discountCardJdo.getActivationDate() == null) {
                int result = dialog.showConfirmDialog("dialog.discounts.card.all.confirm.activate", "dialog.discounts.card.all.confirm.activate.question");
                switch (result) {
                    case JOptionPane.YES_OPTION:
                        discountCardJdo.setActivationDate(Calendar.getInstance());
                        try {
                            discountCardServiceSwingWrapper.updateToSpreadsheet(discountCardJdo);
                        } catch (IOException | ServiceException e) {
                            logger.warn(e.getMessage(), e);
                            discountCardJdo.setPostponedDate(new Date());
                            dialog.getMainForm().getHandler().showPostponedOperationsMessage();
                        }
                        discountCardServiceSwingWrapper.update(discountCardJdo);
                        dialog.getTableModel().setSelectedCard(null);
                        dialog.getTableModel().fireTableDataChanged();
                        break;
                    case JOptionPane.NO_OPTION:
                        break;
                    case JOptionPane.CLOSED_OPTION:
                        dialog.getTableModel().setSelectedCard(null);
                        dialog.getTableModel().fireTableDataChanged();
                        break;
                    case JOptionPane.CANCEL_OPTION:
                        dialog.getTableModel().setSelectedCard(null);
                        dialog.getTableModel().fireTableDataChanged();
                        break;
                }
            }
        } else {
            dialog.showWarningMessage("mainForm.exception.message.dialog.title", "dialog.discounts.allCards.not.chosen");
        }
    }

    public void disableButtonClicked() {
        if (dialog.getTableModel().getSelectedCard() != null) {
            DiscountCardJdo discountCardJdo = dialog.getTableModel().getSelectedCard();
            if (discountCardJdo.getActivationDate() != null) {
                int result = dialog.showConfirmDialog("dialog.discounts.card.all.confirm.activate", "dialog.discounts.card.all.confirm.activate.question");
                switch (result) {
                    case JOptionPane.YES_OPTION:
                        discountCardJdo.setActivationDate(null);
                        try {
                            discountCardServiceSwingWrapper.updateToSpreadsheet(discountCardJdo);
                        } catch (IOException | ServiceException e) {
                            logger.warn(e.getMessage(), e);
                            discountCardJdo.setPostponedDate(new Date());
                            dialog.getMainForm().getHandler().showPostponedOperationsMessage();
                        }
                        discountCardServiceSwingWrapper.update(discountCardJdo);
                        dialog.getTableModel().setSelectedCard(null);
                        dialog.getTableModel().fireTableDataChanged();
                        break;
                    case JOptionPane.NO_OPTION:
                        break;
                    case JOptionPane.CLOSED_OPTION:
                        dialog.getTableModel().setSelectedCard(null);
                        dialog.getTableModel().fireTableDataChanged();
                        break;
                }
            }
        } else {
            dialog.showWarningMessage("mainForm.exception.message.dialog.title", "dialog.discounts.allCards.not.chosen");
        }
    }

    public void cancelButtonClicked() {
        dialog.getTableModel().setSelectedCard(null);
        dialog.hide();
    }

    public void printItemClicked() {
        MessageFormat header = new MessageFormat(messageSource.getMessage("dialog.discounts.card.all.menu.file.print.header", null, localeHolder.getLocale()));
        MessageFormat footer = new MessageFormat(messageSource.getMessage("mainForm.menu.table.print.footer", null, localeHolder.getLocale()));
        boolean fitPageWidth = false;
        boolean showPrintDialog = true;
        boolean interactive = true;
        JTable.PrintMode printMode = fitPageWidth ? JTable.PrintMode.FIT_WIDTH : JTable.PrintMode.NORMAL;
        try {
            boolean complete = dialog.getCardTableComponent().getDiscountCardsTable().print(printMode, header, footer,
                    showPrintDialog, null, interactive, null);
            if (complete) {
                dialog.showInformationMessage("mainForm.menu.table.print.message.title",
                        messageSource.getMessage("mainForm.menu.table.print.finished.message.body", null, localeHolder.getLocale()));
            } else {
                dialog.showInformationMessage("mainForm.menu.table.print.message.title",
                        messageSource.getMessage("mainForm.menu.table.print.cancel.message.body", null, localeHolder.getLocale()));
            }
        } catch (PrinterException e) {
            logger.warn(e.getMessage(), e);
            dialog.showWarningMessage("mainForm.exception.message.dialog.title", "mainForm.handler.print.exception.message");
        }


    }
}
