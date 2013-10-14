package com.lavida.swing.form;

import com.lavida.service.UserService;
import com.lavida.swing.dialog.*;
import com.lavida.swing.dialog.settings.AllDiscountCardsTableViewSettingsDialog;
import com.lavida.swing.dialog.settings.NotSoldArticlesTableViewSettingsDialog;
import com.lavida.swing.dialog.settings.SelectingCategoriesEditingDialog;
import com.lavida.swing.dialog.settings.SoldArticlesTableViewSettingsDialog;
import com.lavida.swing.event.PostponedOperationEvent;
import com.lavida.swing.form.component.ArticleTableComponent;
import com.lavida.swing.form.component.ProgressComponent;
import com.lavida.swing.handler.MainFormHandler;
import com.lavida.swing.preferences.PresetSettings;
import com.lavida.swing.preferences.UsersSettings;
import com.lavida.swing.preferences.UsersSettingsHolder;
import com.lavida.swing.service.ArticlesTableModel;
import com.lavida.swing.service.ConcurrentOperationsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.xml.bind.JAXBException;
import java.awt.*;
import java.awt.event.*;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.List;
import java.util.Timer;

/**
 * The main form of application.
 * Created: 20:09 03.08.13
 *
 * @author Ruslan
 */
@Component
public class MainForm extends AbstractForm implements ApplicationListener<PostponedOperationEvent> {
    private static final Logger logger = LoggerFactory.getLogger(MainFormHandler.class);

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

    @Resource
    private NotSoldArticlesTableViewSettingsDialog notSoldArticlesTableViewSettingsDialog;

    @Resource
    private SoldArticlesTableViewSettingsDialog soldArticlesTableViewSettingsDialog;

    @Resource
    private AllDiscountCardsTableViewSettingsDialog allDiscountCardsTableViewSettingsDialog;

    @Resource
    private RefreshChangesDialog refreshChangesDialog;

    @Resource
    private PostponedChangesDialog postponedChangesDialog;

    @Resource
    private SelectingCategoriesEditingDialog selectingCategoriesEditingDialog;

    @Resource
    private UserService userService;

    @Resource
    private ProgressComponent progressComponent;

    @Resource
    private UsersSettingsHolder usersSettingsHolder;

    @Resource
    private ConcurrentOperationsService concurrentOperationsService;

    private JPanel operationPanel, southPanel, desktopPanel, filtersPanel, analyzePanel, mainPanel, statusBarPanel;
    private Button sellButton, showSoldProductsButton;
    private JLabel postponedOperations, postponedMessage, errorMessage, presetNameLabel, presetNameField;
    private JMenuBar menuBar;
    private JMenu  productsMenu, settingsMenu, discountsMenu, tableMenu, selectedMenu;
    private JMenuItem addNewProductsItem, refreshTableItem, articleChangesItem,
            savePresetItem, selectPresetItem, createPresetItem, deletePresetItem, selectingCategoriesEditItem,
            notSoldArticlesTableViewItem,
            addNewDiscountCardItem, allDiscountCardsItem,
            printItem, fixTableDataItem,
            moveToShopItem, deselectArticlesItem;
    private ArticleTableComponent articleTableComponent = new ArticleTableComponent();
    private List<PopupWrapper> statusBarPopupList = new ArrayList<>();
    private PopupFactory popupFactory = PopupFactory.getSharedInstance();

    @Override
    protected void initializeForm() {
        super.initializeForm();
        form.setTitle(messageSource.getMessage("mainForm.form.title", null, localeHolder.getLocale()));
        form.setResizable(true);
        form.setBounds(100, 100, 900, 700);
        form.setLocationRelativeTo(null);
        form.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                if (concurrentOperationsService.hasActiveThreads()) {
                    int result = showConfirmDialog("mainForm.closing.warning.title", "mainForm.closing.warning.message");
                    switch (result) {
                        case JOptionPane.NO_OPTION:
                            break;

                        case JOptionPane.YES_OPTION:
                            form.dispose();
                            System.exit(0);
                    }
                } else {
                    form.dispose();
                    System.exit(0);

                }
            }
        });
    }

    @Override
    protected void initializeComponents() {
        rootContainer.setLayout(new BorderLayout());

//        menuBar
        initializeMenuBar();
        form.setJMenuBar(menuBar);

//      desktop pane
        desktopPanel = new JPanel();
        desktopPanel.setLayout(new BorderLayout());
        desktopPanel.setBorder(BorderFactory.createEmptyBorder());

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(1, 5, 1, 5);


        articleTableComponent.initializeComponents(tableModel, messageSource, localeHolder, usersSettingsHolder);

//      main panel for table of goods
        mainPanel = articleTableComponent.getMainPanel();
        mainPanel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
        desktopPanel.add(mainPanel, BorderLayout.CENTER);

//      analyze panel for total analyses
        analyzePanel = articleTableComponent.getArticleFiltersComponent().getArticleAnalyzeComponent().getAnalyzePanel();

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
                soldProductsDialog.show();
            }
        });

        operationPanel.add(Box.createHorizontalGlue());
        operationPanel.add(sellButton);
        operationPanel.add(Box.createHorizontalGlue());
        operationPanel.add(showSoldProductsButton);
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

        presetNameLabel = new JLabel();
        presetNameLabel.setText(messageSource.getMessage("mainForm.panel.statusBar.preset.name.label", null, localeHolder.getLocale()));
        presetNameLabel.setLabelFor(presetNameField);
        presetNameField = new JLabel();
        presetNameLabel.setToolTipText("Текущий пресет");

        statusBarPanel.add(presetNameLabel);
        statusBarPanel.add(presetNameField);
        statusBarPanel.add(postponedOperations);
        statusBarPanel.add(postponedMessage);
        statusBarPanel.add(errorMessage);
        statusBarPanel.add(progressComponent.getLabel());
        statusBarPanel.add(progressComponent.getProgressBar());

        rootContainer.add(statusBarPanel, BorderLayout.SOUTH);
    }

    /**
     * Initializes the menuBar of the mainForm.
     */
    private void initializeMenuBar() {
        menuBar = new JMenuBar();


//        products menu
        productsMenu = new JMenu();
        productsMenu.setText(messageSource.getMessage("mainForm.menu.products.title", null, localeHolder.getLocale()));

        refreshTableItem = new JMenuItem();
        refreshTableItem.setText(messageSource.getMessage("mainForm.menu.products.item.refresh", null, localeHolder.getLocale()));
        refreshTableItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handler.refreshTableItemClicked();
            }
        });

        articleChangesItem = new JMenuItem();
        articleChangesItem.setText(messageSource.getMessage("mainForm.menu.products.item.articleChanges", null, localeHolder.getLocale()));
        articleChangesItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshChangesDialog.show();
            }
        });

        addNewProductsItem = new JMenuItem();
        addNewProductsItem.setText(messageSource.getMessage("mainForm.menu.products.item.addNew", null, localeHolder.getLocale()));
        addNewProductsItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addNewProductsDialog.show();
            }
        });

        productsMenu.add(refreshTableItem);
        productsMenu.add(articleChangesItem);
        productsMenu.add(addNewProductsItem);

//        settings menu
        settingsMenu = new JMenu();
        settingsMenu.setText(messageSource.getMessage("mainForm.menu.settings.title", null, localeHolder.getLocale()));

        JMenuItem postponedItem = new JMenuItem();
        postponedItem.setText(messageSource.getMessage("mainForm.menu.postponed.title", null, localeHolder.getLocale()));
        postponedItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                postponedChangesDialog.show();
            }
        });

        notSoldArticlesTableViewItem = new JMenuItem();
        notSoldArticlesTableViewItem.setText(messageSource.getMessage("mainForm.menu.settings.item.view.tables", null, localeHolder.getLocale()));
        notSoldArticlesTableViewItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                notSoldArticlesTableViewSettingsDialog.show();
            }
        });

/*
        JMenu comboBoxColumnsMenu = new JMenu();
        comboBoxColumnsMenu.setText(messageSource.getMessage("mainForm.menu.comboBoxes", null, localeHolder.getLocale()));

        JMenuItem brandItem = new JMenuItem();
        brandItem.setText(messageSource.getMessage("mainForm.menu.comboBoxes.brand", null, localeHolder.getLocale()));
        brandItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handler.brandItemClicked();
            }
        });

        JMenuItem sizeItem = new JMenuItem();
        sizeItem.setText(messageSource.getMessage("mainForm.menu.comboBoxes.size", null, localeHolder.getLocale()));
        sizeItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handler.sizeItemClicked();
            }
        });

        JMenuItem shopItem = new JMenuItem();
        shopItem.setText(messageSource.getMessage("mainForm.menu.comboBoxes.shop", null, localeHolder.getLocale()));
        shopItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handler.shopItemClicked();
            }
        });


        comboBoxColumnsMenu.add(brandItem);
        comboBoxColumnsMenu.add(sizeItem);
        comboBoxColumnsMenu.add(shopItem);
*/

        selectingCategoriesEditItem = new JMenuItem();
        selectingCategoriesEditItem.setText(messageSource.getMessage("mainForm.menu.selectingCategoriesEdit", null, localeHolder.getLocale()));
        selectingCategoriesEditItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectingCategoriesEditingDialog.show();
            }
        });

        JMenu presetMenu = new JMenu();
        presetMenu.setText(messageSource.getMessage("mainForm.menu.preset", null, localeHolder.getLocale()));

        savePresetItem = new JMenuItem();
        savePresetItem.setText(messageSource.getMessage("mainForm.menu.settings.save", null, localeHolder.getLocale()));
        savePresetItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handler.savePresetItemClicked();
            }
        });

        selectPresetItem = new JMenuItem();
        selectPresetItem.setText(messageSource.getMessage("mainForm.menu.settings.select", null, localeHolder.getLocale()));
        selectPresetItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handler.selectPresetItemClicked();
            }
        });

        createPresetItem = new JMenuItem();
        createPresetItem.setText(messageSource.getMessage("mainForm.menu.settings.create", null, localeHolder.getLocale()));
        createPresetItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handler.createPresetItemClicked();
            }
        });

        deletePresetItem = new JMenuItem();
        deletePresetItem.setText(messageSource.getMessage("mainForm.menu.settings.delete", null, localeHolder.getLocale()));
        deletePresetItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handler.deletePresetItemClicked();
            }
        });

        presetMenu.add(savePresetItem);
        presetMenu.add(selectPresetItem);
        presetMenu.add(createPresetItem);
        presetMenu.add(deletePresetItem);

        settingsMenu.add(postponedItem);
        settingsMenu.add(notSoldArticlesTableViewItem);
//        settingsMenu.add(comboBoxColumnsMenu);
        settingsMenu.add(selectingCategoriesEditItem);
        settingsMenu.addSeparator();
        settingsMenu.add(presetMenu);
//        settingsMenu.add(savePresetItem);
//        settingsMenu.add(selectPresetItem);
//        settingsMenu.add(createPresetItem);
//        settingsMenu.add(deletePresetItem);

//        discounts menu
        discountsMenu = new JMenu();
        discountsMenu.setText(messageSource.getMessage("mainForm.menu.discounts.title", null, localeHolder.getLocale()));

        addNewDiscountCardItem = new JMenuItem();
        addNewDiscountCardItem.setText(messageSource.getMessage("mainForm.menu.discounts.item.addNewCard", null, localeHolder.getLocale()));
        addNewDiscountCardItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addNewDiscountCardsDialog.show();
            }
        });
        discountsMenu.add(addNewDiscountCardItem);

        allDiscountCardsItem = new JMenuItem();
        allDiscountCardsItem.setText(messageSource.getMessage("mainForm.menu.discounts.item.allCards", null, localeHolder.getLocale()));
        allDiscountCardsItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                allDiscountCardsDialog.show();
            }
        });
        discountsMenu.add(allDiscountCardsItem);

        tableMenu = new JMenu();
        tableMenu.setText(messageSource.getMessage("mainForm.menu.table", null, localeHolder.getLocale()));

        fixTableDataItem = new JMenuItem();
        fixTableDataItem.setText(messageSource.getMessage("mainForm.menu.table.fixData", null, localeHolder.getLocale()));
        fixTableDataItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handler.fixTableDataItemClicked();
            }
        });

        printItem = new JMenuItem();
        printItem.setText(messageSource.getMessage("mainForm.menu.table.print", null, localeHolder.getLocale()));
        printItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handler.printItemClicked();
            }
        });

        tableMenu.add(fixTableDataItem);
        tableMenu.add(printItem);

//        selected menu
        selectedMenu = new JMenu();
        selectedMenu.setText(messageSource.getMessage("mainForm.menu.selected", null, localeHolder.getLocale()));

        moveToShopItem = new JMenuItem();
        moveToShopItem.setText(messageSource.getMessage("mainForm.menu.selected.moveToShop", null, localeHolder.getLocale()));
        moveToShopItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handler.moveToShopItemClicked();
            }
        });

        deselectArticlesItem = new JMenuItem();
        deselectArticlesItem.setText(messageSource.getMessage("mainForm.menu.selected.deselect.articles", null, localeHolder.getLocale()));
        deselectArticlesItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handler.deselectArticlesItemClicked();
            }
        });

        selectedMenu.add(moveToShopItem);
        selectedMenu.add(deselectArticlesItem);

        menuBar.add(tableMenu);
        menuBar.add(productsMenu);
        menuBar.add(discountsMenu);
        menuBar.add(selectedMenu);
        menuBar.add(settingsMenu);
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
     */
    public void filterTableByRoles() {
        articleTableComponent.filterTableByRoles(userService);
        soldProductsDialog.getArticleTableComponent().filterTableByRoles(userService);
        addNewProductsDialog.getArticleTableComponent().filterTableByRoles(userService);

    }

    public void filterSellDialogByRoles(List<String> userRoles) {
        sellDialog.filterByRoles(userRoles);
    }

    public void filterAnalyzePanelByRoles() {
        articleTableComponent.getArticleFiltersComponent().getArticleAnalyzeComponent().
                filterAnalyzeComponentByRoles(userService);
        soldProductsDialog.getArticleTableComponent().getArticleFiltersComponent().getArticleAnalyzeComponent().
                filterAnalyzeComponentByRoles(userService);
        addNewProductsDialog.getArticleTableComponent().getArticleFiltersComponent().getArticleAnalyzeComponent().
                filterAnalyzeComponentByRoles(userService);
        allDiscountCardsDialog.getCardTableComponent().getCardFiltersComponent().getCardAnalyzeComponent().
                filterAnalyzeComponentByRoles(userService);
        addNewDiscountCardsDialog.getCardTableComponent().getCardFiltersComponent().getCardAnalyzeComponent().
                filterAnalyzeComponentByRoles(userService);
    }

    public void removeFiltersByRoles() {
        articleTableComponent.getArticleFiltersComponent().removeFiltersByRoles(userService);
        soldProductsDialog.getArticleTableComponent().getArticleFiltersComponent().removeFiltersByRoles(userService);
    }

    public void filterTableDataByRole(List<String> userRoles) {
        getTableModel().filterTableDataByRole(userRoles);
        soldProductsDialog.getTableModel().filterTableDataByRole(userRoles);
    }

    public void filterMenuBarByRoles() {
        if (userService.hasForbiddenRole()) {
            moveToShopItem.setEnabled(false);
            printItem.setEnabled(false);
            articleChangesItem.setEnabled(false);
            soldProductsDialog.getPrintItem().setEnabled(false);
            addNewProductsDialog.getPrintItem().setEnabled(false);
            addNewDiscountCardsDialog.getPrintItem().setEnabled(false);
            allDiscountCardsDialog.getPrintItem().setEnabled(false);
        }
    }

    public void updateBrandEditor() {
        articleTableComponent.updateBrandColumnEditor();
        soldProductsDialog.getArticleTableComponent().updateBrandColumnEditor();
        addNewProductsDialog.getArticleTableComponent().updateBrandColumnEditor();
    }

    public void updateSizeEditor() {
        articleTableComponent.updateSizeColumnEditor();
        soldProductsDialog.getArticleTableComponent().updateSizeColumnEditor();
        addNewProductsDialog.getArticleTableComponent().updateSizeColumnEditor();
    }

    public void updateShopEditor() {
        articleTableComponent.updateShopColumnEditor();
        soldProductsDialog.getArticleTableComponent().updateShopColumnEditor();
        addNewProductsDialog.getArticleTableComponent().updateShopColumnEditor();
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

    /**
     * Sets all tables to the {@link com.lavida.swing.preferences.UsersSettingsHolder}.
     */
    public void holdAllTables() {
        JTable notSoldArticlesTable = getArticleTableComponent().getArticlesTable();
        usersSettingsHolder.setNotSoldArticlesTable(notSoldArticlesTable);
        JTable soldArticlesTable = soldProductsDialog.getArticleTableComponent().getArticlesTable();
        usersSettingsHolder.setSoldArticlesTable(soldArticlesTable);
        JTable addNewArticlesTable = addNewProductsDialog.getArticleTableComponent().getArticlesTable();
        usersSettingsHolder.setAddNewArticlesTable(addNewArticlesTable);
        JTable allDiscountCardsTable = allDiscountCardsDialog.getCardTableComponent().getDiscountCardsTable();
        usersSettingsHolder.setAllDiscountCardsTable(allDiscountCardsTable);
        JTable addNewDiscountCardsTable = addNewDiscountCardsDialog.getCardTableComponent().getDiscountCardsTable();
        usersSettingsHolder.setAddNewDiscountCardsTable(addNewDiscountCardsTable);
    }

    public void createDefaultPreset() {
        handler.createDefaultPreset();
    }

    public void updatePresetNameField() {
        presetNameField.setText(usersSettingsHolder.getPresetName());
    }

    @Override
    public void onApplicationEvent(PostponedOperationEvent event) {
        handler.showPostponedOperationsMessage();
    }

    /**
     * The custom button.
     */
    private class Button extends JButton {
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

    public void setRefreshTableItemEnable(boolean isVisible) {
        refreshTableItem.setEnabled(isVisible);
    }

    public void initializeUserSettings() {
        UsersSettings usersSettings = null;
        try {
            usersSettings = userSettingsService.getSettings();
        } catch (JAXBException | FileNotFoundException e) {
            Toolkit.getDefaultToolkit().beep();
            logger.error(e.getMessage(), e);
            showWarningMessage("mainForm.exception.message.dialog.title", "mainForm.handler.load.usersSettings.error.message");

        }
        getArticleTableComponent().applyUserSettings(usersSettings, PresetSettings.NOT_SOLD_ARTICLES_TABLE);
        notSoldArticlesTableViewSettingsDialog.getTableViewComponent().updateListModels();
        soldProductsDialog.getArticleTableComponent().applyUserSettings(usersSettings, PresetSettings.SOLD_ARTICLES_TABLE);
        soldArticlesTableViewSettingsDialog.getTableViewComponent().updateListModels();
        addNewProductsDialog.getArticleTableComponent().applyUserSettings(usersSettings, PresetSettings.ADD_NEW_ARTICLES_TABLE);
        allDiscountCardsDialog.getCardTableComponent().applyUserSettings(usersSettings, PresetSettings.ALL_DISCOUNT_CARDS_TABLE);
        allDiscountCardsTableViewSettingsDialog.getTableViewComponent().updateListModels();
        addNewDiscountCardsDialog.getCardTableComponent().applyUserSettings(usersSettings, PresetSettings.ADD_NEW_DISCOUNT_CARDS_TABLE);

    }

    public void initializeTableViewComponents() {
        getArticleTableComponent().initHeadersAndColumnsMap();
        soldProductsDialog.getArticleTableComponent().initHeadersAndColumnsMap();
        addNewProductsDialog.getArticleTableComponent().initHeadersAndColumnsMap();
        allDiscountCardsDialog.getCardTableComponent().initHeadersAndColumnsMap();
        addNewDiscountCardsDialog.getCardTableComponent().initHeadersAndColumnsMap();

        notSoldArticlesTableViewSettingsDialog.postInit();
        soldArticlesTableViewSettingsDialog.postInit();
        allDiscountCardsTableViewSettingsDialog.postInit();
    }

    public void showInfoToolTip(final String message) {
        if (message.replaceFirst("<html>", "").isEmpty()) {
            return;
        }

        JToolTip jtoolTip = statusBarPanel.createToolTip();
        jtoolTip.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

            }
        });
        jtoolTip.setTipText(message);
        jtoolTip.setBackground(Color.ORANGE);
        jtoolTip.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        int tipWidth = jtoolTip.getUI().getPreferredSize(jtoolTip).width;
        int tipHeight = jtoolTip.getUI().getPreferredSize(jtoolTip).height;
        int xLocation = getForm().getLocation().x + getForm().getWidth() - tipWidth;
        int yLocation = getForm().getLocation().y + getForm().getHeight() - tipHeight;

        final Popup popup = popupFactory.getPopup(statusBarPanel, jtoolTip, xLocation, yLocation);
        final PopupWrapper popupWrapper = new PopupWrapper(popup, jtoolTip, new Point(xLocation, yLocation),
                new Dimension(tipWidth, tipHeight), System.currentTimeMillis());
        final java.util.Timer timer = new Timer(true);
        statusBarPopupList.add(popupWrapper);
        sortStatusBarPopupList(statusBarPopupList);
        jtoolTip.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                popupWrapper.popup.hide();
                timer.cancel();
                statusBarPopupList.remove(popupWrapper);
                sortStatusBarPopupList(statusBarPopupList);
            }
        });
        popupWrapper.popup.show();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
               popupWrapper.popup.hide();
                timer.cancel();
            }
        }, 30000);
    }

    private void sortStatusBarPopupList(List<PopupWrapper> statusBarPopupList) {
        int lastXLeft = getForm().getLocation().x + getForm().getWidth();
        int lastYDown = getForm().getLocation().y + getForm().getHeight();
        Collections.sort(statusBarPopupList);
        for (PopupWrapper popupWrapper : statusBarPopupList) {
            int xLocation = lastXLeft - popupWrapper.dimension.width;
            int yLocation = lastYDown - popupWrapper.dimension.height;
            popupWrapper.popup.hide();
            popupWrapper.popup = popupFactory.getPopup(statusBarPanel, popupWrapper.jToolTip, xLocation, yLocation);
            lastYDown = yLocation;
            if ((System.currentTimeMillis() - popupWrapper.startTime) < 29500) popupWrapper.popup.show();
        }

    }



    private class PopupWrapper implements Comparable<PopupWrapper> {
        Popup popup;
        JToolTip jToolTip;
        Point location;
        Dimension dimension;
        long startTime;

        private PopupWrapper(Popup popup, JToolTip jToolTip, Point location, Dimension dimension, long startTime) {
            this.popup = popup;
            this.jToolTip = jToolTip;
            this.location = location;
            this.dimension = dimension;
            this.startTime = startTime;
        }

        @Override
        public int compareTo(PopupWrapper o) {
            return this.location.y - o.location.y;
        }
    }
}

