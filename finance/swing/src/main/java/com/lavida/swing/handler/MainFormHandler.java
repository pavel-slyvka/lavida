package com.lavida.swing.handler;

import com.google.gdata.util.ServiceException;
import com.lavida.service.entity.ArticleJdo;
import com.lavida.swing.LocaleHolder;
import com.lavida.swing.dialog.SellDialog;
import com.lavida.swing.exception.LavidaSwingRuntimeException;
import com.lavida.swing.form.MainForm;
import com.lavida.swing.service.ArticleServiceSwingWrapper;
import com.lavida.swing.service.ArticlesTableModel;
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
    private ArticleServiceSwingWrapper articleServiceSwingWrapper;

    @Resource
    protected LocaleHolder localeHolder;

    @Resource(name = "notSoldArticleTableModel")
    private ArticlesTableModel tableModel;

    /**
     * The ActionListener for refreshButton component.
     * When button is clicked data for ArticleJdo List loads from Google spreadsheet
     * and saves to database, then it goes through filterSold() for deleting all sold
     * goods and renders to the articleTable.
     */
    public void refreshButtonClicked() {
        try {
            List<ArticleJdo> articles = articleServiceSwingWrapper.loadArticlesFromRemoteServer();
            articleServiceSwingWrapper.update(articles);
            form.update();    // repaint MainForm in some time

        } catch (IOException e) {
            throw new LavidaSwingRuntimeException(LavidaSwingRuntimeException.GOOGLE_IO_EXCEPTION, e, form);
        } catch (ServiceException e) {
            throw new LavidaSwingRuntimeException(LavidaSwingRuntimeException.GOOGLE_SERVICE_EXCEPTION, e, form);
        }
    }


    public void sellButtonClicked() {
        if (tableModel.getSelectedArticle() != null) {
            sellDialog.initWithArticleJdo(tableModel.getSelectedArticle());
            sellDialog.show();

        } else {
            form.showMessage("mainForm.exception.message.dialog.title", "mainForm.handler.sold.article.not.chosen");
        }
    }

    public void recommitButtonClicked() {
        List<ArticleJdo> articles = articleServiceSwingWrapper.getAll();
        for (ArticleJdo articleJdo : articles) {
            if (articleJdo.getPostponedOperationDate() != null) {
                try {
                    articleServiceSwingWrapper.updateToSpreadsheet(articleJdo);
                    articleJdo.setPostponedOperationDate(null);
                    showPostponedOperationsMessage();
                } catch (IOException e) {
                    form.showMessage("mainForm.exception.message.dialog.title", "sellDialog.handler.sold.article.not.saved.to.worksheet");
                    showPostponedOperationsMessage();
                    throw new LavidaSwingRuntimeException(LavidaSwingRuntimeException.GOOGLE_IO_EXCEPTION, e, form);
                } catch (ServiceException e) {
                    form.showMessage("mainForm.exception.message.dialog.title", "sellDialog.handler.sold.article.not.saved.to.worksheet");
                    showPostponedOperationsMessage();
                    throw new LavidaSwingRuntimeException(LavidaSwingRuntimeException.GOOGLE_SERVICE_EXCEPTION, e, form);
                }
            }
        }
    }

    public void showPostponedOperationsMessage() {
        int count = 0;
        List<ArticleJdo> articles = articleServiceSwingWrapper.getAll();
        for (ArticleJdo articleJdo : articles) {
            if (articleJdo.getPostponedOperationDate() != null) {
                ++count;
            }
        }
        form.getPostponedMessage().setText(count + "!");
    }
}
