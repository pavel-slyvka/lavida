package com.lavida.swing.form;

import com.lavida.swing.dialog.SoldProductsDialog;
import com.lavida.swing.form.component.ArticleTableComponent;
import com.lavida.swing.handler.MainFormHandler;
import com.lavida.swing.service.ArticlesTableModel;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The main form of application.
 * Created: 20:09 03.08.13
 *
 * @author Ruslan
 */
@Component
public class MainForm extends AbstractForm {

    @Resource
    private MainFormHandler handler;

    @Resource(name = "notSoldArticleTableModel")
    private ArticlesTableModel tableModel;

    @Resource
    private SoldProductsDialog soldProductsDialog;



    private static final List<String> FORBIDDEN_ROLES = new ArrayList<String>();
    static {
        FORBIDDEN_ROLES.add("ROLE_SELLER");
    }

    private JPanel operationPanel, refreshPanel, southPanel, desktopPanel, filtersPanel, analyzePanel, mainPanel,
            buttonPanel, statusBarPanel;
    private Button refreshButton, recommitButton, sellButton, showSoldProductsButton;
    private JLabel postponedOperations, postponedMessage, errorMessage;
    private JMenuBar menuBar;
    private JMenu postponedMenu, productsMenu, settingsMenu, discountsMenu;
    private JMenuItem savePostponedItem, loadPostponedItem, deletePostponedItem,
            addNewProductsItem, columnsViewItem, addNewDiscountCardItem, allDiscountCardsItem;
    private ArticleTableComponent articleTableComponent = new ArticleTableComponent();

    @Override
    protected void initializeForm() {
        super.initializeForm();
        form.setTitle(messageSource.getMessage("mainForm.form.title", null, localeHolder.getLocale()));
        form.setResizable(true);
        form.setBounds(100, 100, 900, 700);
        form.setLocationRelativeTo(null);
    }

    @Override
    protected void initializeComponents() {
        rootContainer.setLayout(new BorderLayout());

//        menuBar
        initializeMenuBar();
        form.setJMenuBar(menuBar);

//      desktop pane
        desktopPanel = new JPanel();
//        desktopPanel.setBackground(Color.white);
        desktopPanel.setLayout(new BorderLayout());
        desktopPanel.setBorder(BorderFactory.createEmptyBorder());

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(1, 5, 1, 5);
//        constraints.fill = GridBagConstraints.HORIZONTAL;


        articleTableComponent.initializeComponents(tableModel, messageSource, localeHolder);

//      main panel for table of goods
        mainPanel = articleTableComponent.getMainPanel();
        mainPanel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
        desktopPanel.add(mainPanel, BorderLayout.CENTER);

//      analyze panel for total analyses
        analyzePanel = articleTableComponent.getArticleFiltersComponent().getArticleAnalyzeComponent().getAnalyzePanel();
        analyzePanel.setPreferredSize(new Dimension(890, 25));
        analyzePanel.setMinimumSize(new Dimension(800, 25));
        analyzePanel.setMaximumSize(new Dimension(1500, 25));

//      panel for search operations
        filtersPanel = articleTableComponent.getArticleFiltersComponent().getFiltersPanel();

        buttonPanel = new JPanel(new GridLayout(2, 1));
//      panel for refresh and save operations with data
        refreshPanel = new JPanel();
        refreshPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(messageSource.
                getMessage("mainForm.panel.refresh.title", null, localeHolder.getLocale())),
                BorderFactory.createEmptyBorder(0, 5, 0, 5)));
        refreshPanel.setLayout(new BoxLayout(refreshPanel, BoxLayout.Y_AXIS));
        refreshButton = new Button(messageSource.getMessage("mainForm.button.refresh.title", null,
                localeHolder.getLocale()));
        refreshButton.setMnemonic(KeyEvent.VK_T);  // Alt+T hot keys
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handler.refreshButtonClicked();
            }
        });
        refreshPanel.add(refreshButton);

        refreshPanel.add(Box.createVerticalStrut(5));
//        refreshPanel.add(new JSeparator(JSeparator.HORIZONTAL));
        refreshPanel.add(Box.createVerticalStrut(5));

        recommitButton = new Button(messageSource.getMessage("mainForm.button.recommit.title", null, localeHolder.getLocale()));
        recommitButton.setMnemonic(KeyEvent.VK_K); // Alt + K hot keys
        recommitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handler.recommitButtonClicked();
            }
        });
        refreshPanel.add(recommitButton);


//        operation panel for selling and returning goods.
        operationPanel = new JPanel();
        operationPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(messageSource.
                getMessage("mainForm.panel.operation.title", null, localeHolder.getLocale())),
                BorderFactory.createEmptyBorder(0, 5, 0, 5)));
        operationPanel.setLayout(new BoxLayout(operationPanel, BoxLayout.Y_AXIS));

        sellButton = new Button(messageSource.getMessage("mainForm.button.sell.title", null, localeHolder.getLocale()));
        sellButton.setMnemonic(KeyEvent.VK_S); // Alt + S hot keys
        sellButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handler.sellButtonClicked();
            }
        });
        operationPanel.add(sellButton);

        operationPanel.add(Box.createVerticalStrut(5));
//        operationPanel.add(new JSeparator(JSeparator.HORIZONTAL));
        operationPanel.add(Box.createVerticalStrut(5));

        showSoldProductsButton = new Button(messageSource.getMessage("mainForm.button.show.sold.products.title", null, localeHolder.getLocale()));
        showSoldProductsButton.setMnemonic(KeyEvent.VK_X); // Alt + X hot keys
        showSoldProductsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handler.showSoldProductsButtonClicked();
            }
        });
        operationPanel.add(showSoldProductsButton);
        buttonPanel.add(refreshPanel);
        buttonPanel.add(operationPanel);

        southPanel = new JPanel(new GridBagLayout());
        southPanel.setBorder(BorderFactory.createEmptyBorder());

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.weightx = 0.0;
        constraints.weighty = 0.0;
        southPanel.add(analyzePanel, constraints);

        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.fill = GridBagConstraints.VERTICAL;
        constraints.gridwidth = GridBagConstraints.RELATIVE;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.weightx = 0.0;
        constraints.weighty = 0.0;
        southPanel.add(buttonPanel, constraints);

        constraints.gridx = 1;
        constraints.gridy = 1;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.gridheight = GridBagConstraints.REMAINDER;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.weightx = 1.0;
        constraints.weighty = 1.0;
        southPanel.add(filtersPanel, constraints);

        desktopPanel.add(southPanel, BorderLayout.SOUTH);

        rootContainer.add(desktopPanel, BorderLayout.CENTER);

//        Status bar panel
        statusBarPanel = new JPanel();
        FlowLayout flowLayout = new FlowLayout(FlowLayout.LEADING);
        statusBarPanel.setLayout(flowLayout);
        statusBarPanel.setPreferredSize(new Dimension(790, 20));
        statusBarPanel.setMinimumSize(new Dimension(300, 20));
        statusBarPanel.setMaximumSize(new Dimension(1500, 20));
        statusBarPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(-5, 5, 1, 5),
                BorderFactory.createBevelBorder(BevelBorder.RAISED)));
        postponedOperations = new JLabel();
        postponedOperations.setHorizontalAlignment(JLabel.LEFT);
        postponedOperations.setVerticalAlignment(JLabel.TOP);
        postponedOperations.setLabelFor(postponedMessage);
        postponedOperations.setVerticalTextPosition(JLabel.TOP);
        postponedOperations.setText(messageSource.getMessage(
                "mainForm.panel.statusBar.postponed.operations.label.title", null, localeHolder.getLocale()));

        postponedMessage = new JLabel();
        postponedMessage.setVerticalTextPosition(JLabel.TOP);
        postponedMessage.setForeground(Color.RED);
        handler.showPostponedOperationsMessage();

        errorMessage = new JLabel();
        errorMessage.setForeground(Color.RED);

        JSeparator separator = new JSeparator(JSeparator.VERTICAL);
        separator.setPreferredSize(new Dimension(1, 15));
        separator.setMinimumSize(new Dimension(1, 15));
        separator.setMaximumSize(new Dimension(1, 15));

        statusBarPanel.add(postponedOperations);
        statusBarPanel.add(postponedMessage);
        statusBarPanel.add(separator);
        statusBarPanel.add(errorMessage);

        rootContainer.add(statusBarPanel, BorderLayout.SOUTH);
    }

    /**
     * Initializes the menuBar of the mainForm.
     */
    private void initializeMenuBar() {
        menuBar = new JMenuBar();

//        postponed menu
        postponedMenu = new JMenu();
        postponedMenu.setText(messageSource.getMessage("mainForm.menu.postponed.title", null, localeHolder.getLocale()));

        savePostponedItem = new JMenuItem();
        savePostponedItem.setText(messageSource.getMessage("mainForm.menu.postponed.save.title", null, localeHolder.getLocale()));
        savePostponedItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handler.savePostponedItemClicked();
            }
        });

        loadPostponedItem = new JMenuItem();
        loadPostponedItem.setText(messageSource.getMessage("mainForm.menu.postponed.load.title", null, localeHolder.getLocale()));
        loadPostponedItem.add(new JSeparator());
        loadPostponedItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handler.loadPostponedItemClicked();
            }
        });

        deletePostponedItem = new JMenuItem();
        deletePostponedItem.setText(messageSource.getMessage("mainForm.menu.postponed.delete.title", null, localeHolder.getLocale()));
        deletePostponedItem.add(new JSeparator());
        deletePostponedItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handler.deletePostponedItemClicked();
            }
        });

        postponedMenu.add(savePostponedItem);
        postponedMenu.add(loadPostponedItem);
        postponedMenu.add(deletePostponedItem);

//        products menu
        productsMenu = new JMenu();
        productsMenu.setText(messageSource.getMessage("mainForm.menu.products.title", null, localeHolder.getLocale()));

        addNewProductsItem = new JMenuItem();
        addNewProductsItem.setText(messageSource.getMessage("mainForm.menu.products.item.addNew", null, localeHolder.getLocale()));
        addNewProductsItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handler.addNewProductsItemClicked();
            }
        });
        productsMenu.add(addNewProductsItem);

//        settings menu
        settingsMenu = new JMenu();
        settingsMenu.setText(messageSource.getMessage("mainForm.menu.settings.title", null, localeHolder.getLocale()));

        columnsViewItem = new JMenuItem();
        columnsViewItem.setText(messageSource.getMessage("mainForm.menu.settings.item.view.columns", null, localeHolder.getLocale()));
        columnsViewItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handler.columnsViewItemClicked();
            }
        });
        settingsMenu.add(columnsViewItem);

//        discounts menu
        discountsMenu = new JMenu();
        discountsMenu.setText(messageSource.getMessage("mainForm.menu.discounts.title", null, localeHolder.getLocale()));

        addNewDiscountCardItem = new JMenuItem();
        addNewDiscountCardItem.setText(messageSource.getMessage("mainForm.menu.discounts.item.addNewCard", null, localeHolder.getLocale()));
        addNewDiscountCardItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handler.addNewDiscountCardItemClicked();
            }
        });
        discountsMenu.add(addNewDiscountCardItem);

        allDiscountCardsItem = new JMenuItem();
        allDiscountCardsItem.setText(messageSource.getMessage("mainForm.menu.discounts.item.allCards", null, localeHolder.getLocale()));
        allDiscountCardsItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handler.allDiscountCardsItemClicked();
            }
        });
        discountsMenu.add(allDiscountCardsItem);

        menuBar.add(postponedMenu);
        menuBar.add(productsMenu);
        menuBar.add(settingsMenu);
        menuBar.add(discountsMenu);

    }

    /**
     * Updates tableModels for sold articles and not sold articles , and their analyze components.
     */
    @Override
    public void update() {
        getTableModel().fireTableDataChanged();
        getSoldProductsDialog().getTableModel().fireTableDataChanged();
        getArticleTableComponent().getArticleFiltersComponent().updateAnalyzeComponent();
        getSoldProductsDialog().getArticleTableComponent().getArticleFiltersComponent().updateAnalyzeComponent();
        super.update();
    }

    /**
     * Filters the JTable by permissions of roles (ROLE_SELLER). It removes certain columns.
     *
     * @param userRoles
     */
    public void filterTableByRoles(List<String> userRoles) {
        articleTableComponent.filterTableByRoles(userRoles);
    }

    public void filterAnalyzePanelByRoles(List<String> userRoles) {
        articleTableComponent.getArticleFiltersComponent().getArticleAnalyzeComponent().
                filterAnalyzeComponentByRoles(userRoles);
    }

    public void filterMenuBarByRoles (List<String> userRoles) {
        if (isForbidden(userRoles, FORBIDDEN_ROLES)) {
            addNewProductsItem.setEnabled(false);
            addNewDiscountCardItem.setEnabled(false);
        }
    }

    public JLabel getPostponedMessage() {
        return postponedMessage;
    }

    public MainFormHandler getHandler() {
        return handler;
    }

    public SoldProductsDialog getSoldProductsDialog() {
        return soldProductsDialog;
    }

    public ArticleTableComponent getArticleTableComponent() {
        return articleTableComponent;
    }

    private class Button extends JButton {
        private Button() {
            super();
            setHorizontalTextPosition(JButton.CENTER);
            setPreferredSize(new Dimension(150, 20));
            setMaximumSize(new Dimension(150, 20));
            setMinimumSize(new Dimension(150, 20));

        }

        private Button(String text) {
            super(text);
            setHorizontalTextPosition(JButton.CENTER);
            setPreferredSize(new Dimension(150, 25));
            setMaximumSize(new Dimension(150, 25));
            setMinimumSize(new Dimension(150, 25));
        }
    }

    public ArticlesTableModel getTableModel() {
        return tableModel;
    }

    private boolean isForbidden(List<String> userRoles, List<String> forbiddenRoles) {
        for (String forbiddenRole : forbiddenRoles) {
            if (userRoles.contains(forbiddenRole)) {
                return true;
            }
        }
        return false;
    }

    public JLabel getErrorMessage() {
        return errorMessage;
    }
}

