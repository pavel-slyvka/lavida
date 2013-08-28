package com.lavida.swing.handler;

import com.lavida.service.entity.ArticleJdo;
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

    /**
     * Performs selling operation.
     *
     * @param articleJdo //     * @param articlesTableModel
     */
    public void sellButtonClicked(ArticleJdo articleJdo) {
        articleJdo.setSaleDate(Calendar.getInstance());
        articleJdo.setSold(messageSource.getMessage("sellDialog.button.sell.clicked.sold", null, localeHolder.getLocale()));
        articleJdo.setComment(dialog.getCommentTextField().getText().trim());
        dialog.getCommentTextField().setText("");
        if (dialog.getOursCheckBox().isSelected()) {
            articleJdo.setSalePrice(articleJdo.getCalculatedSalePrice());
            articleJdo.setSellType(dialog.getOursCheckBox().getActionCommand());
            dialog.getOursCheckBox().setSelected(false);
        } else if (dialog.getPresentCheckBox().isSelected()) {
            articleJdo.setSalePrice(Double.parseDouble(dialog.getPriceField().getText().trim()));
            articleJdo.setSellType(dialog.getPresentCheckBox().getActionCommand());
            dialog.getPresentCheckBox().setSelected(false);
        }
        StringBuilder tagsBuilder = new StringBuilder();
        for (JCheckBox checkBox : dialog.getTagCheckBoxes()) {
            if (checkBox.isSelected()) {
                tagsBuilder.append(checkBox.getActionCommand() + "; ");
                checkBox.setSelected(false);
            }
        }
        articleJdo.setTags(new String(tagsBuilder));
        articleJdo.setShop((dialog.getShopTextField().getText().trim() == null) ? null :
                dialog.getShopTextField().getText().trim());
        dialog.getShopTextField().setText(messageSource.getMessage("sellDialog.text.field.shop.text", null, localeHolder.getLocale()));
        articleServiceSwingWrapper.update(articleJdo);
        try {
            articleServiceSwingWrapper.updateToSpreadsheet(articleJdo, new Boolean(true));
        } catch (Exception e) {        // todo change to Custom exception
            e.printStackTrace();
            articleJdo.setPostponedOperationDate(new Date());
            articleServiceSwingWrapper.update(articleJdo);
            dialog.hide();
            dialog.getMainForm().getTableModel().setSelectedArticle(null);
            dialog.getMainForm().getArticleTableComponent().getArticleFiltersComponent().getClearSearchButton().doClick();
            dialog.getMainForm().getHandler().showPostponedOperationsMessage();
            dialog.showMessage("mainForm.exception.message.dialog.title", "sellDialog.handler.sold.article.not.saved.to.worksheet");
            dialog.getMainForm().getArticleTableComponent().getArticleFiltersComponent().updateAnalyzeComponent();
            dialog.getMainForm().getSoldProductsDialog().getArticleTableComponent().getArticleFiltersComponent().updateAnalyzeComponent();
            dialog.getMainForm().update();
            dialog.getMainForm().show();
        }
        dialog.hide();
        dialog.getMainForm().getTableModel().setSelectedArticle(null);
        dialog.getMainForm().getArticleTableComponent().getArticleFiltersComponent().updateAnalyzeComponent();
        dialog.getMainForm().getSoldProductsDialog().getArticleTableComponent().getArticleFiltersComponent().updateAnalyzeComponent();
        dialog.getMainForm().update();
        dialog.getMainForm().show();
    }

    public void oursCheckBoxSelected() {
        dialog.getPriceField().setText(String.valueOf(0));
        dialog.getDiscountTextField().setText(String.valueOf(0));
    }

    public void checkBoxDeSelected() {
        ArticleJdo articleJdo = tableModel.getSelectedArticle();
        dialog.getPriceField().setText(String.valueOf(articleJdo.getSalePrice()));
    }

    public void presentCheckBoxSelected() {
        dialog.getPriceField().setText(String.valueOf(0));
        dialog.getDiscountTextField().setText(String.valueOf(0));
    }

    public void discountTextEntered() {
        String inputStr = (dialog.getDiscountTextField().getText().trim().equals("")) ? "0.0" : dialog.getDiscountTextField().getText().trim();
        String discountStr = inputStr.replace(",", ".").replaceAll("[^0-9.]", "");
        double discount = Double.parseDouble(discountStr);
        double price = Double.parseDouble(dialog.getPriceField().getText());
        double totalCost = price - discount;
        dialog.getTotalCostTextField().setText(new DecimalFormat("##.##").format(totalCost));
    }
}
