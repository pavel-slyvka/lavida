package com.lavida.swing.handler;

import com.lavida.swing.LocaleHolder;
import com.lavida.swing.dialog.RefundDialog;
import com.lavida.swing.dialog.SoldProductsDialog;
import com.lavida.swing.form.component.ArticleFiltersComponent;
import com.lavida.swing.service.ArticlesTableModel;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * Created: 15:32 18.08.13
 * The SoldProductsDialogHandler is the handler for the SoldProductsDialog.
 *
 * @author Ruslan
 */
@Component
public class SoldProductsDialogHandler {

    @Resource
    private SoldProductsDialog soldProductsDialog;

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
            soldProductsDialog.showMessage("mainForm.exception.message.dialog.title", "mainForm.handler.sold.article.not.chosen");
        }
    }

    /**
     * Handles the CancelButton clicking.
     */
    public void cancelButtonClicked() {
        soldProductsDialog.getDialog().setVisible(false);
    }

    /**
     * Handles the CurrentDateCheckBox selecting.
     */
    public void currentDateCheckBoxSelected() {
        String currentDate = new SimpleDateFormat("dd.MM.yyyy").format(Calendar.getInstance().getTime());

        List<ArticleFiltersComponent.FilterUnit> filters = soldProductsDialog.getArticleTableComponent().
                getArticleFiltersComponent().getFilters();
        for (ArticleFiltersComponent.FilterUnit filterUnit : filters) {
            if (messageSource.getMessage("mainForm.table.articles.column.sell.date.title", null, localeHolder.getLocale()).
                    equalsIgnoreCase(filterUnit.columnTitle)) {
                filterUnit.textField.setText(currentDate);
            }
        }
    }

    /**
     * Handles the CurrentDateCheckBox deselecting.
     */
    public void currentDateCheckBoxDeSelected() {
        List<ArticleFiltersComponent.FilterUnit> filters = soldProductsDialog.getArticleTableComponent().
                getArticleFiltersComponent().getFilters();
        for (ArticleFiltersComponent.FilterUnit filterUnit : filters) {
            if (messageSource.getMessage("mainForm.table.articles.column.sell.date.title", null, localeHolder.getLocale()).
                    equalsIgnoreCase(filterUnit.columnTitle)) {
                filterUnit.textField.setText("");
            }
        }
    }
}
