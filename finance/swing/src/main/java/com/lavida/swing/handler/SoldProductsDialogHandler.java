package com.lavida.swing.handler;

import com.lavida.service.entity.ArticleJdo;
import com.lavida.swing.form.component.TablePrintPreviewComponent;
import com.lavida.swing.LocaleHolder;
import com.lavida.swing.dialog.RefundDialog;
import com.lavida.swing.dialog.SoldProductsDialog;
import com.lavida.swing.service.ArticlesTableModel;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Created: 15:32 18.08.13
 * The SoldProductsDialogHandler is the handler for the SoldProductsDialog.
 *
 * @author Ruslan
 */
@Component
public class SoldProductsDialogHandler {
//    private static final Logger logger = LoggerFactory.getLogger(SoldProductsDialogHandler.class);

    @Resource
    private SoldProductsDialog dialog;

    @Resource
    private RefundDialog refundDialog;

    @Resource
    private MessageSource messageSource;

    @Resource
    private LocaleHolder localeHolder;

    @Resource(name = "soldArticleTableModel")
    private ArticlesTableModel tableModel;

    /**
     * Handles refund button clicking.
     */
    public void refundButtonClicked() {
        if (tableModel.getSelectedArticle() != null) {
            refundDialog.initWithArticleJdo(tableModel.getSelectedArticle());
            refundDialog.show();

        } else {
            dialog.showWarningMessage("mainForm.exception.message.dialog.title", "mainForm.handler.sold.article.not.chosen");
        }
    }

    /**
     * Handles the CancelButton clicking.
     */
    public void cancelButtonClicked() {
        dialog.getDialog().setVisible(false);
    }

    public void printItemClicked() {
        TablePrintPreviewComponent tablePrintPreviewComponent = new TablePrintPreviewComponent();
        boolean done = tablePrintPreviewComponent.showPrintPreviewDialog(dialog.getDialog(), dialog.getArticleTableComponent().getArticlesTable(),
                messageSource, localeHolder);
        if (done) {
            dialog.showInformationMessage("mainForm.menu.table.print.message.title",
                    messageSource.getMessage("mainForm.menu.table.print.finished.message.body", null, localeHolder.getLocale()));
        } else {
            dialog.showInformationMessage("mainForm.menu.table.print.message.title",
                    messageSource.getMessage("mainForm.menu.table.print.cancel.message.body", null, localeHolder.getLocale()));
        }


/*
        MessageFormat header = new MessageFormat(messageSource.getMessage("dialog.sold.menu.file.print.header", null, localeHolder.getLocale()));
        MessageFormat footer = new MessageFormat(messageSource.getMessage("mainForm.menu.table.print.footer", null, localeHolder.getLocale()));
        boolean fitPageWidth = false;
        boolean showPrintDialog = true;
        boolean interactive = true;
        JTable.PrintMode printMode = fitPageWidth ? JTable.PrintMode.FIT_WIDTH : JTable.PrintMode.NORMAL;
        try {
            boolean complete = dialog.getArticleTableComponent().getArticlesTable().print(printMode, header, footer,
                    showPrintDialog, null, interactive, null);
            if (complete) {
                dialog.showInformationMessage("mainForm.menu.table.print.message.title",
                        messageSource.getMessage("mainForm.menu.table.print.finished.message.body", null, localeHolder.getLocale()));
            } else {
                dialog.showInformationMessage("mainForm.menu.table.print.message.title",
                        messageSource.getMessage("mainForm.menu.table.print.cancel.message.body", null, localeHolder.getLocale()));
            }
        } catch (PrinterException e) {
            logger.warn(e.getMessage(), e);
            dialog.showWarningMessage("mainForm.exception.message.dialog.title", "mainForm.handler.print.exception.message");
        }
*/
    }

    public void deselectArticlesItemClicked() {
        for (ArticleJdo articleJdo : dialog.getTableModel().getTableData()) {
            if (articleJdo.isSelected()) {
                articleJdo.setSelected(false);
            }
        }
        dialog.getTableModel().fireTableDataChanged();
    }

}
