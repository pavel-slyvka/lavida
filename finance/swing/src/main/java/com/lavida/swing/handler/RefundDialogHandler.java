package com.lavida.swing.handler;

import com.lavida.service.entity.ArticleJdo;
import com.lavida.swing.LocaleHolder;
import com.lavida.swing.dialog.RefundDialog;
import com.lavida.swing.service.ArticleServiceSwingWrapper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;

/**
 * Created: 21:50 18.08.13
 * The RefundDialogHandler is a handler for the RefundDialog.
 * @author Ruslan
 */

@Component
public class RefundDialogHandler {

    @Resource
    RefundDialog refundDialog;

    @Resource
    protected LocaleHolder localeHolder;

    @Resource
    private ArticleServiceSwingWrapper articleServiceSwingWrapper;

    /**
     * Refunds article from  a consumer to the stock. Makes the article not sold.
     * @param articleJdo the selected articleJdo to be refunded.
     */
    public void refundButtonClicked (ArticleJdo articleJdo){
        if (refundDialog.getCommentTextField().getText().trim().isEmpty()) {
            refundDialog.showWarningMessage("mainForm.exception.message.dialog.title", "refundDialog.handler.comment.not.entered");
            refundDialog.getCommentTextField().requestFocusInWindow();
            return;
        }
        articleJdo.setComment(refundDialog.getCommentTextField().getText().trim());
        if (articleJdo.getOldSalePrice() > 0.0) {
            articleJdo.setSalePrice(articleJdo.getOldSalePrice());
            articleJdo.setOldSalePrice(0.0);
        }
        articleJdo.setSold(null);
        articleJdo.setSellType(null);
        articleJdo.setRefundDate(new Date());
        articleJdo.setSaleDate(null);
        articleJdo.setTags(null);
        articleServiceSwingWrapper.update(articleJdo);
        try {
            articleServiceSwingWrapper.updateToSpreadsheet(articleJdo, new Boolean(false));
        } catch (Exception e) {        // todo change to Custom exception
            e.printStackTrace();
            articleJdo.setPostponedOperationDate(new Date());
            articleServiceSwingWrapper.update(articleJdo);
            refundDialog.hide();
            refundDialog.getSoldProductsDialog().getTableModel().setSelectedArticle(null);
            refundDialog.getMainForm().update();
            refundDialog.getMainForm().getHandler().showPostponedOperationsMessage();
            refundDialog.showWarningMessage("mainForm.exception.message.dialog.title", "sellDialog.handler.sold.article.not.saved.to.worksheet");
            refundDialog.getSoldProductsDialog().getDialog().repaint();
            return;
        }
        refundDialog.hide();
        refundDialog.getMainForm().update();
        refundDialog.getSoldProductsDialog().getTableModel().setSelectedArticle(null);
        refundDialog.getSoldProductsDialog().getDialog().repaint();
        refundDialog.getSoldProductsDialog().show();
    }
}
