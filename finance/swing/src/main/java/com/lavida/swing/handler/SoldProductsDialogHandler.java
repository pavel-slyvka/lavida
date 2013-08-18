package com.lavida.swing.handler;

import com.lavida.swing.ExchangerHolder;
import com.lavida.swing.LocaleHolder;
import com.lavida.swing.dialog.SoldProductsDialog;
import com.lavida.swing.service.ArticleServiceSwingWrapper;
import com.lavida.swing.service.ArticlesTableModel;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Created: 15:32 18.08.13
 *
 * @author Ruslan
 */
@Component
public class SoldProductsDialogHandler {

    @Resource
    private SoldProductsDialog soldProductsDialog;

    @Resource
    private MessageSource messageSource;

    @Resource
    private LocaleHolder localeHolder;

    @Resource
    private ArticleServiceSwingWrapper articleServiceSwingWrapper;

    @Resource(name = "notSoldArticleTableModel")
    private ArticlesTableModel tableModel;

    @Resource
    private ExchangerHolder exchangerHolder;

    public void refundButtonClicked() {

    }
}
