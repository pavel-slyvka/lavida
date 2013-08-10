package com.lavida.swing.form;

import com.google.gdata.util.ServiceException;
import com.lavida.swing.form.tablemodel.ArticlesTableModel;
import com.lavida.swing.handler.MainFormHandler;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.annotation.Resource;
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
import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * MainForm
 * Created: 20:09 03.08.13
 *
 * @author Ruslan
 */
@org.springframework.stereotype.Component
public class MainForm extends AbstractForm {
    //    private static final String CLEAR_BUTTON_NAME_RU = "Сброс";
//    private static final String SEARCH_BY_NAME_RU = "Наименование:";
//    private static final String SEARCH_BY_CODE_RU = "Код:";
//    private String SEARCH_PANEL_NAME_RU = messageSource.getMessage("mainForm.panel.search.name", null, currentLocale);
//    private static final String REFRESH_PANEL_NAME_RU = "Обновления";
//    private static final String REFRESH_BUTTON_NAME_RU = "Обновить";
//    private static final String SOLD_RU = "Продано";
//    private static final String SEARCH_BY_PRICE_RU = "Цена:";
//    private  String SOLD_RU = messageSource.getMessage("mainForm.filter.sold", null, currentLocale);
    private static final String WINDOW_NAME = "La Vida";

    @Resource
    private MainFormHandler handler;

    @Resource
    private ArticlesTableModel tableModel;

    private JMenuBar jMenuBar;
    private JDesktopPane desktopPane;
    private JPanel mainPanel, operationPanel, searchPanel, refreshPanel;
    private JLabel searchByNameLabel, searchByCodeLabel, searchByPriceLabel;
    private JTextField searchByNameField, searchByCodeField, searchByPriceField;
    private JButton clearNameButton, clearCodeButton, clearPriceButton, refreshButton;
    private JTable articlesTable;
    private JScrollPane tableScrollPane;
    private TableRowSorter<ArticlesTableModel> sorter;

    @Override
    protected void initializeForm() {
        super.initializeForm();
        form.setTitle(WINDOW_NAME);
        form.setResizable(true);
        form.setBounds(100, 100, 800, 500);
        form.setLocationRelativeTo(null);
    }

    @Override
    protected void initializeComponents() {
        handler.loadGoogleAccountCredentials();

//      menu bar
        jMenuBar = new JMenuBar();
        jMenuBar.setBackground(Color.lightGray);
        jMenuBar.setPreferredSize(new Dimension(500, 25));
        form.setJMenuBar(jMenuBar);

//      desktop pane
        desktopPane = new JDesktopPane();
        desktopPane.setBackground(Color.white);
        desktopPane.setLayout(new BorderLayout());

//      main panel for table of goods
        mainPanel = new JPanel();
        mainPanel.setBackground(Color.white);
        mainPanel.setLayout(new BorderLayout());

        handler.initTableModel(tableModel);

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
                getMessage("mainForm.panel.search.title", null, locale)),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        searchPanel.setOpaque(true);
        searchPanel.setAutoscrolls(true);
        searchPanel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;

        searchByNameLabel = new JLabel(messageSource.getMessage("mainForm.label.search.by.title", null,
                locale));
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

        clearNameButton = new JButton(messageSource.getMessage("mainForm.button.clear.title", null,
                locale));
        constraints.gridx = 2;
        constraints.gridy = 0;
        clearNameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchByNameField.setText("");
            }
        });
        searchPanel.add(clearNameButton, constraints);

        searchByCodeLabel = new JLabel(messageSource.getMessage("mainForm.label.search.by.code", null,
                locale));
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
        });

        searchPanel.add(searchByCodeField, constraints);

        clearCodeButton = new JButton(messageSource.getMessage("mainForm.button.clear.title", null,
                locale));
        constraints.gridx = 2;
        constraints.gridy = 1;
        clearCodeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchByCodeField.setText("");
            }
        });
        searchPanel.add(clearCodeButton, constraints);

        searchByPriceLabel = new JLabel(messageSource.getMessage("mainForm.label.search.by.price", null,
                locale));
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

        clearPriceButton = new JButton(messageSource.getMessage("mainForm.button.clear.title", null,
                locale));
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
                getMessage("mainForm.panel.refresh.title", null, locale)),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        refreshPanel.setOpaque(true);
        refreshPanel.setAutoscrolls(true);
        refreshPanel.setLayout(new GridBagLayout());

        refreshButton = new JButton(messageSource.getMessage("mainForm.button.refresh.title", null,
                locale));
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handler.refreshButtonClicked(tableModel);
            }
        });
        constraints.gridx = 0;
        constraints.gridy = 0;
        refreshPanel.add(refreshButton, constraints);

        desktopPane.add(refreshPanel, BorderLayout.WEST);

        rootContainer.add(desktopPane);
    }

    /**
     * Filters table by column "Names" according to expression.
     */
    private void filterNames() {
        String name = messageSource.getMessage("mainForm.table.articles.column.name", null, locale);
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
        String code = messageSource.getMessage("mainForm.table.articles.column.code", null, locale);
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
        String price = messageSource.getMessage("mainForm.table.articles.column.price", null, locale);
        int columnPriceIndex = tableModel.findColumn(price);

        String raisedPrice = messageSource.getMessage("mainForm.table.articles.column.price.raised", null, locale);
        int columnRaisedPriceIndex = tableModel.findColumn(raisedPrice);

        String actionPrice = messageSource.getMessage("mainForm.table.articles.column.price.action", null, locale);
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

    public void showErrorMessageByException(Exception e) {
        if (IllegalArgumentException.class == e.getClass()) {   // todo don't need
            showMessage("mainForm.exception.message.dialog.title",
                    "mainForm.exception.illegal.argument.save.to.database");

        } else if (TransactionRequiredException.class == e.getClass()) {    // todo don't need
            showMessage("mainForm.exception.message.dialog.title",
                    "mainForm.exception.transaction.required.save.to.database");

        } else if (IOException.class == e.getClass()) {
            showMessage("mainForm.exception.message.dialog.title",
                    "mainForm.exception.io.load.google.spreadsheet.table.data");

        } else if (ServiceException.class == e.getClass()) {
            showMessage("mainForm.exception.message.dialog.title",
                    "mainForm.exception.service.load.google.spreadsheet.table.data");
        }
    }

    /**
     * Sets preferred width to certain columns
     *
     * @param table JTable to be changed
     */
    public void packTable(JTable table) {
        table.getColumn(messageSource.getMessage("mainForm.table.articles.column.name", null,
                locale)).setPreferredWidth(250);
//        table.getColumn(messageSource.getMessage("mainForm.table.articles.column.name", null,
//        currentLocale)).setResizable(true);
    }

    public void filterByPermissionsStub() { // todo changeme!!!
        filterByPermissions(articlesTable);
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
                    getMessage("mainForm.table.articles.column.purchasing.price", null, locale)));
            articlesTable.removeColumn(articlesTable.getColumn(messageSource.
                    getMessage("mainForm.table.articles.column.transport.cost", null, locale)));
            articlesTable.removeColumn(articlesTable.getColumn(messageSource.
                    getMessage("mainForm.table.articles.column.sold", null, locale)));
            articlesTable.removeColumn(articlesTable.getColumn(messageSource.
                    getMessage("mainForm.table.articles.column.ours", null, locale)));
            articlesTable.removeColumn(articlesTable.getColumn(messageSource.
                    getMessage("mainForm.table.articles.column.sale.date", null, locale)));
        }
    }

    public String getSoldMessage() {
        return messageSource.getMessage("mainForm.filter.sold", null, locale);
    }
}
