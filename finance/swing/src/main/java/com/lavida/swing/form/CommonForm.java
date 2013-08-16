package com.lavida.swing.form;

import com.lavida.service.entity.ArticleJdo;
import com.lavida.swing.LocaleHolder;
import com.lavida.swing.form.tablemodel.ArticlesTableModel;
import org.springframework.context.MessageSource;

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
import java.util.*;

/**
 * Created: 15:48 16.08.13
 *
 * @author Ruslan
 */
public class CommonForm {

    @Resource
    private ArticlesTableModel tableModel;

    @Resource
    protected MessageSource messageSource;

    @Resource
    protected LocaleHolder localeHolder;

    private JLabel searchByNameLabel, searchByCodeLabel, searchByPriceLabel;
    private JTextField searchByNameField, searchByCodeField, searchByPriceField;
    private JButton clearSearchButton;
    private JTable articlesTable;
    private JScrollPane tableScrollPane;
    private TableRowSorter<ArticlesTableModel> sorter;
    private JPanel searchPanel;
    private JPanel mainPanel;


    public void intitComponents() {

        mainPanel = new JPanel();
        mainPanel.setBackground(Color.white);
        mainPanel.setLayout(new BorderLayout());

//        tableModel.initHeaderFieldAndTitles(messageSource, localeHolder.getLocale());
//        handler.initTableModelWithData(tableModel);

        articlesTable = new JTable(tableModel);
        articlesTable.doLayout();
        articlesTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
//      Filtering the table
        articlesTable.setFillsViewportHeight(true);
        articlesTable.setRowSelectionAllowed(true);
        articlesTable.setCellSelectionEnabled(true); // solution for copying one cell from table
        articlesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        articlesTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting()) return;
                ListSelectionModel listSelectionModel = (ListSelectionModel) e.getSource();
                if (!listSelectionModel.isSelectionEmpty()) {
                    int viewRow = listSelectionModel.getMinSelectionIndex();
                    int selectedRow = articlesTable.convertRowIndexToModel(viewRow);
                    ArticleJdo selectedArticle = tableModel.getArticleJdoByRowIndex(selectedRow);
                    tableModel.setSelectedArticle(selectedArticle);
                }
            }
        });

        tableScrollPane = new JScrollPane(articlesTable);
        tableScrollPane.setPreferredSize(new Dimension(1000, 700));
        packTable(articlesTable);
        mainPanel.add(tableScrollPane, BorderLayout.CENTER);

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
                allFilter();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                allFilter();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                allFilter();
            }
        });
        constraints.gridx = 1;
        constraints.gridy = 0;
        searchPanel.add(searchByNameField, constraints);

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
                allFilter();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                allFilter();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                allFilter();
            }
        });

        searchPanel.add(searchByCodeField, constraints);

        clearSearchButton = new JButton(messageSource.getMessage("mainForm.button.clear.title", null,
                localeHolder.getLocale()));
        constraints.gridx = 2;
        constraints.gridy = 1;
        clearSearchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchByCodeField.setText("");
                searchByNameField.setText("");
                searchByPriceField.setText("");
            }
        });
        searchPanel.add(clearSearchButton, constraints);

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
                allFilter();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                allFilter();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                allFilter();
            }
        });
        searchPanel.add(searchByPriceField, constraints);
    }

    /**
     * Sets preferred width to certain columns
     *
     * @param table JTable to be changed
     */
    public void packTable(JTable table) {
        table.getColumn(messageSource.getMessage("mainForm.table.articles.column.name.title", null,
                localeHolder.getLocale())).setPreferredWidth(250);
    }

    /**
     * Filters table by name, by code, by price.
     */
    private void allFilter() {
        java.util.List<RowFilter<ArticlesTableModel, Object>> andFilters = new ArrayList<RowFilter<ArticlesTableModel, Object>>();
        String name = messageSource.getMessage("mainForm.table.articles.column.name", null, localeHolder.getLocale());
        int columnNameIndex = tableModel.findColumn(name);

        RowFilter<ArticlesTableModel, Object> namesFilter = RowFilter.regexFilter(
                ("(?iu)" + searchByNameField.getText().trim()), columnNameIndex);

        String code = messageSource.getMessage("mainForm.table.articles.column.code", null, localeHolder.getLocale());  // todo change this shit
        int columnCodeIndex = tableModel.findColumn(code);
        RowFilter<ArticlesTableModel, Object> codeFilter = RowFilter.regexFilter(
                searchByCodeField.getText().trim(), columnCodeIndex);

        String price = messageSource.getMessage("mainForm.table.articles.column.sell.price.uah.title", null, localeHolder.getLocale());
        int columnPriceIndex = tableModel.findColumn(price);

        RowFilter<ArticlesTableModel, Object> priceFilter;
        if (searchByPriceField.getText().length() > 0) {
            Double number = Double.parseDouble(searchByPriceField.getText());
            priceFilter = RowFilter.numberFilter(RowFilter.ComparisonType.EQUAL, number, columnPriceIndex);
            andFilters.add(priceFilter);
        }

        andFilters.add(namesFilter);
        andFilters.add(codeFilter);

        sorter = new TableRowSorter<ArticlesTableModel>(tableModel);
        sorter.setRowFilter(RowFilter.andFilter(andFilters));
        articlesTable.setRowSorter(sorter);
    }


    public void setTableModel(ArticlesTableModel tableModel) {
        this.tableModel = tableModel;
    }

    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public void setLocaleHolder(LocaleHolder localeHolder) {
        this.localeHolder = localeHolder;
    }
}
