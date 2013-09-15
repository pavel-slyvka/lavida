package com.lavida.swing.handler;

import com.lavida.service.entity.DiscountCardJdo;
import com.lavida.swing.dialog.AllDiscountCardsDialog;
import com.lavida.swing.service.DiscountCardServiceSwingWrapper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.swing.*;
import java.util.Calendar;

/**
 * The handler for the {@link com.lavida.swing.dialog.AllDiscountCardsDialog}.
 * Created: 11:34 06.09.13
 *
 * @author Ruslan
 */
@Component
public class AllDiscountCardsDialogHandler {

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
