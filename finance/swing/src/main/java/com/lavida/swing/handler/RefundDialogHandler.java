package com.lavida.swing.handler;

import com.lavida.service.entity.ArticleJdo;
import com.lavida.swing.LocaleHolder;
import com.lavida.swing.dialog.RefundDialog;
import com.lavida.swing.exception.LavidaSwingRuntimeException;
import com.lavida.swing.exception.RemoteUpdateException;
import com.lavida.swing.service.ArticleServiceSwingWrapper;
import com.lavida.swing.service.ConcurrentOperationsService;
import org.springframework.context.MessageSource;
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
//    private static final Logger logger = LoggerFactory.getLogger(RefundDialogHandler.class);

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
        ArticleJdo oldArticle;
        try {
            oldArticle = (ArticleJdo)articleJdo.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
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
        updateArticle(oldArticle, articleJdo);
        dialog.hide();
        dialog.getMainForm().update();
        dialog.getSoldProductsDialog().getTableModel().setSelectedArticle(null);
        dialog.getSoldProductsDialog().getDialog().repaint();
        dialog.getSoldProductsDialog().show();
    }

    private void updateArticle(final ArticleJdo oldArticleJdo, final ArticleJdo articleJdo) {
        concurrentOperationsService.startOperation("Refunding", new Runnable() {
            @Override
            public void run() {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(messageSource.getMessage("dialog.refund.finished.message", null, localeHolder.getLocale()));
                stringBuilder.append("\n");
                boolean postponed = false;
                RemoteUpdateException exception = null;
                try {
                    articleServiceSwingWrapper.updateToSpreadsheet(oldArticleJdo, articleJdo, false);
                } catch (RemoteUpdateException e) {
                    exception = e;
                    postponed = true;
                }
                String message = convertToMultiline(new String(stringBuilder));
                dialog.getMainForm().showInfoToolTip(message);
                if (postponed) {
                    throw new LavidaSwingRuntimeException(LavidaSwingRuntimeException.GOOGLE_SERVICE_EXCEPTION, exception);
                }
            }
        });
    }

    private String convertToMultiline(String orig) {
        return "<html>" + orig.replaceAll("\n", "<br>"); //+ "</html>"
    }

}
