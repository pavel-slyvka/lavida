package com.lavida.swing.form.component;

import com.lavida.swing.LocaleHolder;
import com.lavida.swing.form.tablemodel.ArticlesTableModel;
import org.springframework.context.MessageSource;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * ArticleFiltersComponent
 * Created: 20:09 16.08.13
 *
 * @author Pavel
 */
public class ArticleFiltersComponent {
    private MessageSource messageSource;
    private LocaleHolder localeHolder;
    private ArticlesTableModel tableModel;

    private JPanel searchPanel;
    private JLabel searchByNameLabel, searchByCodeLabel, searchByPriceLabel;
    private JTextField searchByNameField, searchByCodeField, searchByPriceField;
    private JButton clearSearchButton;
    private TableRowSorter<ArticlesTableModel> sorter;

    public void initializeComponents(ArticlesTableModel tableModel, MessageSource messageSource, LocaleHolder localeHolder) {
        this.tableModel = tableModel;
        this.messageSource = messageSource;
        this.localeHolder = localeHolder;

        FilterElementsListener filterElementsListener = new FilterElementsListener();

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
        searchByNameField.getDocument().addDocumentListener(filterElementsListener);
        constraints.gridx = 1;
        constraints.gridy = 0;
        searchPanel.add(searchByNameField, constraints);

//        clearNameButton = new JButton(messageSource.getMessage("mainForm.button.clear.title", null,
//                localeHolder.getLocale()));
//        constraints.gridx = 2;
//        constraints.gridy = 0;
//        clearNameButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                searchByNameField.setText("");
//            }
//        });
//        searchPanel.add(clearNameButton, constraints);

        searchByCodeLabel = new JLabel(messageSource.getMessage("mainForm.label.search.by.code", null,
                localeHolder.getLocale()));
        constraints.gridx = 0;
        constraints.gridy = 1;
        searchPanel.add(searchByCodeLabel, constraints);

        searchByCodeField = new JTextField();
        constraints.gridx = 1;
        constraints.gridy = 1;
        searchByCodeField.getDocument().addDocumentListener(filterElementsListener);

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
        searchByPriceField.getDocument().addDocumentListener(filterElementsListener);
        searchPanel.add(searchByPriceField, constraints);

        sorter = new TableRowSorter<ArticlesTableModel>(tableModel);
    }

    class FilterElementsListener implements DocumentListener {
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

        sorter.setRowFilter(RowFilter.andFilter(andFilters));
    }

    public JPanel getSearchPanel() {
        return searchPanel;
    }

    public TableRowSorter<ArticlesTableModel> getSorter() {
        return sorter;
    }
}
