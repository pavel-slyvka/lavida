package com.lavida.swing.form.component;

import com.lavida.service.FilterColumn;
import com.lavida.service.FilterType;
import com.lavida.service.FiltersPurpose;
import com.lavida.service.ViewColumn;
import com.lavida.service.entity.*;
import com.lavida.swing.LocaleHolder;
import com.lavida.swing.service.ChangedFieldTableModel;
import org.springframework.context.MessageSource;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableRowSorter;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

/**
 * The ChangedFieldFiltersComponent
 * <p/>
 * Created: 02.10.13 11:53.
 *
 * @author Ruslan.
 */
public class ChangedFieldFiltersComponent {
    private ChangedFieldTableModel tableModel;
    //    private MessageSource messageSource;
//    private LocaleHolder localeHolder;
    private List<FilterUnit> filters;
    private JPanel filtersPanel;
    //    private JButton clearSearchButton;
    private TableRowSorter<ChangedFieldTableModel> sorter;
    //    private Map<String, String[]> comboBoxItemsMap;
    private Map<String, String[]> comboBoxItemsMap;

    public void initializeComponents(ChangedFieldTableModel aTableModel, MessageSource messageSource, LocaleHolder localeHolder) {
        this.tableModel = aTableModel;
//        this.messageSource = messageSource;
//        this.localeHolder = localeHolder;
        this.filters = new ArrayList<>();
        FilterElementsListener filterElementsListener = new FilterElementsListener();
        initializeComboBoxItemsMap();
        FiltersPurpose filtersPurpose = tableModel.getFiltersPurpose();

//      panel for search operations
        filtersPanel = new JPanel();
        filtersPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(messageSource.
                getMessage("mainForm.panel.search.title", null, localeHolder.getLocale())),
                BorderFactory.createEmptyBorder()));
        filtersPanel.setLayout(new GridBagLayout());

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(1, 5, 1, 5);

        boolean postponed = FiltersPurpose.POSTPONED_CHANGED_FIELDS == filtersPurpose;
        boolean refreshed = FiltersPurpose.REFRESHED_CHANGED_FIELDS == filtersPurpose;
        for (Field field : ChangedFieldJdo.class.getDeclaredFields()) {
            FilterColumn filterColumn = field.getAnnotation(FilterColumn.class);
            if (filterColumn != null) {
                if (postponed && filterColumn.showForPostponed() || refreshed && filterColumn.showForRefreshed()) {
                    FilterUnit filterUnit = new FilterUnit();
                    if (postponed) {
                        filterUnit.order = filterColumn.orderForPostponed();
                    } else if (refreshed) {
                        filterUnit.order = filterColumn.orderForRefreshed();
                    }
                    filterUnit.order = filterUnit.order == 0 ? Integer.MAX_VALUE : filterUnit.order;
                    filterUnit.filterType = filterColumn.type();
                    filterUnit.columnTitle = getColumnTitle(field, messageSource, localeHolder);
                    filterUnit.columnDatePattern = getColumnDatePattern(field);
                    filterUnit.label = new JLabel();
                    if (!filterColumn.labelKey().isEmpty()) {
                        filterUnit.label.setText(messageSource.getMessage(filterColumn.labelKey(), null, localeHolder.getLocale()));
                    }
                    JTextComponent textComponent = null;
                    if (FilterType.COMBOBOX == filterUnit.filterType) {
                        filterUnit.comboBox = new JComboBox<>(comboBoxItemsMap.get(field.getName()));
                        filterUnit.comboBox.setEditable(true);
                        filterUnit.comboBox.setSelectedItem(null);
                        textComponent = (JTextComponent) filterUnit.comboBox.getEditor().getEditorComponent();
                        textComponent.setDocument(new ComboBoxPlainDocumentComponent(filterUnit.comboBox));
                    } else if (FilterType.CHECKBOXES == filterUnit.filterType || FilterType.BOOLEAN_CHECKBOX == filterUnit.filterType) {
                        if (filterColumn.checkBoxesNumber() > 0) {
                            filterUnit.checkBoxes = new JCheckBox[filterColumn.checkBoxesNumber()];
                            for (int i = 0; i < filterColumn.checkBoxesNumber(); ++i) {
                                String text = messageSource.getMessage(filterColumn.checkBoxesText()[i], null, localeHolder.getLocale());
                                String actionCommand = null;
                                if (text.equals(messageSource.getMessage("dialog.changed.field.article.filter.checkBox.update", null, localeHolder.getLocale()))) {
                                    actionCommand = ChangedFieldJdo.RefreshOperationType.UPDATED.name();
                                } else if (text.equals(messageSource.getMessage("dialog.changed.field.article.filter.checkBox.save", null, localeHolder.getLocale()))) {
                                    actionCommand = ChangedFieldJdo.RefreshOperationType.SAVED.name();
                                } else if (text.equals(messageSource.getMessage("dialog.changed.field.article.filter.checkBox.delete", null, localeHolder.getLocale()))) {
                                    actionCommand = ChangedFieldJdo.RefreshOperationType.DELETED.name();
                                }
                                filterUnit.checkBoxes[i] = new JCheckBox(text);
                                filterUnit.checkBoxes[i].setActionCommand(actionCommand);
                                if (FilterType.CHECKBOXES == filterUnit.filterType) {
                                    filterUnit.checkBoxes[i].setSelected(true);
                                } else if (FilterType.BOOLEAN_CHECKBOX == filterUnit.filterType) {
                                    filterUnit.checkBoxes[i].setSelected(false);
                                }
                                filterUnit.checkBoxes[i].addItemListener(new ItemListener() {
                                    @Override
                                    public void itemStateChanged(ItemEvent e) {
                                        int state = e.getStateChange();
                                        if (state == ItemEvent.SELECTED) {
                                            applyFilters();
                                        } else if (state == ItemEvent.DESELECTED) {
                                            applyFilters();
                                        }
                                    }
                                });
                            }
                        }
                    } else {
                        filterUnit.textField = new JTextField(filterColumn.editSize());
                        textComponent = filterUnit.textField;
                    }
                    if (textComponent != null) {
                        textComponent.getDocument().addDocumentListener(filterElementsListener);
                    }

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
            if (filters.get(i).label != null) {
                if (filters.get(i).textField != null) {
                    filters.get(i).label.setLabelFor(filters.get(i).textField);
                    constraints.fill = GridBagConstraints.NONE;
                    constraints.gridwidth = GridBagConstraints.RELATIVE;
                    constraints.anchor = GridBagConstraints.EAST;
                    constraints.weightx = 0.0;
                    filtersPanel.add(filters.get(i).label, constraints);
                    constraints.fill = GridBagConstraints.HORIZONTAL;
                    constraints.gridwidth = GridBagConstraints.REMAINDER;
                    constraints.anchor = GridBagConstraints.EAST;
                    constraints.weightx = 1.0;
                    filtersPanel.add(filters.get(i).textField, constraints);
                } else if (filters.get(i).comboBox != null) {
                    filters.get(i).label.setLabelFor(filters.get(i).comboBox);
                    constraints.fill = GridBagConstraints.NONE;
                    constraints.gridwidth = GridBagConstraints.RELATIVE;
                    constraints.anchor = GridBagConstraints.EAST;
                    constraints.weightx = 0.0;
                    filtersPanel.add(filters.get(i).label, constraints);
                    constraints.fill = GridBagConstraints.HORIZONTAL;
                    constraints.gridwidth = GridBagConstraints.REMAINDER;
                    constraints.anchor = GridBagConstraints.EAST;
                    constraints.weightx = 1.0;
                    filtersPanel.add(filters.get(i).comboBox, constraints);
                } else if (filters.get(i).checkBoxes != null) {
                    JPanel checkBoxPanel = new JPanel();
                    filters.get(i).label.setLabelFor(checkBoxPanel);
                    constraints.fill = GridBagConstraints.NONE;
                    constraints.gridwidth = GridBagConstraints.RELATIVE;
                    constraints.anchor = GridBagConstraints.EAST;
                    constraints.weightx = 0.0;
                    filtersPanel.add(filters.get(i).label, constraints);

                    checkBoxPanel.setLayout(new BoxLayout(checkBoxPanel, BoxLayout.LINE_AXIS));
                    checkBoxPanel.add(Box.createHorizontalGlue());
                    for (int j = 0; j < filters.get(i).checkBoxes.length; ++j) {
                        checkBoxPanel.add(filters.get(i).checkBoxes[j]);
                        if (j == filters.get(i).checkBoxes.length - 1) break;
                        checkBoxPanel.add(Box.createHorizontalGlue());
                    }
                    constraints.fill = GridBagConstraints.HORIZONTAL;
                    constraints.gridwidth = GridBagConstraints.REMAINDER;
                    constraints.anchor = GridBagConstraints.EAST;
                    constraints.weightx = 1.0;
                    filtersPanel.add(checkBoxPanel, constraints);
                }
            }
        }

        JButton clearSearchButton = new JButton(messageSource.getMessage("mainForm.button.clear.title", null,
                localeHolder.getLocale()));
        clearSearchButton.setPreferredSize(new Dimension(500, 20));
        clearSearchButton.setMaximumSize(new Dimension(500, 20));
        clearSearchButton.setMinimumSize(new Dimension(500, 20));
        constraints.gridx = 1;
        constraints.gridy = filters.size() + 1;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.anchor = GridBagConstraints.EAST;
        constraints.weightx = 1.0;
        clearSearchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (FilterUnit filterUnit : filters) {
                    if (filterUnit.textField != null) {
                        filterUnit.textField.setText("");
                    } else if (filterUnit.checkBoxes != null) {
                        for (int j = 0; j < filterUnit.checkBoxes.length; ++j) {
                            if (FilterType.CHECKBOXES == filterUnit.filterType) {
                                filterUnit.checkBoxes[j].setSelected(true);
                            } else if (FilterType.BOOLEAN_CHECKBOX == filterUnit.filterType) {
                                filterUnit.checkBoxes[j].setSelected(false);
                            }
                        }
                    } else if (filterUnit.comboBox != null) {
                        JTextComponent textComponent = (JTextComponent) filterUnit.comboBox.getEditor().getEditorComponent();
                        textComponent.setText("");
                    }
                }
            }
        });
        filtersPanel.add(clearSearchButton, constraints);
        sorter = new TableRowSorter<>(tableModel);
    }

    private void initializeComboBoxItemsMap() {
        comboBoxItemsMap = new HashMap<>();
        List<BrandJdo> brandList = tableModel.getBrandService().getAll();
        String[] brandArray = new String[brandList.size()];
        for (int i = 0; i < brandList.size(); ++i) {
            brandArray[i] = brandList.get(i).getName();
        }
        Arrays.sort(brandArray);

        List<SizeJdo> sizeList = tableModel.getSizeService().getAll();
        String[] sizeArray = new String[sizeList.size()];
        for (int i = 0; i < sizeList.size(); ++i) {
            sizeArray[i] = sizeList.get(i).getName();
        }
        Arrays.sort(sizeArray);

        List<ShopJdo> shopList = tableModel.getShopService().getAll();
        String[] shopArray = new String[shopList.size()];
        for (int i = 0; i < shopList.size(); ++i) {
            shopArray[i] = shopList.get(i).getName();
        }
        Arrays.sort(shopArray);

        comboBoxItemsMap.put("brand", brandArray);
        comboBoxItemsMap.put("size", sizeArray);
        comboBoxItemsMap.put("shop", shopArray);


    }

    private String getColumnTitle(Field field, MessageSource messageSource, LocaleHolder localeHolder) {
        ViewColumn viewColumn = field.getAnnotation(ViewColumn.class);
        return viewColumn != null ? messageSource.getMessage(viewColumn.titleKey(), null, localeHolder.getLocale()) : field.getName();
    }

    private String getColumnDatePattern(Field field) {
        ViewColumn viewColumn = field.getAnnotation(ViewColumn.class);
        return viewColumn != null ? viewColumn.datePattern() : null;
    }

    /**
     * Filters table by name, by code, by price.
     */
    private void applyFilters() {
        List<RowFilter<ChangedFieldTableModel, Integer>> andFilters = new ArrayList<>();
        for (final FilterUnit filterUnit : filters) {
            final int columnIndex = tableModel.findColumn(filterUnit.columnTitle);

            RowFilter<ChangedFieldTableModel, Integer> filter = null;
            if (filterUnit.checkBoxes != null) {
                if (FilterType.CHECKBOXES == filterUnit.filterType) {
                    if (anyDeselected(filterUnit.checkBoxes)) {
                        filter = new RowFilter<ChangedFieldTableModel, Integer>() {
                            @Override
                            public boolean include(Entry<? extends ChangedFieldTableModel, ? extends Integer> entry) {
                                Object operationType = tableModel.getRawValueAt(entry.getIdentifier(), columnIndex);
                                return correspondsToCheckBoxes(filterUnit.checkBoxes, operationType);
                            }
                        };
                    }
                } else if (FilterType.BOOLEAN_CHECKBOX == filterUnit.filterType) {
                    if (anySelected(filterUnit.checkBoxes)) {
                        filter = new RowFilter<ChangedFieldTableModel, Integer>() {
                            @Override
                            public boolean include(Entry<? extends ChangedFieldTableModel, ? extends Integer> entry) {
                                Object obj = tableModel.getRawValueAt(entry.getIdentifier(), columnIndex);
                                return (Boolean) obj;
                            }
                        };
                    }
                }
            } else if (filterUnit.textField != null) {
                if (" ".equals(filterUnit.textField.getText())) {
                    filter = new RowFilter<ChangedFieldTableModel, Integer>() {
                        @Override
                        public boolean include(Entry<? extends ChangedFieldTableModel, ? extends Integer> entry) {
                            Object obj = tableModel.getRawValueAt(entry.getIdentifier(), columnIndex);
                            return obj == null || obj.toString().trim().isEmpty();
                        }
                    };
                } else if (FilterType.PART_TEXT == filterUnit.filterType) {
                    if (filterUnit.textField.getText().length() > 0) {
                        filter = RowFilter.regexFilter(("(?iu)" + filterUnit.textField.getText().trim()), columnIndex);
                    }
                } else if (FilterType.FULL_TEXT == filterUnit.filterType) {
                    if (filterUnit.textField.getText().length() > 0) {
                        filter = RowFilter.regexFilter(filterUnit.textField.getText().trim(), columnIndex);
                    }
                } else if (FilterType.NUMBER == filterUnit.filterType
                        || FilterType.NUMBER_DIAPASON == filterUnit.filterType && !filterUnit.textField.getText().contains("-")) {
                    if (filterUnit.textField.getText().length() > 0) {
                        String numberStr = filterUnit.textField.getText().trim().replace(",", ".").replaceAll("[^0-9.]", "");
                        Double number = Double.parseDouble(numberStr);
                        filter = RowFilter.numberFilter(RowFilter.ComparisonType.EQUAL, number, columnIndex);
                    }
                } else if (FilterType.NUMBER_DIAPASON == filterUnit.filterType) {
                    if (filterUnit.textField.getText().length() > 0) {
                        String[] numbers = filterUnit.textField.getText().split("-", 2);
                        if (numbers.length > 1 && !numbers[0].trim().isEmpty() && !numbers[1].trim().isEmpty()) {
                            String numbers0 = numbers[0].replace(",", ".").replaceAll("[^0-9.]", "");
                            String numbers1 = numbers[1].replace(",", ".").replaceAll("[^0-9.]", "");
                            final Double number1 = Double.parseDouble(numbers0);
                            final Double number2 = Double.parseDouble(numbers1);
                            filter = new RowFilter<ChangedFieldTableModel, Integer>() {
                                @Override
                                public boolean include(Entry<? extends ChangedFieldTableModel, ? extends Integer> entry) {
                                    Double number = (Double) tableModel.getRawValueAt(entry.getIdentifier(), columnIndex);
                                    return number > number1 && number < number2;
                                }
                            };
                        }
                    }
                } else if (FilterType.DATE == filterUnit.filterType
                        || FilterType.DATE_DIAPASON == filterUnit.filterType && !filterUnit.textField.getText().contains("-")) {
                    if (filterUnit.textField.getText().length() > 0) {
                        Date correctedDate = getCorrectedDate(filterUnit.textField.getText().trim());
                        String correctedDateString = new SimpleDateFormat(filterUnit.columnDatePattern).format(correctedDate);
                        filter = RowFilter.regexFilter(correctedDateString, columnIndex);
                    }
                } else if (FilterType.DATE_DIAPASON == filterUnit.filterType) {
                    if (filterUnit.textField.getText().length() > 0) {
                        String[] dates = filterUnit.textField.getText().split("-", 2);
                        if (dates.length > 1 && !dates[0].trim().isEmpty() && !dates[1].trim().isEmpty()) {
                            final Date correctedDate1 = getCorrectedDate(dates[0]);
                            final Date correctedDate2 = getCorrectedDate(dates[1]);
                            filter = new RowFilter<ChangedFieldTableModel, Integer>() {
                                @Override
                                public boolean include(Entry<? extends ChangedFieldTableModel, ? extends Integer> entry) {
                                    Object saleDateObj = tableModel.getRawValueAt(entry.getIdentifier(), columnIndex);
                                    if (saleDateObj != null) {
                                        Date date = ((Calendar) tableModel.getRawValueAt(entry.getIdentifier(), columnIndex)).getTime();
                                        return date.after(addDays(correctedDate1, -1)) && date.before(addDays(correctedDate2, 1));
                                    } else {
                                        return false;
                                    }
                                }
                            };
                        }
                    }
                }
            } else if (filterUnit.comboBox != null) {
                JTextComponent textComponent = (JTextComponent) filterUnit.comboBox.getEditor().getEditorComponent();
                if (" ".equals(textComponent.getText())) {
                    filter = new RowFilter<ChangedFieldTableModel, Integer>() {
                        @Override
                        public boolean include(Entry<? extends ChangedFieldTableModel, ? extends Integer> entry) {
                            Object obj = tableModel.getRawValueAt(entry.getIdentifier(), columnIndex);
                            return obj == null || obj.toString().trim().isEmpty();
                        }
                    };

                } else if (FilterType.COMBOBOX == filterUnit.filterType) {
                    if (textComponent.getText().length() > 0) {
                        filter = RowFilter.regexFilter(("(?iu)" + textComponent.getText().trim()), columnIndex);
                    }
                }
            }

            if (filter != null) {
                andFilters.add(filter);
            }
            sorter.setRowFilter(RowFilter.andFilter(andFilters));
        }
    }

    private boolean correspondsToCheckBoxes(JCheckBox[] checkBoxes, Object operationObject) {
        if (operationObject instanceof ChangedFieldJdo.RefreshOperationType) {
            ChangedFieldJdo.RefreshOperationType refreshOperationType = (ChangedFieldJdo.RefreshOperationType) operationObject;
            for (JCheckBox checkBox : checkBoxes) {
                if (checkBox.isSelected() && (refreshOperationType.name()).matches(checkBox.getActionCommand()))
                    return true;
            }
        }
        return false;
    }

    private boolean anyDeselected(JCheckBox[] checkBoxes) {
        for (JCheckBox checkBox : checkBoxes) {
            if (!checkBox.isSelected()) {
                return true;
            }
        }
        return false;
    }

    private boolean anySelected(JCheckBox[] checkBoxes) {
        for (JCheckBox checkBox : checkBoxes) {
            if (checkBox.isSelected()) return true;
        }
        return false;
    }

    private Date addDays(Date date, int daysCount) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, daysCount);
        return calendar.getTime();
    }

    /**
     * Returns date in the format: neededDatePattern, in case if year or month isn't entered, current year/month is put.
     *
     * @return date in the format: neededDatePattern.
     */
    private Date getCorrectedDate(String enteredDate) throws NoSuchElementException {
        enteredDate = enteredDate.replaceAll("[^0-9.,]", "");
        Queue<String> dateParts = new ArrayDeque<>(3);
        StringBuilder number = new StringBuilder();
        for (char symbol : enteredDate.toCharArray()) {
            if (Character.isDigit(symbol)) {
                number.append(symbol);
            } else if (number.length() > 0) {
                dateParts.add(number.toString());
                number = new StringBuilder();
            }
        }
        if (number.length() > 0) {
            dateParts.add(number.toString());
        }

        Calendar currentDate = Calendar.getInstance();
        switch (dateParts.size()) {
            case 0:
                dateParts.add(Integer.toString(currentDate.get(Calendar.DATE)));
            case 1:
                dateParts.add(Integer.toString(currentDate.get(Calendar.MONTH) + 1));
            case 2:
                dateParts.add(Integer.toString(currentDate.get(Calendar.YEAR)));
        }

        try {
            return new SimpleDateFormat("dd.MM.yyyy").parse(dateParts.remove() + '.' + dateParts.remove() + '.' + dateParts.remove());

        } catch (ParseException e) {
            throw new RuntimeException(e);  // todo change exception
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

    public TableRowSorter<ChangedFieldTableModel> getSorter() {
        return sorter;
    }

//    public List<FilterUnit> getFilters() {
//        return filters;
//    }

}
