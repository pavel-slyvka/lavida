package com.lavida.swing.form.component;

import com.lavida.service.FilterColumn;
import com.lavida.service.FilterType;
import com.lavida.service.FiltersPurpose;
import com.lavida.service.ViewColumn;
import com.lavida.service.entity.ArticleJdo;
import com.lavida.service.utils.CalendarConverter;
import com.lavida.service.utils.DateConverter;
import com.lavida.swing.LocaleHolder;
import com.lavida.swing.exception.LavidaSwingRuntimeException;
import com.lavida.swing.form.MainForm;
import com.lavida.swing.service.ArticlesTableModel;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.util.*;
import java.util.List;

/**
 * ArticleFiltersComponent
 * Created: 20:09 16.08.13
 *
 * @author Pavel
 */
public class ArticleFiltersComponent {
    private ArticlesTableModel tableModel;
    private List<FilterUnit> filters;
    private JPanel filtersPanel;
    private TableRowSorter<ArticlesTableModel> sorter;

    public void initializeComponents(ArticlesTableModel tableModel, MessageSource messageSource, LocaleHolder localeHolder) {
        this.tableModel = tableModel;
        this.filters = new ArrayList<FilterUnit>();
        FiltersPurpose filtersPurpose = tableModel.getFiltersPurpose();

        FilterElementsListener filterElementsListener = new FilterElementsListener();

//      panel for search operations
        filtersPanel = new JPanel();
        filtersPanel.setBackground(Color.lightGray);
        filtersPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(messageSource.
                getMessage("mainForm.panel.search.title", null, localeHolder.getLocale())),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        filtersPanel.setOpaque(true);
        filtersPanel.setAutoscrolls(true);
        filtersPanel.setLayout(new GridBagLayout());

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;

        boolean sellPurpose = FiltersPurpose.SELL_PRODUCTS == filtersPurpose;
        boolean soldPurpose = FiltersPurpose.SOLD_PRODUCTS == filtersPurpose;
        for (Field field : ArticleJdo.class.getDeclaredFields()) {
            FilterColumn filterColumn = field.getAnnotation(FilterColumn.class);
            if (filterColumn != null) {
                if (sellPurpose && filterColumn.showForSell() || soldPurpose && filterColumn.showForSold()) {
                    FilterUnit filterUnit = new FilterUnit();
                    filterUnit.order = sellPurpose ? filterColumn.orderForSell() : filterColumn.orderForSold();
                    filterUnit.order = filterUnit.order == 0 ? Integer.MAX_VALUE : filterUnit.order;
                    filterUnit.filterType = filterColumn.type();
                    filterUnit.columnTitle = getColumnTitle(field, messageSource, localeHolder);

                    if (!filterColumn.labelKey().isEmpty()) {
                        filterUnit.label = new JLabel(messageSource.getMessage(filterColumn.labelKey(), null, localeHolder.getLocale()));
                    }

                    filterUnit.textField = new JTextField(filterColumn.editSize());
                    filterUnit.textField.getDocument().addDocumentListener(filterElementsListener);
                    filters.add(filterUnit);
                }
            }
        }
        Collections.sort(filters, new Comparator<FilterUnit>() {
            @Override
            public int compare(FilterUnit filterUnit1, FilterUnit filterUnit2) {
                return filterUnit1.order - filterUnit2.order;
            }
        });
        for (int i = 0; i < filters.size(); ++i) {
            constraints.gridx = 0;
            constraints.gridy = i;
            filtersPanel.add(filters.get(i).label, constraints);
            constraints.gridx = 1;
            constraints.gridy = i;
            filtersPanel.add(filters.get(i).textField, constraints);
        }

        JButton clearSearchButton = new JButton(messageSource.getMessage("mainForm.button.clear.title", null, localeHolder.getLocale()));
        constraints.gridx = 2;
        constraints.gridy = 1;
        clearSearchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (FilterUnit filterUnit : filters) {
                    filterUnit.textField.setText("");
                }
            }
        });
        filtersPanel.add(clearSearchButton, constraints);
        sorter = new TableRowSorter<ArticlesTableModel>(tableModel);
    }

    public String getColumnTitle(Field field, MessageSource messageSource, LocaleHolder localeHolder) {
        ViewColumn viewColumn = field.getAnnotation(ViewColumn.class);
        return viewColumn != null ? messageSource.getMessage(viewColumn.titleKey(), null, localeHolder.getLocale()) : field.getName();
    }

    /**
     * Filters table by name, by code, by price.
     */
    private void applyFilters() {
        List<RowFilter<ArticlesTableModel, Object>> andFilters = new ArrayList<RowFilter<ArticlesTableModel, Object>>();
        for (FilterUnit filterUnit : filters) {
            int columnIndex = tableModel.findColumn(filterUnit.columnTitle);

            RowFilter<ArticlesTableModel, Object> filter = null;
            if (FilterType.PART_TEXT == filterUnit.filterType) {
                filter = RowFilter.regexFilter(("(?iu)" + filterUnit.textField.getText().trim()), columnIndex);
            } else if (FilterType.FULL_TEXT == filterUnit.filterType) {
                filter = RowFilter.regexFilter(filterUnit.textField.getText().trim(), columnIndex);
            } else if (FilterType.NUMBER == filterUnit.filterType) {
                if (filterUnit.textField.getText().length() > 0) {
                    Double number = Double.parseDouble(filterUnit.textField.getText());
                    filter = RowFilter.numberFilter(RowFilter.ComparisonType.EQUAL, number, columnIndex);
                }
            } else if (FilterType.DATE == filterUnit.filterType) {
                if (filterUnit.textField.getText().length() > 9) {
                    try {
                        filter = RowFilter.dateFilter(RowFilter.ComparisonType.EQUAL, CalendarConverter.convertStringToCalendar(
                                filterUnit.textField.getText().trim()).getTime(), columnIndex);
                    } catch (ParseException e) {
                        e.printStackTrace();
                        ApplicationContext context = new ClassPathXmlApplicationContext("spring-swing.xml");
                        MainForm form = context.getBean(MainForm.class);
                        form.showMessage("mainForm.exception.message.dialog.title", "exception.parse.component.filters.article.sale.date");
                    }
                }
            }
            if (filter != null) {
                andFilters.add(filter);
            }
            sorter.setRowFilter(RowFilter.andFilter(andFilters));
        }
    }

    public class FilterUnit {
        public JTextField textField;
        public JLabel label;
        public FilterType filterType;
        public String columnTitle;
        public int order;

        @Override
        public String toString() {
            return "FilterUnit{" +
                    "textField=" + textField +
                    ", label=" + label +
                    ", filterType=" + filterType +
                    ", columnTitle='" + columnTitle + '\'' +
                    ", order=" + order +
                    '}';
        }
    }

    class FilterElementsListener implements DocumentListener {
        @Override
        public void insertUpdate(DocumentEvent e) {
            applyFilters();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            applyFilters();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            applyFilters();
        }
    }

    public JPanel getFiltersPanel() {
        return filtersPanel;
    }

    public TableRowSorter<ArticlesTableModel> getSorter() {
        return sorter;
    }

    public List<FilterUnit> getFilters() {
        return filters;
    }
}
