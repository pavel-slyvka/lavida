package com.lavida.swing.handler;

import com.google.gdata.util.ServiceException;
import com.lavida.service.entity.ArticleJdo;
import com.lavida.swing.LocaleHolder;
import com.lavida.swing.dialog.AddNewProductsDialog;
import com.lavida.swing.dialog.SoldProductsDialog;
import com.lavida.swing.service.ArticleServiceSwingWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * The AddNewProductsDialogHandler is a handler for the {@link com.lavida.swing.dialog.AddNewProductsDialog}.
 * Created: 21:57 02.09.13
 *
 * @author Ruslan
 */
@Component
public class AddNewProductsDialogHandler {
    private static final Logger logger = LoggerFactory.getLogger(AddNewProductsDialogHandler.class);

    @Resource
    private AddNewProductsDialog dialog;

    @Resource
    private MessageSource messageSource;

    @Resource
    private LocaleHolder localeHolder;

    @Resource
    private ArticleServiceSwingWrapper articleServiceSwingWrapper;


    public void addRowButtonClicked() {
        ArticleJdo articleJdo = new ArticleJdo();
        dialog.getTableModel().getTableData().add(articleJdo);
        dialog.getTableModel().fireTableDataChanged();
        dialog.getArticleTableComponent().updateAnalyzeComponent();
    }

    public void deleteRowButtonClicked() {
        ArticleJdo selectedArticle = dialog.getTableModel().getSelectedArticle();
        dialog.getTableModel().getTableData().remove(selectedArticle);
        dialog.getTableModel().setSelectedArticle(null);
        dialog.getTableModel().fireTableDataChanged();
        dialog.getArticleTableComponent().updateAnalyzeComponent();
    }

    public void cancelButtonClicked() {
        dialog.getTableModel().setSelectedArticle(null);
        dialog.getTableModel().setTableData(new ArrayList<ArticleJdo>());
        dialog.getTableModel().fireTableDataChanged();
        dialog.hide();
        dialog.getMainForm().update();
    }

    public void acceptProductsButtonClicked() {
        List<ArticleJdo> newArticles = dialog.getTableModel().getTableData();
        for (ArticleJdo newArticle : newArticles) {
            try {
                articleServiceSwingWrapper.updateToSpreadsheet(newArticle, null);
                articleServiceSwingWrapper.update(newArticle);
            } catch (IOException e) {
                logger.warn(e.getMessage(), e);
                newArticle.setPostponedOperationDate(new Date());
                articleServiceSwingWrapper.update(newArticle);
            } catch (ServiceException e) {
                logger.warn(e.getMessage(), e);
                newArticle.setPostponedOperationDate(new Date());
                articleServiceSwingWrapper.update(newArticle);
            }
        }
        dialog.getTableModel().getTableData().removeAll(newArticles);
        dialog.getTableModel().fireTableDataChanged();
        dialog.getArticleTableComponent().updateAnalyzeComponent();
    }
}
