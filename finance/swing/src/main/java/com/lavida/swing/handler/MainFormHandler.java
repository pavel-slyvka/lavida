package com.lavida.swing.handler;

import com.google.gdata.util.ServiceException;
import com.lavida.service.ArticleService;
import com.lavida.service.entity.ArticleJdo;
import com.lavida.service.utils.MyPropertiesUtil;
import com.lavida.swing.form.MainForm;
import com.lavida.swing.form.tablemodel.ArticlesTableModel;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.persistence.TransactionRequiredException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * MainFormHandler
 * <p/>
 * Created: 21:14 09.08.13
 *
 * @author Pavel
 */
@Component
public class MainFormHandler {
    private static final String filePath = "gmail.properties";  // file path for google account properties

    @Resource
    private MainForm form;

    @Resource
    private ArticleService articleService;

    private String userNameGmail;         // is initialised by loadGoogleAccountCredentials() method.
    private String passwordGmail;         // is initialised by loadGoogleAccountCredentials() method.

    /**
     * Sets table header and data to articlesTableModel received from google spreadsheet.
     */
    public void initTableModel(ArticlesTableModel tableModel) {
        try {
            tableModel.setTableHeader(articleService.loadTableHeader(userNameGmail, passwordGmail)); //todo change
            tableModel.setTableData(articleService.getNotSoldArticles());

        } catch (IOException e) {
            form.showErrorMessageByException(e);
        } catch (ServiceException e) {
            form.showErrorMessageByException(e);
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
            List<ArticleJdo> articles = articleService.loadFromGoogle(userNameGmail, passwordGmail);
            articleService.update(articles);
            initTableModel(tableModel);

        } catch (IOException e) {
            form.showMessage("mainForm.exception.message.dialog.title",
                    "mainForm.exception.io.load.google.spreadsheet.table.data");
        } catch (ServiceException e) {
            form.showMessage("mainForm.exception.message.dialog.title",
                    "mainForm.exception.service.load.google.spreadsheet.table.data");
        }
    }

    /**
     * Loads properties
     */
    public void loadGoogleAccountCredentials() {
        Properties properties = null;
        try {
            properties = MyPropertiesUtil.loadProperties(filePath);
            this.userNameGmail = properties.getProperty(MyPropertiesUtil.USER_NAME);
            this.passwordGmail = properties.getProperty(MyPropertiesUtil.PASSWORD);
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            form.showMessage("mainForm.exception.message.dialog.title",
                    "mainForm.exception.io.properties.load.google.account");
        }

    }

    public void setForm(MainForm form) {
        this.form = form;
    }

    public void setArticleService(ArticleService articleService) {
        this.articleService = articleService;
    }
}
