package com.lavida.swing.handler;

import com.lavida.service.entity.ArticleJdo;
import com.lavida.service.entity.DiscountCardJdo;
import com.lavida.swing.LocaleHolder;
import com.lavida.swing.dialog.SellDialog;
import com.lavida.swing.service.ArticleServiceSwingWrapper;
import com.lavida.swing.service.ArticlesTableModel;
import com.lavida.swing.service.DiscountCardServiceSwingWrapper;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.swing.*;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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

    @Resource
    private DiscountCardServiceSwingWrapper discountCardServiceSwingWrapper;

    @Resource(name = "notSoldArticleTableModel")
    private ArticlesTableModel tableModel;

    /**
     * Performs selling operation.
     *
     * @param articleJdo
     */
    public void sellButtonClicked(ArticleJdo articleJdo) {
        if (dialog.getCommentTextField().getText().trim().isEmpty() && (dialog.getOursCheckBox().isSelected() ||
                dialog.getPresentCheckBox().isSelected())) {
            dialog.showMessage("mainForm.exception.message.dialog.title", "sellDialog.handler.ours.comment.not.entered");
            dialog.getCommentTextField().requestFocusInWindow();
            dialog.getMainForm().update();
            return;
        }
        articleJdo.setComment(dialog.getCommentTextField().getText().trim());
        dialog.getCommentTextField().setText("");
        double totalCostUAH = Double.parseDouble(dialog.getTotalCostTextField().getText().replace(",", "."));
        if (!dialog.getDiscountCardNumberTextField().getText().trim().isEmpty()) {
            int discountCardNumber = Integer.parseInt(dialog.getDiscountCardNumberTextField().getText().trim());
            DiscountCardJdo discountCardJdo = discountCardServiceSwingWrapper.getByNumber(discountCardNumber);
            if (discountCardJdo != null) {
                if (discountCardJdo.getActivationDate() != null) {
                    discountCardJdo.setSumTotalUAH(discountCardJdo.getSumTotalUAH() + totalCostUAH);
                    discountCardServiceSwingWrapper.update(discountCardJdo);
                    dialog.getDiscountCardNumberTextField().setText("");
                } else {
                    dialog.showMessage("mainForm.exception.message.dialog.title", "sellDialog.handler.discountCard.isNot.active");
                    dialog.getDiscountCardNumberTextField().setText("");
                    dialog.getDiscountCardNumberTextField().requestFocusInWindow();
                    return;
                }
            } else {
                dialog.showMessage("mainForm.exception.message.dialog.title", "dialog.sell.handler.discount.card.number.not.exists.message");
                dialog.getDiscountCardNumberTextField().setText("");
                dialog.getDiscountCardNumberTextField().requestFocusInWindow();
                return;
            }
        }
        String saleDateStr = dialog.getSaleDateTextField().getText().trim();
        Calendar saleDateCalendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        dateFormat.setLenient(false);
        try {
            Date saleDate = dateFormat.parse(saleDateStr);
            saleDateCalendar.setTime(saleDate);
        } catch (ParseException e) {
            e.printStackTrace();
            dialog.showMessage("mainForm.exception.message.dialog.title", "sellDialog.handler.saleDate.not.correct.format");
            dialog.hide();
            dialog.getMainForm().getTableModel().setSelectedArticle(null);
            dialog.getMainForm().update();
            return;
        }
        articleJdo.setSaleDate(saleDateCalendar);
        if (dialog.getOursCheckBox().isSelected()) {
            articleJdo.setOldSalePrice(articleJdo.getSalePrice());
            articleJdo.setSalePrice(articleJdo.getTotalCostUAH());
            articleJdo.setSellType(dialog.getOursCheckBox().getActionCommand());
        } else if (dialog.getPresentCheckBox().isSelected()) {
            articleJdo.setOldSalePrice(articleJdo.getSalePrice());
            articleJdo.setSalePrice(0.0);
            articleJdo.setSellType(dialog.getPresentCheckBox().getActionCommand());
        } else if (dialog.getClientCheckBox().isSelected()) {
            if (totalCostUAH != articleJdo.getSalePrice()) {
                articleJdo.setOldSalePrice(articleJdo.getSalePrice());
            }
            articleJdo.setSalePrice(totalCostUAH);
        }
        dialog.getClientCheckBox().setSelected(true);

        StringBuilder tagsBuilder = new StringBuilder();
        for (JCheckBox checkBox : dialog.getTagCheckBoxes()) {
            if (checkBox.isSelected()) {
                tagsBuilder.append(checkBox.getActionCommand() + "; ");
                checkBox.setSelected(false);
            }
        }
        articleJdo.setTags(new String(tagsBuilder));
        articleJdo.setShop((dialog.getShopComboBox().getSelectedItem() == null) ? null :
                (String) dialog.getShopComboBox().getSelectedItem());
        dialog.getShopComboBox().setSelectedItem(dialog.getDefaultShop());
        String seller = (dialog.getSellerNames().getSelectedItem() != null) ? (String) dialog.getSellerNames().getSelectedItem() : null;
        tableModel.setSellerName(seller);
        dialog.getSellerNames().setSelectedItem(seller);
        articleJdo.setSeller(seller);
        articleJdo.setSold(messageSource.getMessage("sellDialog.button.sell.clicked.sold", null, localeHolder.getLocale()));

        articleServiceSwingWrapper.update(articleJdo);
        try {
            articleServiceSwingWrapper.updateToSpreadsheet(articleJdo, new Boolean(true));
        } catch (Exception e) {        // todo change to Custom exception
            e.printStackTrace();
            articleJdo.setPostponedOperationDate(new Date());
            articleServiceSwingWrapper.update(articleJdo);
            dialog.hide();
            dialog.showMessage("mainForm.exception.message.dialog.title", "sellDialog.handler.sold.article.not.saved.to.worksheet");
            dialog.getMainForm().getHandler().showPostponedOperationsMessage();
            dialog.getMainForm().getTableModel().setSelectedArticle(null);
            dialog.getMainForm().update();
            return;
        }
        dialog.hide();
        dialog.getMainForm().getTableModel().setSelectedArticle(null);
        dialog.getMainForm().update();
        dialog.getMainForm().show();
    }


    public void oursCheckBoxSelected() {
        dialog.getDiscountCardNumberTextField().setText("");
        dialog.getDiscountCardNumberTextField().setEnabled(false);
        dialog.getPriceField().setText(String.valueOf(0));
        dialog.getDiscountTextField().setText(String.valueOf(0));
        dialog.getDiscountTextField().setEnabled(false);
        dialog.getCommentTextField().setText("");
    }

    public void checkBoxDeSelected() {
        ArticleJdo articleJdo = tableModel.getSelectedArticle();
        dialog.getPriceField().setText(String.valueOf(articleJdo.getSalePrice()));
        dialog.getDiscountTextField().setEnabled(true);
        dialog.getDiscountCardNumberTextField().setEnabled(true);

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
        dialog.getTotalCostTextField().setText(new DecimalFormat("00.00").format(totalCost).replace(",", "."));
    }

    public void discountCardNumberTextEntered() {
        String cardNumberStr = dialog.getDiscountCardNumberTextField().getText().trim();
        if (!cardNumberStr.matches("[0-9]")) {
            dialog.getDiscountCardNumberTextField().setText("");
            dialog.getDiscountCardNumberTextField().requestFocusInWindow();
            dialog.showMessage("mainForm.exception.message.dialog.title", "dialog.sell.handler.discount.card.number.not.correct.message");
            return;
        }
        int cardNumber = Integer.parseInt(cardNumberStr);
        DiscountCardJdo discountCardJdo = discountCardServiceSwingWrapper.getByNumber(cardNumber);
        if (discountCardJdo != null) {
            if (discountCardJdo.getActivationDate() == null) {
                dialog.showMessage("mainForm.exception.message.dialog.title", "sellDialog.handler.discountCard.isNot.active");
                dialog.getDiscountCardNumberTextField().setText("");
                dialog.getDiscountCardNumberTextField().requestFocusInWindow();
                return;
            }
        } else {
            dialog.showMessage("mainForm.exception.message.dialog.title", "dialog.sell.handler.discount.card.number.not.exists.message");
            dialog.getDiscountCardNumberTextField().setText("");
            dialog.getDiscountCardNumberTextField().requestFocusInWindow();
            return;
        }
        double discountRate = discountCardJdo.getDiscountRate();
        double price = Double.parseDouble(dialog.getPriceField().getText());
        double discountVale = price * discountRate / 100;
        dialog.getDiscountTextField().setText(String.valueOf(discountVale));
        discountTextEntered();
        if (discountVale > 0) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(messageSource.getMessage("sellDialog.comment.discountCard.number", null, localeHolder.getLocale()));
            stringBuilder.append(" " + cardNumber + ", ");
            stringBuilder.append(discountVale);
            stringBuilder.append(messageSource.getMessage("sellDialog.comment.discountCard.UAH", null, localeHolder.getLocale()));
            dialog.getCommentTextField().setText(new String(stringBuilder));
        }
    }

    public void clientCheckBoxSelected() {
        if (!dialog.getDiscountCardNumberTextField().getText().trim().isEmpty()) {
            discountCardNumberTextEntered();
        }
    }

    public void cancelButtonClicked() {
        dialog.hide();
        dialog.getDiscountCardNumberTextField().setText("");
        dialog.getMainForm().getTableModel().setSelectedArticle(null);
        dialog.getMainForm().getForm().setVisible(true);
        dialog.getMainForm().update();

    }
}