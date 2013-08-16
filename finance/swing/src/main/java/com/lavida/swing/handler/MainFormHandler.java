package com.lavida.swing.handler;

import com.google.gdata.util.ServiceException;
import com.lavida.service.ArticleService;
import com.lavida.service.entity.ArticleJdo;
import com.lavida.swing.LocaleHolder;
import com.lavida.swing.dialog.SellDialog;
import com.lavida.swing.exception.LavidaSwingRuntimeException;
import com.lavida.swing.form.MainForm;
import com.lavida.swing.form.tablemodel.ArticlesTableModel;
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
    private SellDialog sellDialog;

    @Resource
    private ArticleService articleService;

    @Resource
    protected LocaleHolder localeHolder;



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


    public void sellButtonClicked() {

        if (form.getTableModel().getSelectedArticle() != null) {
            sellDialog.initWithArticleJdo(form.getTableModel().getSelectedArticle());
            sellDialog.show();

        } else {
            form.showMessage("mainForm.exception.message.dialog.title", "mainForm.handler.sold.article.not.chosen");
        }
    }

    public void setForm(MainForm form) {
        this.form = form;
    }

    public void setArticleService(ArticleService articleService) {
        this.articleService = articleService;
    }

    public void setSellDialog(SellDialog sellDialog) {
        this.sellDialog = sellDialog;
    }

    public void setLocaleHolder(LocaleHolder localeHolder) {
        this.localeHolder = localeHolder;
    }

    public void recommitButtonClicked() {
        List<ArticleJdo> articles = articleService.getAll();
        for (ArticleJdo articleJdo : articles) {
            if (articleJdo.getPostponedOperationDate() != null) {
                try {
                    articleService.updateToSpreadsheet(articleJdo);
                    articleJdo.setPostponedOperationDate(null);
                    showPostponedOperationsMessage();
                } catch (IOException e) {
                    e.printStackTrace();
                    form.showMessage("mainForm.exception.message.dialog.title", "sellDialog.handler.sold.article.not.saved.to.worksheet");
                    showPostponedOperationsMessage();
                } catch (ServiceException e) {
                    form.showMessage("mainForm.exception.message.dialog.title", "sellDialog.handler.sold.article.not.saved.to.worksheet");
                    e.printStackTrace();
                    showPostponedOperationsMessage();

                }
            }
        }
    }

    public void showPostponedOperationsMessage() {
        int count = 0;
        List<ArticleJdo> articles = articleService. getAll();
        for (ArticleJdo articleJdo : articles) {
            if (articleJdo.getPostponedOperationDate() != null) {
                ++ count;
            }
        }
        form.getPostponedMessage().setText(count + "!");
    }
}
