package com.lavida.swing.form.component;

import com.lavida.service.entity.DiscountCardJdo;
import com.lavida.swing.preferences.*;
import com.lavida.swing.LocaleHolder;
import com.lavida.swing.service.DiscountCardsTableModel;
import org.springframework.context.MessageSource;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * The table component for the AllDiscountCardsDialog
 * Created: 11:48 06.09.13
 *
 * @author Ruslan
 */

public class DiscountCardTableComponent implements TableModelListener {

    private DiscountCardsTableModel tableModel;
    private MessageSource messageSource;
    private LocaleHolder localeHolder;
    private UsersSettingsHolder usersSettingsHolder;

    private JPanel mainPanel;
    private JTable discountCardsTable;
    private JScrollPane tableScrollPane;
    private DiscountCardFiltersComponent cardFiltersComponent = new DiscountCardFiltersComponent();
    private Map<String, TableColumn> headersAndColumnsMap;

    public void initializeComponents(DiscountCardsTableModel discountCardsTableModel, MessageSource messageSource,
                                     LocaleHolder localeHolder, UsersSettingsHolder usersSettingsHolder) {
        this.tableModel = discountCardsTableModel;
        this.tableModel.addTableModelListener(this);
        this.messageSource = messageSource;
        this.localeHolder = localeHolder;
        this.usersSettingsHolder = usersSettingsHolder;

        tableModel.initAnalyzeFields();

//      main panel for table of goods
        mainPanel = new JPanel();
//        mainPanel.setBackground(Color.white);
        mainPanel.setLayout(new BorderLayout());

        discountCardsTable = new JTable(tableModel);
        discountCardsTable.setCellEditor(new DefaultCellEditor(new JTextField()));
        initTableColumnsWidth();
        discountCardsTable.doLayout();
        discountCardsTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
//      Filtering the table
        discountCardsTable.setFillsViewportHeight(true);
        discountCardsTable.setRowSelectionAllowed(true);
        discountCardsTable.setCellSelectionEnabled(true); // solution for copying one cell from table
        discountCardsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        discountCardsTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting()) return;
                ListSelectionModel listSelectionModel = (ListSelectionModel) e.getSource();
                if (!listSelectionModel.isSelectionEmpty()) {
                    int viewRow = listSelectionModel.getMinSelectionIndex();
                    int selectedRow = discountCardsTable.convertRowIndexToModel(viewRow);
                    DiscountCardJdo selectedCard = tableModel.getDiscountCardByRowIndex(selectedRow);
                    tableModel.setSelectedCard(selectedCard);
                }
            }
        });

        tableScrollPane = new JScrollPane(discountCardsTable);
        tableScrollPane.setPreferredSize(new Dimension(1000, 700));
//        initTableColumnsWidth();
        mainPanel.add(tableScrollPane, BorderLayout.CENTER);

//      panel for search operations
        cardFiltersComponent.initializeComponents(tableModel, messageSource, localeHolder);
        discountCardsTable.setRowSorter(cardFiltersComponent.getSorter());
    }

    /**
     * Filters the JTable by permissions of roles (ROLE_SELLER). It removes certain columns.
     *
     * @param userRoles current user's roles.
     */
    public void filterTableByRoles(java.util.List<String> userRoles) {
        java.util.List<String> forbiddenHeaders = tableModel.getForbiddenHeadersToShow(messageSource, localeHolder.getLocale(), userRoles);
        for (String forbiddenHeader : forbiddenHeaders) {
            discountCardsTable.removeColumn(discountCardsTable.getColumn(forbiddenHeader));
        }
    }

    /**
     * Sets preferred width to certain columns
     */
    private void initTableColumnsWidth() {
        Map<String, Integer> columnHeaderToWidth = tableModel.getColumnHeaderToWidth();
        for (Map.Entry<String, Integer> entry : columnHeaderToWidth.entrySet()) {
            discountCardsTable.getColumn(entry.getKey()).setPreferredWidth(entry.getValue());
        }
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public DiscountCardFiltersComponent getCardFiltersComponent() {
        return cardFiltersComponent;
    }

    public JTable getDiscountCardsTable() {
        return discountCardsTable;
    }

    @Override
    public void tableChanged(TableModelEvent e) {
        if (e != null) {
            getCardFiltersComponent().updateAnalyzeComponent();
        }
    }

    /**
     * Initializes the headersAndColumnsMap.
     */
    public void initHeadersAndColumnsMap () {
        headersAndColumnsMap = new HashMap<>();
        Enumeration<TableColumn> columnEnumeration = discountCardsTable.getColumnModel().getColumns();
        while (columnEnumeration.hasMoreElements()) {
            TableColumn column = columnEnumeration.nextElement();
            String header = column.getHeaderValue().toString();
            headersAndColumnsMap.put(header, column);
        }
    }


    public boolean applyUserSettings(UsersSettings usersSettings, String tableSettingsName) {
        String presetName = usersSettingsHolder.getPresetName();
        String login = usersSettingsHolder.getLogin();
        UserSettings userSettings = null;
        for (UserSettings settings : usersSettings.getUserSettingsList()) {
            if (settings.getLogin().equals(login)) {
                userSettings = settings;
            }
        }
        if (userSettings == null) return false;

        PresetSettings presetSettings = null;
        for (PresetSettings settings : userSettings.getPresetSettingsList()) {
            if (presetName.equals(settings.getPresetName())) {
                presetSettings = settings;
            }
        }
        if (presetSettings == null) return false;

        TableSettings tableSettings = null;
        for (TableSettings settings : presetSettings.getTableSettings()) {
            if (tableSettingsName.equals(settings.getTableSettingsName())) {
                tableSettings = settings;
            }
        }
        if (tableSettings == null) return false;
        fixColumnOrder(tableSettings);
        fixColumnWidth(tableSettings);

        EditorsSettings editorsSettings = usersSettings.getEditorsSettings();
        if (editorsSettings == null) return false;

        TableEditorSettings tableEditorSettings = null;
        for (TableEditorSettings settings : editorsSettings.getTableEditor()) {
            if (EditorsSettings.DISCOUNT_CARDS_TABLE.equals(settings.getTableEditorSettingsName())) {
                tableEditorSettings = settings;
            }
        }
        if (tableEditorSettings == null) return false;
        fixColumnEditors(tableEditorSettings);

        DiscountCardsTableModel discountCardsTableModel = (DiscountCardsTableModel) discountCardsTable.getModel();
        discountCardsTableModel.fireTableDataChanged();
        return true;
    }

    private void fixColumnEditors(TableEditorSettings tableEditorSettings) {
        TableColumnModel tableColumnModel = discountCardsTable.getColumnModel();
        Enumeration<TableColumn> columnEnumeration = tableColumnModel.getColumns();
        java.util.List<TableColumn> columnList = tableColumnEnumerationToList(columnEnumeration);
        for (TableColumn column : columnList) {
            String columnHeader = (String) column.getHeaderValue();
            java.util.List<String> comboBoxItemList = getColumnEditorComboBoxItems(tableEditorSettings, columnHeader);
            if (comboBoxItemList != null && comboBoxItemList.size() > 0) {
                JComboBox comboBox = new JComboBox(comboBoxItemList.toArray());
                TableCellEditor tableCellEditor = new DefaultCellEditor(comboBox);
                column.setCellEditor(tableCellEditor);
            }
        }

    }

    /**
     * Finds the List of comboBox items for the column from the TableEditorSettings.
     *
     * @param tableEditorSettings the source TableEditorSettings.
     * @param columnHeader        the certain column's header.
     * @return the List of comboBox items.
     */
    private java.util.List<String> getColumnEditorComboBoxItems(TableEditorSettings tableEditorSettings, String columnHeader) {
        java.util.List<String> comboBoxItems = null;
        if (tableEditorSettings.getColumnEditors() != null) {
            for (ColumnEditorSettings settings : tableEditorSettings.getColumnEditors()) {
                if (columnHeader.equals(settings.getHeader())) {
                    comboBoxItems = settings.getComboBoxItem();
                }
            }
        }
        return comboBoxItems;
    }

    private void fixColumnWidth(TableSettings tableSettings) {
        TableColumnModel tableColumnModel = discountCardsTable.getColumnModel();
        Enumeration<TableColumn> columnEnumeration = tableColumnModel.getColumns();
        java.util.List<TableColumn> columnList = tableColumnEnumerationToList(columnEnumeration);
        for (TableColumn column : columnList) {
            String columnHeader = (String) column.getHeaderValue();
            int width = getPresetColumnWidth(tableSettings, columnHeader);
            column.setPreferredWidth(width);
        }

    }

    private void fixColumnOrder(TableSettings tableSettings) {
        TableColumnModel tableColumnModel = discountCardsTable.getColumnModel();
        Enumeration<TableColumn> columnEnumeration = tableColumnModel.getColumns();
        java.util.List<TableColumn> columnList = tableColumnEnumerationToList(columnEnumeration);
        for (TableColumn column : columnList) {
                tableColumnModel.removeColumn(column);
        }
        for (ColumnSettings columnSettings : tableSettings.getColumns()) {
            String header = columnSettings.getHeader();
            int index = columnSettings.getIndex();
            TableColumn column = headersAndColumnsMap.get(header);
            column.setModelIndex(index);
            tableColumnModel.addColumn(column);
        }
    }

    /**
     * Finds the width for the column from the TableSettings.
     *
     * @param tableSettings the source  TableSettings.
     * @param columnHeader  the certain column's header.
     * @return the width for the column from the TableSettings.
     */
    private int getPresetColumnWidth(TableSettings tableSettings, String columnHeader) {
        int width = 0;
        for (ColumnSettings columnSettings : tableSettings.getColumns()) {
            if (columnHeader.equals(columnSettings.getHeader())) {
                width = columnSettings.getWidth();
                break;
            }
        }
        return width;
    }

    /**
     * Converts the Enumeration of TableColumn  to the List of TableColumn.
     *
     * @param tableColumnEnumeration the Enumeration to be converted.
     * @return the List of TableColumn.
     */
    private List<TableColumn> tableColumnEnumerationToList(Enumeration<TableColumn> tableColumnEnumeration) {
        List<TableColumn> tableColumnList = new ArrayList<>();
        while (tableColumnEnumeration.hasMoreElements()) {
            tableColumnList.add(tableColumnEnumeration.nextElement());
        }
        return tableColumnList;
    }

    public Map<String, TableColumn> getHeadersAndColumnsMap() {
        return headersAndColumnsMap;
    }
}
