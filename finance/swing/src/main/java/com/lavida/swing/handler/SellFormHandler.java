package com.lavida.swing.handler;

import com.google.gdata.util.ServiceException;
import com.lavida.service.ArticleService;
import com.lavida.service.SoldArticleService;
import com.lavida.service.entity.ArticleJdo;
import com.lavida.service.entity.SoldArticleJdo;
import com.lavida.service.transformer.ArticlesTransformer;
import com.lavida.swing.LocaleHolder;
import com.lavida.swing.form.SellForm;
import com.lavida.swing.form.tablemodel.ArticlesTableModel;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created: 12:04 12.08.13
 * The SellFormHandler is the handler for logic methods for selling goods.
 * @author Ruslan
 */
@Component
public class SellFormHandler implements MessageSourceAware {
    @Resource
    private SellForm form;
    @Resource
    private MessageSource messageSource;
    @Resource
    protected LocaleHolder localeHolder;
    @Resource
    private ArticlesTransformer articlesTransformer;
    @Resource
    private SoldArticleService soldArticleService;
    @Resource
    private ArticleService articleService;


    /**
     * Performs selling operation.
     */
    public void sell(ArticlesTableModel tableModel, SoldArticleJdo soldArticleJdo) {
        soldArticleJdo.setSaleDate(Calendar.getInstance());
        soldArticleJdo.setSold(messageSource.getMessage("sellForm.button.sell.clicked.sold", null,
                localeHolder.getLocale()));
        soldArticleJdo.setComment((form.getCommentField().getText() == null)? null : form.getCommentField().getText().trim());
        String ours = (form.getOursButtonGroup().getSelection().getActionCommand() == null)? null :
                form.getOursButtonGroup().getSelection().getActionCommand();
//               another way
//                java.awt.Component[] components = form.getOursPanel().getComponents();
//                for (java.awt.Component component :components) {
//                    JCheckBox checkBox = (JCheckBox)component;
//                    if (checkBox.isSelected()){
//                        ours = checkBox.getText();
//                    }
//                }

        soldArticleJdo.setOurs(ours);
        ArticleJdo articleJdo = articlesTransformer.toArticleJdo(soldArticleJdo);
        articleService.update(articleJdo);
        tableModel.updateArticle(articleJdo);
        try{
            articleService.updateToSpreadsheet(articleJdo);

        }  catch (Exception e) {
            e.printStackTrace();
            soldArticleService.save(soldArticleJdo);
            form.showMessage(messageSource.getMessage("mainForm.exception.message.dialog.title", null, localeHolder.getLocale()),
                    messageSource.getMessage("sellForm.handler.sold.article.not.saved.to.worksheet", null, localeHolder.getLocale()));
        }

    }

    public void setForm(SellForm form) {
        this.form = form;
    }

    public SellForm getForm() {
        return form;
    }

    @Override
    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public void setLocaleHolder(LocaleHolder localeHolder) {
        this.localeHolder = localeHolder;
    }

    public void setArticlesTransformer(ArticlesTransformer articlesTransformer) {
        this.articlesTransformer = articlesTransformer;
    }

    public void setSoldArticleService(SoldArticleService soldArticleService) {
        this.soldArticleService = soldArticleService;
    }

    public void setArticleService(ArticleService articleService) {
        this.articleService = articleService;
    }
}
