package com.lavida.swing.handler;

import com.google.gdata.util.ServiceException;
import com.lavida.service.ArticleService;
import com.lavida.service.entity.ArticleJdo;
import com.lavida.swing.exception.LavidaSwingRuntimeException;
import com.lavida.swing.form.MainForm;
import com.lavida.swing.form.SellForm;
import com.lavida.swing.form.tablemodel.ArticlesTableModel;
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

    /**
     * Sets table header and data to articlesTableModel received from google spreadsheet.
     */
    public void initTableModel(ArticlesTableModel tableModel) {
        try {
            tableModel.setTableHeader(articleService.loadTableHeader()); //todo change
            tableModel.setTableData(articleService.getNotSoldArticles());

        } catch (IOException e) {
            throw new LavidaSwingRuntimeException(LavidaSwingRuntimeException.GOOGLE_IO_EXCEPTION, e, form);
        } catch (ServiceException e) {
            throw new LavidaSwingRuntimeException(LavidaSwingRuntimeException.GOOGLE_SERVICE_EXCEPTION, e, form);
        }
    }

    /**
     * The ActionListener for refreshButton component.
     * When button is clicked data for ArticleJdo List loads from Google spreadsheet
     * and saves to database, then it goes through filterSold() for deleting all sold
     * goods and renders to the articleTable.
     */
    public void refreshButtonClicked(ArticlesTableModel tableModel) {
        try {
            List<ArticleJdo> articles = articleService.loadFromGoogle();
            articleService.update(articles);
            initTableModel(tableModel);
            form.update();    // repaint MainForm in some time

        } catch (IOException e) {
            throw new LavidaSwingRuntimeException(LavidaSwingRuntimeException.GOOGLE_IO_EXCEPTION, e, form);
        } catch (ServiceException e) {
            throw new LavidaSwingRuntimeException(LavidaSwingRuntimeException.GOOGLE_SERVICE_EXCEPTION, e, form);
        }
    }

    public void sellButtonClicked(ArticlesTableModel tableModel) {

    }

    public void setForm(MainForm form) {
        this.form = form;
    }

    public void setArticleService(ArticleService articleService) {
        this.articleService = articleService;
    }
}
