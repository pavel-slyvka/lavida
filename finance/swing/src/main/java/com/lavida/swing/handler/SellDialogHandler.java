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
    public void sell(ArticleJdo articleJdo) {
        articleJdo.setSaleDate(Calendar.getInstance());
        articleJdo.setSold(messageSource.getMessage("sellDialog.button.sell.clicked.sold", null, localeHolder.getLocale()));
        articleJdo.setComment(articleJdo.getComment() + "; " + dialog.getCommentTextArea().getText());
        if (dialog.getOursCheckBox().isSelected()) {
            articleJdo.setPriceUAH(exchangeEurToUah(articleJdo.getPurchasingPriceEUR()));
            articleJdo.setOurs(dialog.getOursCheckBox().getActionCommand());
        } else if (dialog.getPresentCheckBox().isSelected()) {
            articleJdo.setPriceUAH(0);
            articleJdo.setOurs(dialog.getPresentCheckBox().getActionCommand());
        }
        articleServiceSwingWrapper.update(articleJdo);

        try {
            articleServiceSwingWrapper.updateToSpreadsheet(articleJdo);
        } catch (Exception e) {        // todo change to Custom exception
            e.printStackTrace();
            articleJdo.setPostponedOperationDate(new Date());
            dialog.getMainForm().getHandler().showPostponedOperationsMessage();
            dialog.showMessage("mainForm.exception.message.dialog.title", "sellDialog.handler.sold.article.not.saved.to.worksheet");
        }

        dialog.hide();
        dialog.getMainForm().update();
        dialog.getMainForm().show();
    }

    private double exchangeEurToUah(double priceEur) {
        return priceEur * exchangerHolder.getSellRateEUR();
    }

    public void oursCheckBoxSelected() {
        ArticleJdo articleJdo = tableModel.getSelectedArticle();
        double sellingPrice = (exchangeEurToUah(articleJdo.getPurchasingPriceEUR()));
        dialog.getPriceField().setText(String.valueOf(sellingPrice));
    }

    public void checkBoxDeSelected() {
        ArticleJdo articleJdo = tableModel.getSelectedArticle();
        dialog.getPriceField().setText(String.valueOf(articleJdo.getPriceUAH()));
    }

    public void presentCheckBoxSelected() {
        dialog.getPriceField().setText(String.valueOf(0));
    }
}
