package com.lavida.swing.handler;

import com.lavida.service.ArticleService;
import com.lavida.service.entity.ArticleJdo;
import com.lavida.swing.ExchangerHolder;
import com.lavida.swing.LocaleHolder;
import com.lavida.swing.dialog.SellDialog;
import com.lavida.swing.form.MainForm;
import com.lavida.swing.form.tablemodel.ArticlesTableModel;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
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
public class SellDialogHandler implements MessageSourceAware {

    @Resource
    private SellDialog dialog;

    @Resource
    private MessageSource messageSource;

    @Resource
    protected LocaleHolder localeHolder;

    @Resource
    private ArticleService articleService;


//    @Resource
//    private MainFormHandler mainFormHandler;

    @Resource
    private ExchangerHolder exchangerHolder;

//    private double sellingPrice;
//    private double startPrice;


    /**
     * Performs selling operation.
     *
     * @param articleJdo //     * @param articlesTableModel
     */
    public void sell(ArticleJdo articleJdo) {
        articleJdo.setSaleDate(Calendar.getInstance());
        articleJdo.setSold(messageSource.getMessage("sellDialog.button.sell.clicked.sold", null, localeHolder.getLocale()));
        articleJdo.setComment(articleJdo.getComment() + "; " + dialog.getCommentTextArea().getText());
        if ( dialog.getOursCheckBox().isSelected()) {
            articleJdo.setPriceUAH(exchangeEurToUah(articleJdo.getPurchasingPriceEUR()));
            articleJdo.setOurs(dialog.getOursCheckBox().getActionCommand());
        } else if (dialog.getPresentCheckBox().isSelected()) {
            articleJdo.setPriceUAH(0);
            articleJdo.setOurs(dialog.getPresentCheckBox().getActionCommand());
        }
        articleService.update(articleJdo);
        dialog.getMainForm().getTableModel().removeArticle(articleJdo);

        try {
            articleService.updateToSpreadsheet(articleJdo);
        } catch (Throwable e) {        // todo change to Custom exception
            e.printStackTrace();
            articleJdo.setPostponedOperationDate(new Date());
            articleService.update(articleJdo);
            dialog.getMainForm().getHandler().showPostponedOperationsMessage();
            dialog.showMessage("mainForm.exception.message.dialog.title", "sellDialog.handler.sold.article.not.saved.to.worksheet");
        }

        dialog.hide();
        dialog.getMainForm().update();
        dialog.getMainForm().show();
    }

    private double exchangeEurToUah(double priceEur) {
        double priceUah = priceEur * exchangerHolder.getSellRateEUR();
        return priceUah;
    }

    public void oursCheckBoxSelected() {
        ArticleJdo articleJdo = dialog.getMainForm().getTableModel().getSelectedArticle();
        double sellingPrice = (exchangeEurToUah(articleJdo.getPurchasingPriceEUR()));
        dialog.getPriceField().setText(String.valueOf(sellingPrice));
    }

    public void checkBoxDeSelected() {
        ArticleJdo articleJdo = dialog.getMainForm().getTableModel().getSelectedArticle();
        dialog.getPriceField().setText(String.valueOf(articleJdo.getPriceUAH()));
    }

    public void presentCheckBoxSelected() {
        dialog.getPriceField().setText(String.valueOf(0));
    }

    @Override
    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public void setDialog(SellDialog dialog) {
        this.dialog = dialog;
    }

    public void setLocaleHolder(LocaleHolder localeHolder) {
        this.localeHolder = localeHolder;
    }

    public void setArticleService(ArticleService articleService) {
        this.articleService = articleService;
    }

    public void setExchangerHolder(ExchangerHolder exchangerHolder) {
        this.exchangerHolder = exchangerHolder;
    }
}
