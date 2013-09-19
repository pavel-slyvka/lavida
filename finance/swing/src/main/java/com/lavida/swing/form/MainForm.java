package com.lavida.swing.form;

import com.lavida.swing.dialog.*;
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

    @Resource
    private SellDialog sellDialog;

    @Resource
    private AddNewProductsDialog addNewProductsDialog;

    @Resource
    private AllDiscountCardsDialog allDiscountCardsDialog;

    @Resource
    private AddNewDiscountCardsDialog addNewDiscountCardsDialog;

    private static final List<String> FORBIDDEN_ROLES = new ArrayList<String>();
    static {
        FORBIDDEN_ROLES.add("ROLE_SELLER_LA_VIDA");
        FORBIDDEN_ROLES.add("ROLE_SELLER_SLAVYANKA");
        FORBIDDEN_ROLES.add("ROLE_SELLER_NOVOMOSKOVSK");
    }

    private JPanel operationPanel, southPanel, desktopPanel, filtersPanel, analyzePanel, mainPanel, statusBarPanel;
    private Button refreshButton, sellButton, showSoldProductsButton;
    private JLabel postponedOperations, postponedMessage, errorMessage;
    private JMenuBar menuBar;
    private JMenu postponedMenu, productsMenu, settingsMenu, discountsMenu, fileMenu;
    private JMenuItem savePostponedItem, loadPostponedItem, recommitPostponedItem, deletePostponedItem,
            addNewProductsItem,
            articleColumnsViewItem, saveSettingsItem,
            addNewDiscountCardItem, allDiscountCardsItem,
            printItem;
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
//        analyzePanel.setPreferredSize(new Dimension(300, 200));
//        analyzePanel.setMinimumSize(new Dimension(300, 200));
//        analyzePanel.setMaximumSize(new Dimension(1000, 1000));

//      panel for search operations
        filtersPanel = articleTableComponent.getArticleFiltersComponent().getFiltersPanel();

//        operation panel for selling and returning goods.
        operationPanel = new JPanel();
        operationPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(messageSource.
                getMessage("mainForm.panel.operation.title", null, localeHolder.getLocale())),
                BorderFactory.createEmptyBorder(0, 5, 0, 5)));
        operationPanel.setLayout(new BoxLayout(operationPanel, BoxLayout.LINE_AXIS));

        sellButton = new Button(messageSource.getMessage("mainForm.button.sell.title", null, localeHolder.getLocale()));
        sellButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handler.sellButtonClicked();
            }
        });

        showSoldProductsButton = new Button(messageSource.getMessage("mainForm.button.show.sold.products.title", null, localeHolder.getLocale()));
        showSoldProductsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handler.showSoldProductsButtonClicked();
            }
        });

        refreshButton = new Button(messageSource.getMessage("mainForm.button.refresh.title", null,
                localeHolder.getLocale()));
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handler.refreshButtonClicked();
            }
        });

        operationPanel.add(Box.createHorizontalGlue());
        operationPanel.add(sellButton);
        operationPanel.add(Box.createHorizontalGlue());
        operationPanel.add(showSoldProductsButton);
        operationPanel.add(Box.createHorizontalGlue());
        operationPanel.add(refreshButton);
        operationPanel.add(Box.createHorizontalGlue());

        southPanel = new JPanel(new GridBagLayout());
        southPanel.setBorder(BorderFactory.createEmptyBorder());
        constraints.insets = new Insets(0, 5, 0, 5);

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.fill = GridBagConstraints.VERTICAL;
        constraints.gridwidth = GridBagConstraints.RELATIVE;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.weightx = 0.0;
        constraints.weighty = 1.0;
        southPanel.add(analyzePanel, constraints);

        constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.gridheight = GridBagConstraints.RELATIVE;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.weightx = 1.0;
        constraints.weighty = 1.0;
        southPanel.add(filtersPanel, constraints);

        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.weightx = 1.0;
        constraints.weighty = 0.0;
        southPanel.add(operationPanel, constraints);

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
        postponedOperations.setForeground(Color.RED);
        postponedOperations.setVisible(false);

        postponedMessage = new JLabel();
        postponedMessage.setVerticalTextPosition(JLabel.TOP);
        postponedMessage.setForeground(Color.RED);
        handler.showPostponedOperationsMessage();

        errorMessage = new JLabel();
        errorMessage.setForeground(Color.RED);


        statusBarPanel.add(postponedOperations);
        statusBarPanel.add(postponedMessage);
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
        loadPostponedItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handler.loadPostponedItemClicked();
            }
        });

        recommitPostponedItem = new JMenuItem();
        recommitPostponedItem.setText(messageSource.getMessage("mainForm.button.recommit.title", null, localeHolder.getLocale()));
        recommitPostponedItem.add(new JSeparator());
        recommitPostponedItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handler.recommitPostponedItemClicked();
            }
        });

        deletePostponedItem = new JMenuItem();
        deletePostponedItem.setText(messageSource.getMessage("mainForm.menu.postponed.delete.title", null, localeHolder.getLocale()));
        deletePostponedItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handler.deletePostponedItemClicked();
            }
        });

        postponedMenu.add(savePostponedItem);
        postponedMenu.add(loadPostponedItem);
        postponedMenu.add(recommitPostponedItem);
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

        articleColumnsViewItem = new JMenuItem();
        articleColumnsViewItem.setText(messageSource.getMessage("mainForm.menu.settings.item.view.columns", null, localeHolder.getLocale()));
        articleColumnsViewItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handler.articleColumnsViewItemClicked();
            }
        });

        saveSettingsItem = new JMenuItem();
        saveSettingsItem.setText(messageSource.getMessage("mainForm.menu.settings.save", null, localeHolder.getLocale()));
        saveSettingsItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handler.saveSettingsItemClicked();
            }
        });

        settingsMenu.add(articleColumnsViewItem);
        settingsMenu.add(saveSettingsItem);

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

        fileMenu = new JMenu();
        fileMenu.setText(messageSource.getMessage("mainForm.menu.table", null, localeHolder.getLocale()));

        printItem = new JMenuItem();
        printItem.setText(messageSource.getMessage("mainForm.menu.table.print", null, localeHolder.getLocale()));
        printItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handler.printItemClicked();
            }
        });

        fileMenu.add(printItem);


        menuBar.add(fileMenu);
        menuBar.add(productsMenu);
        menuBar.add(discountsMenu);
        menuBar.add(settingsMenu);
        menuBar.add(postponedMenu);

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
        soldProductsDialog.filterTableByRoles(userRoles);

    }

    public void initializeSellDialogByUser (List<String> userRoles) {
             sellDialog.initializeByUser(userRoles);
    }

    public void initializeArticleTableColumnLists () {
        handler.getColumnsViewSettingsDialog().initializeLists(
                getArticleTableComponent().getArticlesTable(),
                soldProductsDialog.getArticleTableComponent().getArticlesTable());
    }

    public void filterAnalyzePanelByRoles(List<String> userRoles) {
        articleTableComponent.getArticleFiltersComponent().getArticleAnalyzeComponent().
                filterAnalyzeComponentByRoles(userRoles);
        soldProductsDialog.getArticleTableComponent().getArticleFiltersComponent().getArticleAnalyzeComponent().
                filterAnalyzeComponentByRoles(userRoles);
        allDiscountCardsDialog.getCardTableComponent().getCardFiltersComponent().getCardAnalyzeComponent().
                filterAnalyzeComponentByRoles(userRoles);
        addNewDiscountCardsDialog.getCardTableComponent().getCardFiltersComponent().getCardAnalyzeComponent().
                filterAnalyzeComponentByRoles(userRoles);
    }

    public void removeFiltersByRoles (List<String> userRoles) {
        articleTableComponent.getArticleFiltersComponent().removeFiltersByRoles(userRoles);
        soldProductsDialog.getArticleTableComponent().getArticleFiltersComponent().removeFiltersByRoles(userRoles);
    }

    public void filterTableDataByRole (List<String> userRoles) {
        getTableModel().filterTableDataByRole(userRoles);
        soldProductsDialog.getTableModel().filterTableDataByRole(userRoles);
    }

    public void filterMenuBarByRoles (List<String> userRoles) {
        if (isForbidden(userRoles, FORBIDDEN_ROLES)) {
            addNewProductsItem.setEnabled(false);
            printItem.setEnabled(false);
            soldProductsDialog.getPrintItem().setEnabled(false);
            addNewProductsDialog.getPrintItem().setEnabled(false);
            addNewDiscountCardsDialog.getPrintItem().setEnabled(false);
            allDiscountCardsDialog.getPrintItem().setEnabled(false);
        }
    }

    public JLabel getPostponedOperations() {
        return postponedOperations;
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

    public JMenuItem getSavePostponedItem() {
        return savePostponedItem;
    }

    public JMenuItem getRecommitPostponedItem() {
        return recommitPostponedItem;
    }

    public JMenuItem getDeletePostponedItem() {
        return deletePostponedItem;
    }
}

