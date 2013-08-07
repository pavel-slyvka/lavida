package com.lavida.swing;

import com.google.gdata.util.ServiceException;
import com.lavida.service.ArticleService;
import com.lavida.service.UserService;
import com.lavida.service.entity.ArticleJdo;
import com.lavida.service.entity.UserJdo;
import com.lavida.service.google.ArticlesFromGoogleDocUnmarshaller;
import com.lavida.service.google.MyPropertiesUtil;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.swing.*;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.*;
import java.util.List;

/**
 * MainApplicationWindow
 * Created: 20:09 03.08.13
 *
 * @author Ruslan
 */
public class MainApplicationWindow extends JFrame {
    private static final String WINDOW_NAME = "La Vida";
    private static final String CLEAR_BUTTON_NAME_RU = "Сброс";
    private static final String SEARCH_BY_NAME_RU = "Наименование:";
    private static final String SEARCH_BY_CODE_RU = "Код:";
    private static final String SEARCH_BY_PRICE_RU = "Цена:";
    private static final String SEARCH_PANEL_NAME_RU = "Поиск";
    private static final String REFRESH_PANEL_NAME_RU = "Обновления";
    private static final String REFRESH_BUTTON_NAME_RU = "Обновить";
    private static final String SOLD_RU = "Продано";

    private UserService userService;
    private ArticleService articleService;
    ArticlesTableModel tableModel;


    JMenuBar menuBar;
    JDesktopPane desktopPane;
    JPanel mainPanel, operationPanel, searchPanel, refreshPanel;
    JLabel searchByNameLabel, searchByCodeLabel, searchByPriceLabel;
    JTextField searchByNameField, searchByCodeField, searchByPriceField;
    JButton clearNameButton, clearCodeButton, clearPriceButton, refreshButton;
    JTable articlesTable;
    JScrollPane tableScrollPane;

    UserJdo currentUser;
    List<ArticleJdo> articles;
    List<String> tableHeader;

    private String userNameGmail;
    private String passwordGmail;
    private static final String filePath ="D:/Projects/LaVida/gmail.properties";

    public MainApplicationWindow() {
        super(WINDOW_NAME);

    }

    public UserJdo getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(UserJdo currentUser) {
        this.currentUser = currentUser;
    }

    /**
     * The ActionListener for refreshButton component.
     * When button is clicked data for ArticleJdo List loads from Google spreadsheet
     * and saves to database, then it goes through filterSold() for deleting all sold
     * goods and renders to the articleTable.
     *
     */
    private class RefreshButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (articles != null) {
                articles = null;
            } else {
                try {
                    articles = refreshArticles();
                    saveToDatabase(articles);
                    articles = filterSold(articles);
                    tableModel.setTableData(articles);
                } catch (ServiceException e1) {
                    e1.printStackTrace();
                    JOptionPane.showMessageDialog(desktopPane, e1.getMessage(),
                            "Exception in Google service!",JOptionPane.DEFAULT_OPTION);
                } catch (IOException e1) {
                    e1.printStackTrace();
                    JOptionPane.showMessageDialog(desktopPane, e1.getMessage(),
                            "Failed to read property file for gmail account!", JOptionPane.DEFAULT_OPTION);
                }
            }
        }
    }

    /**
     *  Saves or updates articles to the database.
     *
     * @param articles the {@code List<ArticleJdo>}  to save or update to database.
     */
    private void saveToDatabase (List<ArticleJdo> articles) {
        for (ArticleJdo articleJdo : articles) {
            articleService.update(articleJdo);
        }
    }

    /**
     * Refreshes data from google spreadsheet.
     * @return List of ArticleJdo loaded from google spreadsheet
     * @throws IOException
     * @throws ServiceException
     */
    private List<ArticleJdo> refreshArticles () throws IOException, ServiceException {
//        todo inject properties to articleFromGoogleDocUnmarshaller in context
        Properties properties = MyPropertiesUtil.loadProperties(filePath);
        userNameGmail = properties.getProperty(MyPropertiesUtil.USER_NAME);
        passwordGmail = properties.getProperty(MyPropertiesUtil.PASSWORD);
        return  articleService.loadFromGoogle(userNameGmail, passwordGmail);
    }

    private List<String> loadTableHeader () throws IOException, ServiceException {
        Properties properties = MyPropertiesUtil.loadProperties(filePath);
        userNameGmail = properties.getProperty(MyPropertiesUtil.USER_NAME);
        passwordGmail = properties.getProperty(MyPropertiesUtil.PASSWORD);
        return articleService.loadTableHeader(userNameGmail, passwordGmail);
    }

    private void initTable() {
        if (articles == null) {
            try {
                articles = refreshArticles();   // get List<ArticlesJdo> from Google spreadsheet.
                articles = filterSold(articles);
            } catch (ServiceException e1) {
                e1.printStackTrace();
                JOptionPane.showMessageDialog(desktopPane, e1.getMessage(),
                        "Exception in Google service!",JOptionPane.DEFAULT_OPTION);
            } catch (IOException e1) {
                e1.printStackTrace();
                JOptionPane.showMessageDialog(desktopPane, e1.getMessage(),
                        "Failed to read property file for gmail account!", JOptionPane.DEFAULT_OPTION);
            }
            if (tableHeader == null) {
                try {
                    tableHeader = loadTableHeader();
                } catch (ServiceException e1) {
                    e1.printStackTrace();
                    JOptionPane.showMessageDialog(desktopPane, e1.getMessage(),
                            "Exception in Google service!",JOptionPane.DEFAULT_OPTION);
                } catch (IOException e1) {
                    e1.printStackTrace();
                    JOptionPane.showMessageDialog(desktopPane, e1.getMessage(),
                            "Failed to read property file for gmail account!", JOptionPane.DEFAULT_OPTION);
                }
            }
        }

    }

    private List<ArticleJdo> filterSold(List<ArticleJdo> articles) {
        List<ArticleJdo> filteredList = new ArrayList<ArticleJdo>();
        for (ArticleJdo article : articles) {
            if (!SOLD_RU.equalsIgnoreCase(article.getSold())) {
                 filteredList.add(article);
            }
        }
        return filteredList;
    }

    public void filterByPermissions (JTable articlesTable) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        List<SimpleGrantedAuthority> authorities = (List<SimpleGrantedAuthority>) authentication.getAuthorities();
        if (authorities.contains(new SimpleGrantedAuthority("ROLE_SELLER"))) {
            articlesTable.removeColumn(articlesTable.getColumn("Закупочная цена, евро"));
            articlesTable.removeColumn(articlesTable.getColumn("Транспортные расхды, евро"));
            articlesTable.removeColumn(articlesTable.getColumn("Продано"));
            articlesTable.removeColumn(articlesTable.getColumn("Своим"));
            articlesTable.removeColumn(articlesTable.getColumn("Дата продажи"));
        }
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public void setArticleService(ArticleService articleService) {
        this.articleService = articleService;
    }

    public void setTableModel(ArticlesTableModel tableModel) {
        this.tableModel = tableModel;
    }

    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("spring-swing.xml");
        MainApplicationWindow form = context.getBean(MainApplicationWindow.class);
        form.setVisible(true);

    }

    public void packTable (JTable table) {
        table.getColumn("Наименование товара").setPreferredWidth(200);
        table.getColumn("Наименование товара").setResizable(true);
    }

    public void init() {
        setResizable(true);
        setBounds(100, 100, 800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

//      menu bar
        menuBar = new JMenuBar();
        menuBar.setBackground(Color.lightGray);
        menuBar.setPreferredSize(new Dimension(500, 25));
        setJMenuBar(menuBar);

//      desktop pane
        desktopPane = new JDesktopPane();
        desktopPane.setBackground(Color.white);
        desktopPane.setLayout(new BorderLayout());

//      main panel for table of goods
        mainPanel = new JPanel();
//        mainPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        mainPanel.setBackground(Color.white);
        mainPanel.setLayout(new BorderLayout());
        initTable();
        tableModel.setTableHeader(tableHeader);
        tableModel.setTableData(articles);
        articlesTable = new JTable(tableModel);
        articlesTable.doLayout();
        articlesTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tableScrollPane = new JScrollPane(articlesTable);
        tableScrollPane.setPreferredSize(new Dimension(1000,700));
        packTable(articlesTable);
        mainPanel.add(tableScrollPane, BorderLayout.CENTER);
        desktopPane.add(mainPanel, BorderLayout.CENTER);

//      panel for search operations
        searchPanel = new JPanel();
        searchPanel.setBackground(Color.lightGray);
        searchPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(SEARCH_PANEL_NAME_RU),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        searchPanel.setOpaque(true);
        searchPanel.setAutoscrolls(true);
        searchPanel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;

        searchByNameLabel = new JLabel(SEARCH_BY_NAME_RU);
        constraints.gridx = 0;
        constraints.gridy = 0;
        searchPanel.add(searchByNameLabel, constraints);

        searchByNameField = new JTextField(25);
        constraints.gridx = 1;
        constraints.gridy = 0;
        searchPanel.add(searchByNameField, constraints);

        clearNameButton = new JButton(CLEAR_BUTTON_NAME_RU);
        constraints.gridx = 2;
        constraints.gridy = 0;
        searchPanel.add(clearNameButton, constraints);

        searchByCodeLabel = new JLabel(SEARCH_BY_CODE_RU);
        constraints.gridx = 0;
        constraints.gridy = 1;
        searchPanel.add(searchByCodeLabel, constraints);

        searchByCodeField = new JTextField();
        constraints.gridx = 1;
        constraints.gridy = 1;
        searchPanel.add(searchByCodeField, constraints);

        clearCodeButton = new JButton(CLEAR_BUTTON_NAME_RU);
        constraints.gridx = 2;
        constraints.gridy = 1;
        searchPanel.add(clearCodeButton, constraints);

        searchByPriceLabel = new JLabel(SEARCH_BY_PRICE_RU);
        constraints.gridx = 0;
        constraints.gridy = 2;
        searchPanel.add(searchByPriceLabel, constraints);

        searchByPriceField = new JTextField();
        constraints.gridx = 1;
        constraints.gridy = 2;
        searchPanel.add(searchByPriceField, constraints);

        clearPriceButton = new JButton(CLEAR_BUTTON_NAME_RU);
        constraints.gridx = 2;
        constraints.gridy = 2;
        searchPanel.add(clearPriceButton, constraints);

        desktopPane.add(searchPanel, BorderLayout.SOUTH);

//      panel for refresh and save operations with data
        refreshPanel = new JPanel();
        refreshPanel.setBackground(Color.lightGray);
        refreshPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(REFRESH_PANEL_NAME_RU),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        refreshPanel.setOpaque(true);
        refreshPanel.setAutoscrolls(true);
        refreshPanel.setLayout(new GridBagLayout());

        refreshButton = new JButton(REFRESH_BUTTON_NAME_RU);
        refreshButton.addActionListener(new RefreshButtonActionListener());
        constraints.gridx = 0;
        constraints.gridy = 0;
        refreshPanel.add(refreshButton, constraints);

        desktopPane.add(refreshPanel, BorderLayout.WEST);

        getContentPane().add(desktopPane);
    }
}

