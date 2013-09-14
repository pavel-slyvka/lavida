package com.lavida.swing.handler;

import com.lavida.service.entity.DiscountCardJdo;
import com.lavida.swing.dialog.AddNewDiscountCardsDialog;
import com.lavida.swing.service.DiscountCardServiceSwingWrapper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created: 17:06 07.09.13
 *
 * @author Ruslan
 */
@Component
public class AddNewDiscountCardsDialogHandler {

    @Resource
    private AddNewDiscountCardsDialog dialog;

    @Resource
    private DiscountCardServiceSwingWrapper discountCardServiceSwingWrapper;


    public void cancelButtonClicked() {
        dialog.getTableModel().setSelectedCard(null);
        dialog.getTableModel().setTableData(new ArrayList<DiscountCardJdo>());
        dialog.getTableModel().fireTableDataChanged();
        dialog.hide();
        dialog.getMainForm().update();
    }

    public void addRowButtonClicked() {
        DiscountCardJdo discountCardJdo = new DiscountCardJdo();
        dialog.getTableModel().getTableData().add(discountCardJdo);
        dialog.getTableModel().fireTableDataChanged();
    }

    public void deleteRowButtonClicked() {
        DiscountCardJdo discountCardJdo = dialog.getTableModel().getSelectedCard();
        dialog.getTableModel().getTableData().remove(discountCardJdo);
        dialog.getTableModel().setSelectedCard(null);
        dialog.getTableModel().fireTableDataChanged();
    }

    public void acceptCardsButtonClicked() {
        List<DiscountCardJdo> discountCardJdoList = dialog.getTableModel().getTableData();
        for (DiscountCardJdo discountCardJdo : discountCardJdoList) {
            discountCardJdo.setRegistrationDate(Calendar.getInstance());
            discountCardJdo.setActivationDate(Calendar.getInstance());
            discountCardServiceSwingWrapper.save(discountCardJdo);
        }
        dialog.getTableModel().getTableData().removeAll(discountCardJdoList);
        dialog.getTableModel().fireTableDataChanged();
        dialog.getCardTableComponent().getCardFiltersComponent().updateAnalyzeComponent();
    }
}
