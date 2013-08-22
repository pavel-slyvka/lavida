package com.lavida.swing.handler;

import com.lavida.service.entity.ArticleJdo;
import com.lavida.swing.ExchangerHolder;
import com.lavida.swing.LocaleHolder;
import com.lavida.swing.dialog.SellDialog;
import com.lavida.swing.service.ArticleServiceSwingWrapper;
import com.lavida.swing.service.ArticlesTableModel;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.swing.*;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created: 12:04 12.08.13
 * The SellFormHandler is the handler for logic methods for selling goods.
 *
 * @author Ruslan
 */
@Component
public class SellDialogHandler {

    @Resource
    private SellDialog dialog;

    @Resource
    private MessageSource messageSource;

    @Resource
    protected LocaleHolder localeHolder;

    @Resource
    private ArticleServiceSwingWrapper articleServiceSwingWrapper;

    @Resource(name = "notSoldArticleTableModel")
    private ArticlesTableModel tableModel;

    @Resource
    private ExchangerHolder exchangerHolder;

    /**
     * Performs selling operation.
     *
     * @param articleJdo //     * @param articlesTableModel
     */
    public void sellButtonClicked(ArticleJdo articleJdo) {
        articleJdo.setSaleDate(Calendar.getInstance());
        articleJdo.setSold(messageSource.getMessage("sellDialog.button.sell.clicked.sold", null, localeHolder.getLocale()));
        articleJdo.setComment(dialog.getCommentTextArea().getText().trim());
        if (dialog.getOursCheckBox().isSelected()) {
            articleJdo.setPriceUAH(Double.parseDouble(dialog.getPriceField().getText()));
            articleJdo.setOurs(dialog.getOursCheckBox().getActionCommand());
        } else if (dialog.getPresentCheckBox().isSelected()) {
            articleJdo.setPriceUAH(Double.parseDouble(dialog.getPriceField().getText()));
            articleJdo.setOurs(dialog.getPresentCheckBox().getActionCommand());
        }
        StringBuilder tagsBuilder = new StringBuilder();
        for (JCheckBox checkBox : dialog.getTagCheckBoxes()) {
            if (checkBox.isSelected()) {
               tagsBuilder.append(checkBox.getActionCommand() + "; ");
            }
        }
        articleJdo.setFinancialTags(new String(tagsBuilder));
        articleJdo.setShop((dialog.getShopTextField().getText().trim() == null)? null :
                dialog.getShopTextField().getText().trim());

        articleServiceSwingWrapper.update(articleJdo);
        try {
            articleServiceSwingWrapper.updateToSpreadsheet(articleJdo, new Boolean(true));
        } catch (Exception e) {        // todo change to Custom exception
            e.printStackTrace();
            articleJdo.setPostponedOperationDate(new Date());
            articleServiceSwingWrapper.update(articleJdo);
            dialog.getMainForm().getHandler().showPostponedOperationsMessage();
            dialog.showMessage("mainForm.exception.message.dialog.title", "sellDialog.handler.sold.article.not.saved.to.worksheet");
            dialog.hide();
            dialog.getMainForm().update();
            dialog.getMainForm().show();

        }

        dialog.hide();
        dialog.getMainForm().update();
        dialog.getMainForm().show();
    }

//    private double exchangeEurToUah(double priceEur) {
//        return priceEur * exchangerHolder.getSellRateEUR();
//    }

    public void oursCheckBoxSelected() {
        dialog.getPriceField().setText(String.valueOf(0));
        dialog.getDiscountTextField().setText(String.valueOf(0));
    }

    public void checkBoxDeSelected() {
        ArticleJdo articleJdo = tableModel.getSelectedArticle();
        dialog.getPriceField().setText(String.valueOf(articleJdo.getPriceUAH()));
    }

    public void presentCheckBoxSelected() {
        dialog.getPriceField().setText(String.valueOf(0));
        dialog.getDiscountTextField().setText(String.valueOf(0));
    }

    public void discountTextEntered() {
        String inputStr = (dialog.getDiscountTextField().getText().trim().equals(""))? "0.0" : dialog.getDiscountTextField().getText().trim();
        String discountStr = inputStr.replace(",", ".").replaceAll("[^0-9.]", "");
        double discount = Double.parseDouble(discountStr);
        double price = Double.parseDouble(dialog.getPriceField().getText());
        double totalCost = price - discount;
        dialog.getTotalCostTextField().setText(new DecimalFormat("##.##").format(totalCost));
    }
}
