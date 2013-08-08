package com.lavida.swing;

import com.google.gdata.util.ServiceException;
import com.lavida.service.ArticleService;
import com.lavida.service.entity.ArticleJdo;
import com.lavida.service.utils.MyPropertiesUtil;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.persistence.TransactionRequiredException;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.*;
import java.util.List;

/**
 * MainApplicationWindow
 * Created: 20:09 03.08.13
 *
 * @author Ruslan
 */
public class MainApplicationWindow extends JFrame implements MessageSourceAware {
    //    private static final String CLEAR_BUTTON_NAME_RU = "Сброс";
//    private static final String SEARCH_BY_NAME_RU = "Наименование:";
//    private static final String SEARCH_BY_CODE_RU = "Код:";
//    private String SEARCH_PANEL_NAME_RU = messageSource.getMessage("mainApplicationWindow.panel.search.name", null, currentLocale);
//    private static final String REFRESH_PANEL_NAME_RU = "Обновления";
//    private static final String REFRESH_BUTTON_NAME_RU = "Обновить";
//    private static final String SOLD_RU = "Продано";
//    private static final String SEARCH_BY_PRICE_RU = "Цена:";
//    private  String SOLD_RU = messageSource.getMessage("mainApplicationWindow.filter.sold", null, currentLocale);

    private ArticleService articleService;
    private MessageSource messageSource;
    private ArticlesTableModel tableModel;

    private static final String WINDOW_NAME = "La Vida";
    private static final String filePath = "gmail.properties";  // file path for google account properties

    private String userNameGmail;         // is initialised by loadGoogleAccountCredentials() method.
    private String passwordGmail;         // is initialised by loadGoogleAccountCredentials() method.
    private Locale currentLocale = new Locale.Builder().setLanguage("ru").setRegion("RU").setScript("Cyrl").build();

    private JMenuBar jMenuBar;
    private JDesktopPane desktopPane;
    private JPanel mainPanel, operationPanel, searchPanel, refreshPanel;
    private JLabel searchByNameLabel, searchByCodeLabel, searchByPriceLabel;
    private JTextField searchByNameField, searchByCodeField, searchByPriceField;
    private JButton clearNameButton, clearCodeButton, clearPriceButton, refreshButton;
    private JTable articlesTable;
    private JScrollPane tableScrollPane;
    private List<ArticleJdo> articles;
    private List<String> tableHeader;
    private TableRowSorter<ArticlesTableModel> sorter;

    public MainApplicationWindow() {
        super(WINDOW_NAME);

    }

    /**
     * The init-method for bean mainApplicationWindow
     */
    public void init() {

        loadGoogleAccountCredentials();
        setResizable(true);
        setBounds(100, 100, 800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

//      menu bar
        jMenuBar = new JMenuBar();
        jMenuBar.setBackground(Color.lightGray);
        jMenuBar.setPreferredSize(new Dimension(500, 25));
        setJMenuBar(jMenuBar);

//      desktop pane
        desktopPane = new JDesktopPane();
        desktopPane.setBackground(Color.white);
        desktopPane.setLayout(new BorderLayout());

//      main panel for table of goods
        mainPanel = new JPanel();
        mainPanel.setBackground(Color.white);
        mainPanel.setLayout(new BorderLayout());
        initTableModel();

        articlesTable = new JTable(tableModel);
        articlesTable.doLayout();
        articlesTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
//      Filtering the table
        sorter = new TableRowSorter<ArticlesTableModel>(tableModel);
        articlesTable.setRowSorter(sorter);
        articlesTable.setFillsViewportHeight(true);
        articlesTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        articlesTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                int viewRow = articlesTable.getSelectedRow();
                if (viewRow >= 0) {
                    int modelRow = articlesTable.convertRowIndexToModel(viewRow);
                }
            }
        });

        tableScrollPane = new JScrollPane(articlesTable);
        tableScrollPane.setPreferredSize(new Dimension(1000, 700));
        packTable(articlesTable);
        mainPanel.add(tableScrollPane, BorderLayout.CENTER);
        desktopPane.add(mainPanel, BorderLayout.CENTER);

//      panel for search operations
        searchPanel = new JPanel();
        searchPanel.setBackground(Color.lightGray);
        searchPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(messageSource.
                getMessage("mainApplicationWindow.panel.search.title", null, currentLocale)),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        searchPanel.setOpaque(true);
        searchPanel.setAutoscrolls(true);
        searchPanel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;

        searchByNameLabel = new JLabel(messageSource.getMessage("mainApplicationWindow.label.search.by.title", null,
                currentLocale));
        constraints.gridx = 0;
        constraints.gridy = 0;
        searchPanel.add(searchByNameLabel, constraints);

        searchByNameField = new JTextField(25);
//      Filtering the articlesTable
        searchByNameField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                filterNames();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                filterNames();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                filterNames();
            }
        });
        constraints.gridx = 1;
        constraints.gridy = 0;
        searchPanel.add(searchByNameField, constraints);

        clearNameButton = new JButton(messageSource.getMessage("mainApplicationWindow.button.clear.title", null,
                currentLocale));
        constraints.gridx = 2;
        constraints.gridy = 0;
        clearNameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchByNameField.setText("");
            }
        });
        searchPanel.add(clearNameButton, constraints);

        searchByCodeLabel = new JLabel(messageSource.getMessage("mainApplicationWindow.label.search.by.code", null,
                currentLocale));
        constraints.gridx = 0;
        constraints.gridy = 1;
        searchPanel.add(searchByCodeLabel, constraints);

        searchByCodeField = new JTextField();
        constraints.gridx = 1;
        constraints.gridy = 1;
        searchByCodeField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                filterCodes();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                filterCodes();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                filterCodes();
            }
        }) ;

        searchPanel.add(searchByCodeField, constraints);

        clearCodeButton = new JButton(messageSource.getMessage("mainApplicationWindow.button.clear.title", null,
                currentLocale));
        constraints.gridx = 2;
        constraints.gridy = 1;
        clearCodeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchByCodeField.setText("");
            }
        });
        searchPanel.add(clearCodeButton, constraints);

        searchByPriceLabel = new JLabel(messageSource.getMessage("mainApplicationWindow.label.search.by.price", null,
                currentLocale));
        constraints.gridx = 0;
        constraints.gridy = 2;
        searchPanel.add(searchByPriceLabel, constraints);

        searchByPriceField = new JTextField();
        constraints.gridx = 1;
        constraints.gridy = 2;
        searchByPriceField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                filterPrices();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                filterPrices();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                filterPrices();
            }
        });
        searchPanel.add(searchByPriceField, constraints);

        clearPriceButton = new JButton(messageSource.getMessage("mainApplicationWindow.button.clear.title", null,
                currentLocale));
        constraints.gridx = 2;
        constraints.gridy = 2;
        clearPriceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchByPriceField.setText("");
            }
        });
        searchPanel.add(clearPriceButton, constraints);

        desktopPane.add(searchPanel, BorderLayout.SOUTH);

//      panel for refresh and save operations with data
        refreshPanel = new JPanel();
        refreshPanel.setBackground(Color.lightGray);
        refreshPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(messageSource.
                getMessage("mainApplicationWindow.panel.refresh.title", null, currentLocale)),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        refreshPanel.setOpaque(true);
        refreshPanel.setAutoscrolls(true);
        refreshPanel.setLayout(new GridBagLayout());

        refreshButton = new JButton(messageSource.getMessage("mainApplicationWindow.button.refresh.title", null,
                currentLocale));
        refreshButton.addActionListener(new RefreshButtonActionListener());
        constraints.gridx = 0;
        constraints.gridy = 0;
        refreshPanel.add(refreshButton, constraints);

        desktopPane.add(refreshPanel, BorderLayout.WEST);

        getContentPane().add(desktopPane);
    }

    /**
     * Filters table by column "Names" according to expression.
     */
    private void filterNames() {
        String name = messageSource.getMessage("mainApplicationWindow.table.articles.column.name", null, currentLocale);
        int columnNameIndex = tableModel.findColumn(name);

        RowFilter<ArticlesTableModel, Object> rf = null;
        //If current expression doesn't parse, don't update.
        try {
            rf = RowFilter.regexFilter(searchByNameField.getText().trim(), columnNameIndex);
        } catch (java.util.regex.PatternSyntaxException e) {
            return;
        }
        sorter.setRowFilter(rf);
    }

    /**
     * Filters table by column "Code" according to expression.
     */
    private void filterCodes() {
        String code = messageSource.getMessage("mainApplicationWindow.table.articles.column.code", null, currentLocale);
        int columnCodeIndex = tableModel.findColumn(code);
        RowFilter<ArticlesTableModel, Object> rf = null;
        //If current expression doesn't parse, don't update.
        try {
            rf = RowFilter.regexFilter(searchByCodeField.getText().trim(), columnCodeIndex);
        } catch (java.util.regex.PatternSyntaxException e) {
            return;
        }
        sorter.setRowFilter(rf);
    }

    /**
     * Filters table by columns "*Price" according to expression.
     */
    private void filterPrices() {
        String price = messageSource.getMessage("mainApplicationWindow.table.articles.column.price", null, currentLocale);
        int columnPriceIndex = tableModel.findColumn(price);

        String raisedPrice = messageSource.getMessage("mainApplicationWindow.table.articles.column.price.raised", null, currentLocale);
        int columnRaisedPriceIndex = tableModel.findColumn(raisedPrice);

        String actionPrice = messageSource.getMessage("mainApplicationWindow.table.articles.column.price.action", null, currentLocale);
        int columnActionPrice = tableModel.findColumn(actionPrice);
        RowFilter<ArticlesTableModel, Object> rf = null;
        //If current expression doesn't parse, don't update.
        try {
            rf = RowFilter.regexFilter(searchByPriceField.getText().trim(), columnPriceIndex, columnRaisedPriceIndex, columnActionPrice);
        } catch (java.util.regex.PatternSyntaxException e) {
            return;
        }
        sorter.setRowFilter(rf);
    }

    /**
     * Loads properties
     */
    private void loadGoogleAccountCredentials() {
        Properties properties = null;
        try {
            properties = MyPropertiesUtil.loadProperties(filePath);
            this.userNameGmail = properties.getProperty(MyPropertiesUtil.USER_NAME);
            this.passwordGmail = properties.getProperty(MyPropertiesUtil.PASSWORD);
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            JOptionPane.showMessageDialog(desktopPane, messageSource.
                    getMessage("mainApplicationWindow.exception.io.properties.load.google.account", null, currentLocale),
                    messageSource.getMessage("mainApplicationWindow.exception.message.dialog.title", null, currentLocale),
                    JOptionPane.DEFAULT_OPTION);
        }

    }

    /**
     * The ActionListener for refreshButton component.
     * When button is clicked data for ArticleJdo List loads from Google spreadsheet
     * and saves to database, then it goes through filterSold() for deleting all sold
     * goods and renders to the articleTable.
     */
    private class RefreshButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (articles != null) {
                articles = null;
            } else {
                articles = loadTableData();
                saveToDatabase(articles);
                articles = filterSold(articles);
                tableModel.setTableData(articles);
            }
        }
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
                JOptionPane.showMessageDialog(desktopPane, messageSource.
                        getMessage("mainApplicationWindow.exception.illegal.argument.save.to.database", null, currentLocale),
                        messageSource.getMessage("mainApplicationWindow.exception.message.dialog.title", null, currentLocale),
                        JOptionPane.DEFAULT_OPTION);

            } catch (TransactionRequiredException e) {
                JOptionPane.showMessageDialog(desktopPane, messageSource.
                        getMessage("mainApplicationWindow.exception.transaction.required.save.to.database", null, currentLocale),
                        messageSource.getMessage("mainApplicationWindow.exception.message.dialog.title", null, currentLocale),
                        JOptionPane.DEFAULT_OPTION);

            }
        }
    }

    /**
     * Refreshes data from google spreadsheet.
     *
     * @return List of ArticleJdo loaded from google spreadsheet
     * @throws IOException
     * @throws ServiceException
     */
    private List<ArticleJdo> loadTableData() {
        List<ArticleJdo> articleJdoList = articles;

        try {
            articleJdoList = articleService.loadFromGoogle(userNameGmail, passwordGmail);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(desktopPane, messageSource.
                    getMessage("mainApplicationWindow.exception.io.load.google.spreadsheet.table.data", null, currentLocale),
                    messageSource.getMessage("mainApplicationWindow.exception.message.dialog.title", null, currentLocale),
                    JOptionPane.DEFAULT_OPTION);
        } catch (ServiceException e) {
            JOptionPane.showMessageDialog(desktopPane, messageSource.
                    getMessage("mainApplicationWindow.exception.service.load.google.spreadsheet.table.data", null, currentLocale),
                    messageSource.getMessage("mainApplicationWindow.exception.message.dialog.title", null, currentLocale),
                    JOptionPane.DEFAULT_OPTION);
        }
        return articleJdoList;
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
            JOptionPane.showMessageDialog(desktopPane, messageSource.
                    getMessage("mainApplicationWindow.exception.io.load.google.spreadsheet.table.data", null, currentLocale),
                    messageSource.getMessage("mainApplicationWindow.exception.message.dialog.title", null, currentLocale),
                    JOptionPane.DEFAULT_OPTION);
        } catch (ServiceException e) {
            JOptionPane.showMessageDialog(desktopPane, messageSource.
                    getMessage("mainApplicationWindow.exception.service.load.google.spreadsheet.table.data", null, currentLocale),
                    messageSource.getMessage("mainApplicationWindow.exception.message.dialog.title", null, currentLocale),
                    JOptionPane.DEFAULT_OPTION);
        }
        return columnNamesList;
    }

    /**
     * Sets table header and data to articlesTableModel received from google spreadsheet.
     */
    private void initTableModel() {
        if (articles == null) {
            articles = loadTableData();   // get List<ArticlesJdo> from Google spreadsheet.
            articles = filterSold(articles);
        }
        if (tableHeader == null) {
            tableHeader = loadTableHeader();
        }
        tableModel.setTableHeader(tableHeader);
        tableModel.setTableData(articles);
    }

    /**
     * Sets preferred width to certain columns
     *
     * @param table JTable to be changed
     */
    public void packTable(JTable table) {
        table.getColumn(messageSource.getMessage("mainApplicationWindow.table.articles.column.name", null,
                currentLocale)).setPreferredWidth(250);
//        table.getColumn(messageSource.getMessage("mainApplicationWindow.table.articles.column.name", null,
//        currentLocale)).setResizable(true);
    }

    /**
     * Filters the List of articles and  removes all sold articles.
     *
     * @param articles the List of articles to filter.
     * @return filterd List of ArticleaJdo.
     */
    private List<ArticleJdo> filterSold(List<ArticleJdo> articles) {
        List<ArticleJdo> filteredList = new ArrayList<ArticleJdo>();
        String sold = messageSource.getMessage("mainApplicationWindow.filter.sold", null, currentLocale);
        for (ArticleJdo article : articles) {
            if (!sold.equalsIgnoreCase(article.getSold())) {
                filteredList.add(article);
            }
        }
        return filteredList;
    }

    /**
     * Filters the JTable by permissions of roles (ROLE_SELLER). It removes certain columns.
     *
     * @param articlesTable the JTable to be filtered
     */
    public void filterByPermissions(JTable articlesTable) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        List<SimpleGrantedAuthority> authorities = (List<SimpleGrantedAuthority>) authentication.getAuthorities();
        String seller = messageSource.getMessage("lavida.authority.seller", null, Locale.US);
        if (authorities.contains(new SimpleGrantedAuthority(seller))) {
            articlesTable.removeColumn(articlesTable.getColumn(messageSource.
                    getMessage("mainApplicationWindow.table.articles.column.purchasing.price", null, currentLocale)));
            articlesTable.removeColumn(articlesTable.getColumn(messageSource.
                    getMessage("mainApplicationWindow.table.articles.column.transport.cost", null, currentLocale)));
            articlesTable.removeColumn(articlesTable.getColumn(messageSource.
                    getMessage("mainApplicationWindow.table.articles.column.sold", null, currentLocale)));
            articlesTable.removeColumn(articlesTable.getColumn(messageSource.
                    getMessage("mainApplicationWindow.table.articles.column.ours", null, currentLocale)));
            articlesTable.removeColumn(articlesTable.getColumn(messageSource.
                    getMessage("mainApplicationWindow.table.articles.column.sale.date", null, currentLocale)));
        }
    }


    public void setArticleService(ArticleService articleService) {
        this.articleService = articleService;
    }

    public void setTableModel(ArticlesTableModel tableModel) {
        this.tableModel = tableModel;
    }

    @Override
    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public JMenuBar getjMenuBar() {
        return jMenuBar;
    }

    public JDesktopPane getDesktopPane() {
        return desktopPane;
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public JPanel getOperationPanel() {
        return operationPanel;
    }

    public JPanel getSearchPanel() {
        return searchPanel;
    }

    public JPanel getRefreshPanel() {
        return refreshPanel;
    }

    public JLabel getSearchByNameLabel() {
        return searchByNameLabel;
    }

    public JLabel getSearchByCodeLabel() {
        return searchByCodeLabel;
    }

    public JLabel getSearchByPriceLabel() {
        return searchByPriceLabel;
    }

    public JTextField getSearchByNameField() {
        return searchByNameField;
    }

    public JTextField getSearchByCodeField() {
        return searchByCodeField;
    }

    public JTextField getSearchByPriceField() {
        return searchByPriceField;
    }

    public JButton getClearNameButton() {
        return clearNameButton;
    }

    public JButton getClearCodeButton() {
        return clearCodeButton;
    }

    public JButton getClearPriceButton() {
        return clearPriceButton;
    }

    public JButton getRefreshButton() {
        return refreshButton;
    }

    public JTable getArticlesTable() {
        return articlesTable;
    }

    public JScrollPane getTableScrollPane() {
        return tableScrollPane;
    }

    public List<ArticleJdo> getArticles() {
        return articles;
    }

    public List<String> getTableHeader() {
        return tableHeader;
    }
}

