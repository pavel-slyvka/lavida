package com.lavida.swing.handler;

import com.google.gdata.util.ServiceException;
import com.lavida.service.entity.ArticleJdo;
import com.lavida.swing.LocaleHolder;
import com.lavida.swing.dialog.RefundDialog;
import com.lavida.swing.service.ArticleServiceSwingWrapper;
import com.lavida.swing.service.ConcurrentOperationsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Date;

/**
 * Created: 21:50 18.08.13
 * The RefundDialogHandler is a handler for the RefundDialog.
 * @author Ruslan
 */

@Component
public class RefundDialogHandler {
    private static final Logger logger = LoggerFactory.getLogger(RefundDialogHandler.class);

    @Resource
    RefundDialog dialog;

    @Resource
    private ArticleServiceSwingWrapper articleServiceSwingWrapper;

    @Resource
    private ConcurrentOperationsService concurrentOperationsService;

    @Resource
    private MessageSource messageSource;

    @Resource
    protected LocaleHolder localeHolder;


    /**
     * Refunds article from  a consumer to the stock. Makes the article not sold.
     * @param articleJdo the selected articleJdo to be refunded.
     */
    public void refundButtonClicked (ArticleJdo articleJdo){
        if (dialog.getCommentTextField().getText().trim().isEmpty() ||
                dialog.getCommentTextField().getText().trim().equals(articleJdo.getComment() != null ? articleJdo.getComment() : "")) {
            dialog.showWarningMessage("mainForm.exception.message.dialog.title", "refundDialog.handler.comment.not.entered");
            dialog.getCommentTextField().requestFocusInWindow();
            return;
        }
        articleJdo.setComment(dialog.getCommentTextField().getText().trim());
        if (articleJdo.getOldSalePrice() > 0.0) {
            articleJdo.setSalePrice(articleJdo.getOldSalePrice());
            articleJdo.setOldSalePrice(0.0);
        }
        articleJdo.setSold(null);
        articleJdo.setSellType(null);
        Date refundDate = new Date();
        articleJdo.setRefundDate(refundDate);
        articleJdo.setSaleDate(null);
        articleJdo.setTags(null);
        updateArticle(articleJdo);
        dialog.hide();
        dialog.getMainForm().update();
        dialog.getSoldProductsDialog().getTableModel().setSelectedArticle(null);
        dialog.getSoldProductsDialog().getDialog().repaint();
        dialog.getSoldProductsDialog().show();
    }

    private void updateArticle(final ArticleJdo articleJdo) {
        concurrentOperationsService.startOperation("Refunding", new Runnable() {
            @Override
            public void run() {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(messageSource.getMessage("dialog.refund.finished.message", null, localeHolder.getLocale()));
                stringBuilder.append("\n");
                try {
                    articleServiceSwingWrapper.updateToSpreadsheet(articleJdo, false);
                } catch (IOException | ServiceException e) {
                    logger.warn(e.getMessage(), e);
                    stringBuilder.append(messageSource.getMessage("sellDialog.handler.sold.article.not.saved.to.worksheet", null, localeHolder.getLocale()));
                    articleJdo.setPostponedOperationDate(new Date());
                }
                articleServiceSwingWrapper.update(articleJdo);
                String message = convertToMultiline(new String(stringBuilder));
                dialog.getMainForm().showInfoToolTip(message);
            }
        });
    }

    private String convertToMultiline(String orig) {
        return "<html>" + orig.replaceAll("\n", "<br>"); //+ "</html>"
    }

}
