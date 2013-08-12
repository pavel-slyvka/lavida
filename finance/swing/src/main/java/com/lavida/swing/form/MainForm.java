package com.lavida.swing.form;

import com.lavida.swing.form.tablemodel.ArticlesTableModel;
import com.lavida.swing.handler.MainFormHandler;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.annotation.Resource;
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

    @Resource
    private MainFormHandler handler;

    @Resource
    private ArticlesTableModel tableModel;

    private JMenuBar jMenuBar;
    private JDesktopPane desktopPane;
    private JPanel mainPanel, operationPanel, searchPanel, refreshPanel, westPanel;
    private JLabel searchByNameLabel, searchByCodeLabel, searchByPriceLabel;
    private JTextField searchByNameField, searchByCodeField, searchByPriceField;
    private JButton clearNameButton, clearCodeButton, clearPriceButton, refreshButton, recommitButton, sellButton, returnButton;
    private JTable articlesTable;
    private JScrollPane tableScrollPane;
    private TableRowSorter<ArticlesTableModel> sorter;

    @Override
    protected void initializeForm() {
        super.initializeForm();
        form.setTitle(messageSource.getMessage("mainForm.form.title", null, localeHolder.getLocale()));
        form.setResizable(true);
        form.setBounds(100, 100, 800, 500);
        form.setLocationRelativeTo(null);
    }

    @Override
    protected void initializeComponents() {

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
                getMessage("mainForm.panel.search.title", null, localeHolder.getLocale())),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        searchPanel.setOpaque(true);
        searchPanel.setAutoscrolls(true);
        searchPanel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;

        searchByNameLabel = new JLabel(messageSource.getMessage("mainForm.label.search.by.title", null,
                localeHolder.getLocale()));
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
                localeHolder.getLocale()));
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
                localeHolder.getLocale()));
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
                localeHolder.getLocale()));
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
                localeHolder.getLocale()));
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
                localeHolder.getLocale()));
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
                getMessage("mainForm.panel.refresh.title", null, localeHolder.getLocale())),
                BorderFactory.createEmptyBorder(25, 5, 25, 5)));
        refreshPanel.setOpaque(true);
        refreshPanel.setAutoscrolls(true);
        refreshPanel.setLayout(new GridBagLayout());

        refreshButton = new JButton(messageSource.getMessage("mainForm.button.refresh.title", null,
                localeHolder.getLocale()));
        refreshButton.setMnemonic(KeyEvent.VK_T);  // Alt+T hot keys
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handler.refreshButtonClicked(tableModel);
            }
        });
        constraints.gridx = 0;
        constraints.gridy = 0;
        refreshPanel.add(refreshButton, constraints);

        recommitButton = new JButton(messageSource.getMessage("mainForm.button.recommit.title", null, localeHolder.getLocale()));
        recommitButton.setMnemonic(KeyEvent.VK_K); // Alt + K hot keys
        constraints.gridx = 0;
        constraints.gridy = 1;
        refreshPanel.add(recommitButton, constraints);

//        operation panel for selling and returning goods.
        operationPanel = new JPanel();
        operationPanel.setBackground(Color.lightGray);
        operationPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(messageSource.
                getMessage("mainForm.panel.operation.title", null, localeHolder.getLocale())),
                BorderFactory.createEmptyBorder(15, 5, 15, 5)));
        operationPanel.setOpaque(true);
        operationPanel.setAutoscrolls(true);
        operationPanel.setLayout(new GridBagLayout());

        sellButton = new JButton(messageSource.getMessage("mainForm.button.sell.title", null, localeHolder.getLocale()));
        sellButton.setMnemonic(KeyEvent.VK_S); // Alt + S hot keys
        sellButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handler.sellButtonClicked(tableModel);
            }
        });
        constraints.gridx = 0;
        constraints.gridy = 0;
        operationPanel.add(sellButton, constraints);

        returnButton = new JButton(messageSource.getMessage("mainForm.button.return.title", null, localeHolder.getLocale()));
        returnButton.setMnemonic(KeyEvent.VK_R); // Alt + R hot keys
        constraints.gridx = 0;
        constraints.gridy = 1;
        operationPanel.add(returnButton, constraints);

        westPanel = new JPanel();
        westPanel.setLayout(new GridBagLayout());
        westPanel.setBorder(BorderFactory.createEmptyBorder(25, 5, 25, 5));
        westPanel.setBackground(Color.lightGray);
        constraints.gridx = 0;
        constraints.gridy = 0;
        westPanel.add(refreshPanel, constraints);
        constraints.gridx = 0;
        constraints.gridy = 1;
        westPanel.add(operationPanel, constraints);
        desktopPane.add(westPanel, BorderLayout.WEST);

        rootContainer.add(desktopPane);
    }

    /**
     * Filters table by column "Names" according to expression.
     */
    private void filterNames() {
        String name = messageSource.getMessage("mainForm.table.articles.column.name", null, localeHolder.getLocale());
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
        String code = messageSource.getMessage("mainForm.table.articles.column.code", null, localeHolder.getLocale());
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
        String price = messageSource.getMessage("mainForm.table.articles.column.price", null, localeHolder.getLocale());
        int columnPriceIndex = tableModel.findColumn(price);

        String raisedPrice = messageSource.getMessage("mainForm.table.articles.column.price.raised", null, localeHolder.getLocale());
        int columnRaisedPriceIndex = tableModel.findColumn(raisedPrice);

        String actionPrice = messageSource.getMessage("mainForm.table.articles.column.price.action", null, localeHolder.getLocale());
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
     * Sets preferred width to certain columns
     *
     * @param table JTable to be changed
     */
    public void packTable(JTable table) {
        table.getColumn(messageSource.getMessage("mainForm.table.articles.column.name", null,
                localeHolder.getLocale())).setPreferredWidth(250);
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
                    getMessage("mainForm.table.articles.column.purchasing.price", null, localeHolder.getLocale())));
            articlesTable.removeColumn(articlesTable.getColumn(messageSource.
                    getMessage("mainForm.table.articles.column.transport.cost", null, localeHolder.getLocale())));
            articlesTable.removeColumn(articlesTable.getColumn(messageSource.
                    getMessage("mainForm.table.articles.column.sold", null, localeHolder.getLocale())));
            articlesTable.removeColumn(articlesTable.getColumn(messageSource.
                    getMessage("mainForm.table.articles.column.ours", null, localeHolder.getLocale())));
            articlesTable.removeColumn(articlesTable.getColumn(messageSource.
                    getMessage("mainForm.table.articles.column.sale.date", null, localeHolder.getLocale())));
        }
    }

    public String getSoldMessage() {
        return messageSource.getMessage("mainForm.filter.sold", null, localeHolder.getLocale());
    }


}

