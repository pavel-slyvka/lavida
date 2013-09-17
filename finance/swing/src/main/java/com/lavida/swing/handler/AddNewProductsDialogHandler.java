package com.lavida.swing.handler;

import com.google.gdata.util.ServiceException;
import com.lavida.service.entity.ArticleJdo;
import com.lavida.swing.LocaleHolder;
import com.lavida.swing.dialog.AddNewProductsDialog;
import com.lavida.swing.service.ArticleServiceSwingWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.swing.*;
import java.awt.print.PrinterException;
import java.io.IOException;
import java.text.MessageFormat;
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
    private ArticleServiceSwingWrapper articleServiceSwingWrapper;

    @Resource
    protected MessageSource messageSource;

    @Resource
    protected LocaleHolder localeHolder;


    public void addRowButtonClicked() {
        ArticleJdo articleJdo = new ArticleJdo();
        articleJdo.setQuantity(1);
        articleJdo.setMultiplier(2.5);
        articleJdo.setSalePrice(-1.0);
        articleJdo.setShop(messageSource.getMessage("sellDialog.text.field.shop.LaVida", null, localeHolder.getLocale()));
        if (dialog.getTableModel().getTableData().size() > 0) {
            if (dialog.getTableModel().getTableData().get(0).getDeliveryDate() == null) {
                dialog.showWarningMessage("mainForm.exception.message.dialog.title", "dialog.add.new.product.deliveryDate.not.filled.message");
                return;
            } else {
                articleJdo.setDeliveryDate(dialog.getTableModel().getTableData().get(0).getDeliveryDate());
            }
        }
        dialog.getTableModel().getTableData().add(articleJdo);
        dialog.getTableModel().fireTableDataChanged();
        int row = dialog.getTableModel().getTableData().size() - 1;
        dialog.getArticleTableComponent().getArticlesTable().editCellAt(row, 0);
        dialog.getArticleTableComponent().getArticlesTable().transferFocus();
        dialog.getTableModel().setSelectedArticle(null);
    }

    public void deleteRowButtonClicked() {
        ArticleJdo selectedArticle = dialog.getTableModel().getSelectedArticle();
        dialog.getTableModel().getTableData().remove(selectedArticle);
        dialog.getTableModel().setSelectedArticle(null);
        dialog.getTableModel().fireTableDataChanged();
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
        while (newArticles.size() > 0) {
            ArticleJdo newArticle = newArticles.get(0);
            if (newArticle.getCode().isEmpty() || newArticle.getDeliveryDate() == null
                    || newArticle.getTotalCostUAH() == 0 || newArticle.getSalePrice() == -1.0) {
                dialog.showWarningMessage("mainForm.exception.message.dialog.title", "dialog.add.new.product.code.deliveryDate.totalCostUAH.not.filled.message");
                dialog.getTableModel().fireTableDataChanged();
                dialog.getArticleTableComponent().getArticleFiltersComponent().updateAnalyzeComponent();
                return;
            }
            try {
                articleServiceSwingWrapper.updateToSpreadsheet(newArticle, null);
            } catch (IOException | ServiceException e) {
                logger.warn(e.getMessage(), e);
                newArticle.setPostponedOperationDate(new Date());
                dialog.getMainForm().getHandler().showPostponedOperationsMessage();
            }
            articleServiceSwingWrapper.update(newArticle);
            newArticles.remove(newArticle);
        }
        dialog.getTableModel().fireTableDataChanged();
        dialog.getArticleTableComponent().getArticleFiltersComponent().updateAnalyzeComponent();
    }

    public void copyRowButtonClicked() {
        ArticleJdo copiedArticle;
        try {
            copiedArticle = (ArticleJdo) dialog.getTableModel().getSelectedArticle().clone();
        } catch (CloneNotSupportedException e) {
            logger.warn(e.getMessage(), e);
            return;
        }
        dialog.getTableModel().getTableData().add(copiedArticle);
        dialog.getTableModel().fireTableDataChanged();

    }

    public void printItemClicked() {
        MessageFormat header = new MessageFormat(messageSource.getMessage("dialog.add.new.products.menu.file.print.header", null, localeHolder.getLocale()));
        MessageFormat footer = new MessageFormat(messageSource.getMessage("mainForm.menu.file.print.footer", null, localeHolder.getLocale()));
        boolean fitPageWidth = false;
        boolean showPrintDialog = true;
        boolean interactive = true;
        JTable.PrintMode printMode = fitPageWidth ? JTable.PrintMode.FIT_WIDTH : JTable.PrintMode.NORMAL;
        try {
            boolean complete = dialog.getArticleTableComponent().getArticlesTable().print(printMode, header, footer,
                    showPrintDialog, null, interactive, null);
            if (complete) {
                dialog.showInformationMessage("mainForm.menu.file.print.message.title",
                        messageSource.getMessage("mainForm.menu.file.print.finished.message.body", null, localeHolder.getLocale()));
            } else {
                dialog.showInformationMessage("mainForm.menu.file.print.message.title",
                        messageSource.getMessage("mainForm.menu.file.print.cancel.message.body", null, localeHolder.getLocale()));
            }
        } catch (PrinterException e) {
            logger.warn(e.getMessage(), e);
            dialog.showWarningMessage("mainForm.exception.message.dialog.title", "mainForm.handler.print.exception.message");
        }

    }
}
