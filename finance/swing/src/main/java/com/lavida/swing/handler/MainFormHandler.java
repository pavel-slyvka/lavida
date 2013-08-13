package com.lavida.swing.handler;

import com.google.gdata.util.ServiceException;
import com.lavida.service.ArticleService;
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
    private MessageSource messageSource;
    @Resource
    protected LocaleHolder localeHolder;


    private SoldArticleJdo soldArticleJdo;


    /**
     * Sets table header and data to articlesTableModel received from google spreadsheet.
     */
    public void initTableModel(ArticlesTableModel tableModel) {
//        try {
            tableModel.initHeaderFieldAndTitles(messageSource, localeHolder.getLocale());
            tableModel.setTableData(articleService.getNotSoldArticles());
        // todo review try catches
//        } catch (IOException e) {
//            throw new LavidaSwingRuntimeException(LavidaSwingRuntimeException.GOOGLE_IO_EXCEPTION, e, form);
//        } catch (ServiceException e) {
//            throw new LavidaSwingRuntimeException(LavidaSwingRuntimeException.GOOGLE_SERVICE_EXCEPTION, e, form);
//        }
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
            initTableModel(tableModel);
            form.update();    // repaint MainForm in some time

        } catch (IOException e) {
            throw new LavidaSwingRuntimeException(LavidaSwingRuntimeException.GOOGLE_IO_EXCEPTION, e, form);
        } catch (ServiceException e) {
            throw new LavidaSwingRuntimeException(LavidaSwingRuntimeException.GOOGLE_SERVICE_EXCEPTION, e, form);
        }
    }

    public void sellButtonClicked(ArticlesTableModel tableModel, int selectedRow) {
        List<ArticleJdo> articles = tableModel.getTableData();
        int articleId = (Integer)tableModel.getValueAt(selectedRow, 0);
        for (ArticleJdo articleJdo : articles) {
            if (articleJdo.getId() == articleId) {
                this.soldArticleJdo = articlesTransformer.toSoldArticleJdo(articleJdo);
            }
        }
        if (this.soldArticleJdo == null) {
            form.showMessage("mainForm.exception.message.dialog.title","mainForm.handler.sold.article.not.chosen");
        } else {
            sellForm.getCodeLabel().setText(messageSource.getMessage("sellForm.label.code.title", null, localeHolder.getLocale())
                    + ((soldArticleJdo.getCode() == null)? null : soldArticleJdo.getCode()));
            sellForm.getNameLabel().setText(messageSource.getMessage("sellForm.label.name.title", null, localeHolder.getLocale())
                    + ((soldArticleJdo.getName() == null) ? null : soldArticleJdo.getName()));
            sellForm.getPriceLabel().setText(messageSource.getMessage("sellForm.label.price.title",
                    null, localeHolder.getLocale()) + currentPrice(soldArticleJdo));
            sellForm.getCommentLabel().setText(messageSource.getMessage("sellForm.label.comment.title",
                    null, localeHolder.getLocale()));

            sellForm.getOursCheckBox().setText(messageSource.getMessage("sellForm.checkBox.ours.text", null,
                    localeHolder.getLocale()));
            sellForm.getOursCheckBox().setActionCommand(messageSource.getMessage("sellForm.checkBox.ours.text", null,
                    localeHolder.getLocale()));
            sellForm.getPresentCheckBox().setText(messageSource.getMessage("sellForm.checkBox.present.text", null,
                    localeHolder.getLocale()));
            sellForm.getPresentCheckBox().setActionCommand(messageSource.getMessage("sellForm.checkBox.present.text", null,
                    localeHolder.getLocale()));

            sellForm.show();
            sellForm.setSoldArticleJdo(soldArticleJdo);
            form.getForm().setVisible(false);
        }
    }

    /**
     * Determines current price of the article to be sold.
     *
     * @param soldArticleJdo the article to be sold;
     * @return double value of current prise.
     */
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

    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public void setLocaleHolder(LocaleHolder localeHolder) {
        this.localeHolder = localeHolder;
    }
}
