package com.lavida.swing.handler;

import com.lavida.service.ArticleService;
import com.lavida.service.entity.ArticleJdo;
import com.lavida.swing.ExchangerHolder;
import com.lavida.swing.LocaleHolder;
import com.lavida.swing.dialog.SellDialog;
import com.lavida.swing.form.MainForm;
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

    @Resource
    private MainForm mainForm;

    @Resource
    private MainFormHandler mainFormHandler;

    @Resource
    private ExchangerHolder exchangerHolder;

    private double sellingPrice;
    private double startPrice;


    /**
     * Performs selling operation.
     *
     * @param articleJdo //     * @param articlesTableModel
     */
    public void sell(ArticleJdo articleJdo) {
        articleJdo.setSaleDate(Calendar.getInstance());
        articleJdo.setSold(messageSource.getMessage("sellDialog.button.sell.clicked.sold", null, localeHolder.getLocale()));
        articleJdo.setComment(dialog.getCommentTextArea().getText());
        articleService.update(articleJdo);
        mainForm.getTableModel().removeArticle(articleJdo);

        try {
            articleService.updateToSpreadsheet(articleJdo);
        } catch (Throwable e) {        // todo change to Custom exception
            e.printStackTrace();
            articleJdo.setPostponedOperationDate(new Date());
            articleService.update(articleJdo);
            mainFormHandler.showPostponedOperationsMessage();
            dialog.showMessage("mainForm.exception.message.dialog.title", "sellDialog.handler.sold.article.not.saved.to.worksheet");
        }

        dialog.hide();
        mainForm.update();
        mainForm.show();
    }

/*
    private double exchangeEurToUah(double priceEur) {
        double priceUah = priceEur * exchangerHolder.getSellRateEUR();
        return priceUah;
    }

    public void oursCheckBoxSelected(ArticleJdo articleJdo) {
        sellingPrice = (exchangeEurToUah(articleJdo.getPurchasingPriceEUR()));
        dialog.getPriceField().setText(String.valueOf(sellingPrice));
        articleJdo.setPriceUAH(sellingPrice);
    }
    public void oursCheckBoxDeSelected(ArticleJdo articleJdo) {

    }

    public void presentCheckBoxSelected (ArticleJdo articleJdo) {
        sellingPrice = 0;
        dialog.getPriceField().setText(String.valueOf(sellingPrice));
        articleJdo.setPriceUAH(sellingPrice);
    }
*/

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

    public void setMainForm(MainForm mainForm) {
        this.mainForm = mainForm;
    }

    public void setMainFormHandler(MainFormHandler mainFormHandler) {
        this.mainFormHandler = mainFormHandler;
    }

    public void setExchangerHolder(ExchangerHolder exchangerHolder) {
        this.exchangerHolder = exchangerHolder;
    }
}
