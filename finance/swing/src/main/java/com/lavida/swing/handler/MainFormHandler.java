package com.lavida.swing.handler;

import com.google.gdata.util.ServiceException;
import com.lavida.service.ArticleService;
import com.lavida.service.UserService;
import com.lavida.service.entity.ArticleJdo;
import com.lavida.service.entity.SoldArticleJdo;
import com.lavida.service.transformer.ArticlesTransformer;
import com.lavida.swing.LocaleHolder;
import com.lavida.swing.exception.LavidaSwingRuntimeException;
import com.lavida.swing.form.MainForm;
import com.lavida.swing.form.SellForm;
import com.lavida.swing.form.tablemodel.ArticlesTableModel;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

/**
 * MainFormHandler
 * <p/>
 * Created: 21:14 09.08.13
 *
 * @author Pavel
 */
@Component
public class MainFormHandler {

    @Resource
    private MainForm form;

    @Resource
    private SellForm sellForm;

    @Resource
    private ArticleService articleService;

    @Resource
    private ArticlesTransformer articlesTransformer;


    @Resource
    protected LocaleHolder localeHolder;


    private SoldArticleJdo soldArticleJdo;


    /**
     * Sets table header and data to articlesTableModel received from google spreadsheet.
     */
    public void initTableModelWithData(ArticlesTableModel tableModel) {
        tableModel.setTableData(articleService.getNotSoldArticles());
    }

    /**
     * The ActionListener for refreshButton component.
     * When button is clicked data for ArticleJdo List loads from Google spreadsheet
     * and saves to database, then it goes through filterSold() for deleting all sold
     * goods and renders to the articleTable.
     */
    public void refreshButtonClicked(ArticlesTableModel tableModel) {
        try {
            List<ArticleJdo> articles = articleService.loadArticlesFromRemoteServer();
            articleService.update(articles);
            initTableModelWithData(tableModel);
            form.update();    // repaint MainForm in some time

        } catch (IOException e) {
            throw new LavidaSwingRuntimeException(LavidaSwingRuntimeException.GOOGLE_IO_EXCEPTION, e, form);
        } catch (ServiceException e) {
            throw new LavidaSwingRuntimeException(LavidaSwingRuntimeException.GOOGLE_SERVICE_EXCEPTION, e, form);
        }
    }

    public void sellButtonClicked(ArticlesTableModel tableModel, int selectedRow) {
        if (selectedRow >= 0) {
            ArticleJdo articleJdo = tableModel.getArticleJdoByRowIndex(selectedRow);
            sellForm.initWithArticleJdo(articleJdo);
            sellForm.show();

        } else {
            form.showMessage("mainForm.exception.message.dialog.title", "mainForm.handler.sold.article.not.chosen");
        }
    }

    /**
     * Determines current price of the article to be sold.
     *
     * @param soldArticleJdo the article to be sold;
     * @return double value of current prise.
     */
    @Deprecated
    public double currentPrice(SoldArticleJdo soldArticleJdo) {
        if (soldArticleJdo.getActionPriceUAH() > 0) {
            return soldArticleJdo.getActionPriceUAH();
        } else {
            return soldArticleJdo.getPriceUAH();
        }
    }


    public void setForm(MainForm form) {
        this.form = form;
    }

    public void setArticleService(ArticleService articleService) {
        this.articleService = articleService;
    }

    public void setSellForm(SellForm sellForm) {
        this.sellForm = sellForm;
    }

    public SoldArticleJdo getSoldArticleJdo() {
        return soldArticleJdo;
    }

    public void setArticlesTransformer(ArticlesTransformer articlesTransformer) {
        this.articlesTransformer = articlesTransformer;
    }

    public void setLocaleHolder(LocaleHolder localeHolder) {
        this.localeHolder = localeHolder;
    }
}
