package com.lavida.swing.handler;

import com.google.gdata.util.ServiceException;
import com.lavida.service.entity.DiscountCardJdo;
import com.lavida.swing.dialog.AllDiscountCardsDialog;
import com.lavida.swing.service.DiscountCardServiceSwingWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.swing.*;
import java.io.IOException;
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
            dialog.showMessage("mainForm.exception.message.dialog.title", "dialog.discounts.allCards.not.chosen");
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
            dialog.showMessage("mainForm.exception.message.dialog.title", "dialog.discounts.allCards.not.chosen");
        }
    }

    public void cancelButtonClicked() {
        dialog.getTableModel().setSelectedCard(null);
        dialog.hide();
    }
}
