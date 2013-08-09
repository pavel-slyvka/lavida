package com.lavida.swing.handler;

import com.google.gdata.util.ServiceException;
import com.lavida.service.ArticleService;
import com.lavida.service.entity.ArticleJdo;
import com.lavida.service.utils.MyPropertiesUtil;
import com.lavida.swing.form.tablemodel.ArticlesTableModel;
import com.lavida.swing.form.MainForm;
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

    private List<ArticleJdo> articles;
    private List<String> tableHeader;
    private String userNameGmail;         // is initialised by loadGoogleAccountCredentials() method.
    private String passwordGmail;         // is initialised by loadGoogleAccountCredentials() method.

    public List<String> getTableHeader() {
        return tableHeader;
    }

    public List<ArticleJdo> getArticles() {
        return articles;
    }

    /**
     * Sets table header and data to articlesTableModel received from google spreadsheet.
     */
    public void initTableModel() {
        if (articles == null) {
            articles = loadTableData();   // get List<ArticlesJdo> from Google spreadsheet.
            articles = filterSold(articles);
        }
        if (tableHeader == null) {
            tableHeader = loadTableHeader();
        }
    }

    /**
     * Filters the List of articles and  removes all sold articles.
     *
     * @param articles the List of articles to filter.
     * @return filterd List of ArticleaJdo.
     */
    private List<ArticleJdo> filterSold(List<ArticleJdo> articles) {
        List<ArticleJdo> filteredList = new ArrayList<ArticleJdo>();
        String sold = form.getSoldMessage();
        for (ArticleJdo article : articles) {
            if (!sold.equalsIgnoreCase(article.getSold())) {
                filteredList.add(article);
            }
        }
        return filteredList;
    }

    /**
     * Loads header List for articlesTable.
     *
     * @return List of String for titles of columns
     * @throws IOException
     * @throws ServiceException
     */
    private List<String> loadTableHeader() {
        List<String> columnNamesList = new ArrayList<String>();
        if (tableHeader != null) {
            columnNamesList = tableHeader;
        }
        try {
            columnNamesList = articleService.loadTableHeader(userNameGmail, passwordGmail);
        } catch (IOException e) {
            form.showErrorMessageByException(e);
        } catch (ServiceException e) {
            form.showErrorMessageByException(e);
        }
        return columnNamesList;
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

    /**
     * The ActionListener for refreshButton component.
     * When button is clicked data for ArticleJdo List loads from Google spreadsheet
     * and saves to database, then it goes through filterSold() for deleting all sold
     * goods and renders to the articleTable.
     */
    public void refreshButtonClicked(ArticlesTableModel tableModel) {
        if (articles != null) {
            articles = null;
        } else {
            articles = loadTableData();
            saveToDatabase(articles);
            articles = filterSold(articles);
            tableModel.setTableData(articles);
        }
    }

    /**
     * Refreshes data from google spreadsheet.
     *
     * @return List of ArticleJdo loaded from google spreadsheet
     * @throws java.io.IOException
     * @throws com.google.gdata.util.ServiceException
     */
    private List<ArticleJdo> loadTableData() {
        List<ArticleJdo> articleJdoList = articles;

        try {
            articleJdoList = articleService.loadFromGoogle(userNameGmail, passwordGmail);
        } catch (IOException e) {
            form.showMessage("mainForm.exception.message.dialog.title",
                    "mainForm.exception.io.load.google.spreadsheet.table.data");
        } catch (ServiceException e) {
            form.showMessage("mainForm.exception.message.dialog.title",
                    "mainForm.exception.service.load.google.spreadsheet.table.data");
        }
        return articleJdoList;
    }

    /**
     * Saves or updates articles to the database.
     *
     * @param articles the {@code List<ArticleJdo>}  to save or update to database.
     */
    private void saveToDatabase(List<ArticleJdo> articles) {
        for (ArticleJdo articleJdo : articles) {
            try {
                articleService.update(articleJdo);
            } catch (IllegalArgumentException e) {
                form.showErrorMessageByException(e);

            } catch (TransactionRequiredException e) {
                form.showErrorMessageByException(e);
            }
        }
    }

    public void setForm(MainForm form) {
        this.form = form;
    }

    public void setArticleService(ArticleService articleService) {
        this.articleService = articleService;
    }
}
