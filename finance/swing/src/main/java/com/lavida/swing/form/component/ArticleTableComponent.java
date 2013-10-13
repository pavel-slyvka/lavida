package com.lavida.swing.form.component;

import com.lavida.service.UserService;
import com.lavida.service.entity.ArticleJdo;
import com.lavida.service.entity.BrandJdo;
import com.lavida.swing.preferences.*;
import com.lavida.swing.LocaleHolder;
import com.lavida.swing.service.ArticlesTableModel;
import org.springframework.context.MessageSource;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.text.JTextComponent;
import javax.swing.text.PlainDocument;
import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * ArticleTableComponent
 * Created: 20:09 16.08.13
 *
 * @author Pavel
 */
public class ArticleTableComponent implements TableModelListener{
    private ArticlesTableModel tableModel;
    private MessageSource messageSource;
    private LocaleHolder localeHolder;
    private UsersSettingsHolder usersSettingsHolder;

    private JPanel mainPanel;
    private JTable articlesTable;
    private ArticleFiltersComponent articleFiltersComponent = new ArticleFiltersComponent();
    private Map<String, TableColumn> headersAndColumnsMap;

    public void initializeComponents(ArticlesTableModel articlesTableModel, MessageSource messageSource,
                                     LocaleHolder localeHolder, UsersSettingsHolder usersSettingsHolder) {
        this.tableModel = articlesTableModel;
        this.messageSource = messageSource;
        this.localeHolder = localeHolder;
        this.usersSettingsHolder = usersSettingsHolder;
        tableModel.initAnalyzeFields();
        tableModel.addTableModelListener(this);

//      main panel for table of goods
        mainPanel = new JPanel();
//        mainPanel.setBackground(Color.white);
        mainPanel.setLayout(new BorderLayout());

        articlesTable = new TableComponent();
        articlesTable.putClientProperty( "terminateEditOnFocusLost", Boolean.TRUE );
        articlesTable.setSurrendersFocusOnKeystroke(true);
        articlesTable.setModel(tableModel);
        JTextField textField = new JTextField();
        TableCellEditor tableCellEditor = new DefaultCellEditor(textField);
        articlesTable.setCellEditor(tableCellEditor);
        initTableColumnsEditors();
        initTableColumnsWidth();
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

        JScrollPane tableScrollPane = new JScrollPane(articlesTable);
        tableScrollPane.setPreferredSize(new Dimension(1000, 700));
        mainPanel.add(tableScrollPane, BorderLayout.CENTER);

//      panel for search operations
        articleFiltersComponent.initializeComponents(tableModel, messageSource, localeHolder);
        articlesTable.setRowSorter(articleFiltersComponent.getSorter());


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

    public void initTableColumnsEditors() {
        TableColumn selectionColumn = articlesTable.getColumn(messageSource.getMessage("component.article.table.column.selection", null, localeHolder.getLocale()));
        selectionColumn.setCellEditor(articlesTable.getDefaultEditor(Boolean.class));
        selectionColumn.setCellRenderer(articlesTable.getDefaultRenderer(Boolean.class));

//        JComboBox brandBox = new JComboBox<>(ArticleJdo.BRAND_ARRAY);
//        brandBox.setEditable(true);
//        TableCellEditor brandEditor = new DefaultCellEditor(brandBox);
//        TableColumn brandColumn = articlesTable.getColumn(messageSource.getMessage("mainForm.table.articles.column.brand.title", null, localeHolder.getLocale()));
//        brandColumn.setCellEditor(brandEditor);
        updateBrandColumnEditor();

        JComboBox sizeBox = new JComboBox<>(ArticleJdo.SIZE_ARRAY);
        sizeBox.setEditable(true);
        TableCellEditor sizeEditor = new DefaultCellEditor(sizeBox);
        TableColumn sizeColumn = articlesTable.getColumn(messageSource.getMessage("mainForm.table.articles.column.size.title",
                null, localeHolder.getLocale()));
        sizeColumn.setCellEditor(sizeEditor);

        JComboBox shopBox = new JComboBox<>(ArticleJdo.SHOP_ARRAY);
        shopBox.setEditable(true);
        TableCellEditor shopEditor = new DefaultCellEditor(shopBox);
        TableColumn shopColumn = articlesTable.getColumn(messageSource.getMessage("mainForm.table.articles.column.shop.title",
                null, localeHolder.getLocale()));
        shopColumn.setCellEditor(shopEditor);
    }

    /**
     * Filters the JTable by permissions of roles (ROLE_SELLER). It removes certain columns.
     *
     * @param userService current userService..
     */
    public void filterTableByRoles(UserService userService) {
        java.util.List<String> forbiddenHeaders = tableModel.getForbiddenHeadersToShow(messageSource, localeHolder.getLocale(), userService.getCurrentUserRoles());
        for (String forbiddenHeader : forbiddenHeaders) {
            articlesTable.removeColumn(articlesTable.getColumn(forbiddenHeader));
        }
        if (tableModel.getQueryName() == null && userService.hasForbiddenRole()) {
            articlesTable.removeColumn(articlesTable.getColumn(messageSource.
                    getMessage("mainForm.table.articles.column.sell.price.uah.title", null, localeHolder.getLocale())));
        }
    }

    /**
     * Sets preferred width to certain columns
     */
    private void initTableColumnsWidth() {
        Map<String, Integer> columnHeaderToWidth = tableModel.getColumnHeaderToWidth();
        for (Map.Entry<String, Integer> entry : columnHeaderToWidth.entrySet()) {
            articlesTable.getColumn(entry.getKey()).setPreferredWidth(entry.getValue());
//            articlesTable.getColumn(entry.getKey()).setMinWidth(entry.getValue());
        }
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public ArticleFiltersComponent getArticleFiltersComponent() {
        return articleFiltersComponent;
    }

    public JTable getArticlesTable() {
        return articlesTable;
    }

    @Override
    public void tableChanged(TableModelEvent e) {
        if (e != null) {
            getArticleFiltersComponent().updateAnalyzeComponent();
        }

    }


    /**
     * Initializes the headersAndColumnsMap.
     */
    public void initHeadersAndColumnsMap() {
        headersAndColumnsMap = new HashMap<>();
        Enumeration<TableColumn> columnEnumeration = articlesTable.getColumnModel().getColumns();
        while (columnEnumeration.hasMoreElements()) {
            TableColumn column = columnEnumeration.nextElement();
            String header = column.getHeaderValue().toString();
            headersAndColumnsMap.put(header, column);
        }
    }

    /**
     * Applies settings for the current user.
     *
     * @return true if the user has default settings.
     */
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
            if (EditorsSettings.ARTICLES_TABLE.equals(settings.getTableEditorSettingsName())) {
                tableEditorSettings = settings;
            }
        }
        if (tableEditorSettings == null) return false;
        fixColumnEditors(tableEditorSettings);

        ArticlesTableModel articlesTableModel = (ArticlesTableModel) articlesTable.getModel();
        articlesTableModel.fireTableDataChanged();
        return true;
    }

    private void fixColumnEditors(TableEditorSettings tableEditorSettings) {
        TableColumnModel tableColumnModel = articlesTable.getColumnModel();
        Enumeration<TableColumn> columnEnumeration = tableColumnModel.getColumns();
        List<TableColumn> columnList = tableColumnEnumerationToList(columnEnumeration);
        for (TableColumn column : columnList) {
            String columnHeader = (String) column.getHeaderValue();
            List<String> comboBoxItemList = getColumnEditorComboBoxItems(tableEditorSettings, columnHeader);
            if (comboBoxItemList != null && comboBoxItemList.size() > 0) {
                JComboBox comboBox = new JComboBox<>(comboBoxItemList.toArray());
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
    private List<String> getColumnEditorComboBoxItems(TableEditorSettings tableEditorSettings, String columnHeader) {
        List<String> comboBoxItems = null;
        if (tableEditorSettings.getColumnEditors() != null ) {
            for (ColumnEditorSettings settings : tableEditorSettings.getColumnEditors()) {
                if (columnHeader.equals(settings.getHeader())) {
                    comboBoxItems = settings.getComboBoxItem();
                }
            }
        }

        return comboBoxItems;
    }

    private void fixColumnWidth(TableSettings tableSettings) {
        TableColumnModel tableColumnModel = articlesTable.getColumnModel();
        Enumeration<TableColumn> columnEnumeration = tableColumnModel.getColumns();
        List<TableColumn> columnList = tableColumnEnumerationToList(columnEnumeration);
        for (TableColumn column : columnList) {
            String columnHeader = (String) column.getHeaderValue();
            int width = getPresetColumnWidth(tableSettings, columnHeader);
            column.setPreferredWidth(width);
        }
    }

    private void fixColumnOrder(TableSettings tableSettings) {
        TableColumnModel tableColumnModel = articlesTable.getColumnModel();
        Enumeration<TableColumn> columnEnumeration = tableColumnModel.getColumns();
        List<TableColumn> columnList = tableColumnEnumerationToList(columnEnumeration);
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

    public Map<String, TableColumn> getHeadersAndColumnsMap() {
        return headersAndColumnsMap;
    }


    public void updateBrandColumnEditor() {
        List<BrandJdo> brandJdoList = (tableModel.getBrandService()).getAll();
        String[] brandArray = new String[brandJdoList.size()];
        for (int i = 0; i < brandJdoList.size(); ++i) {
            brandArray[i] = brandJdoList.get(i).getName();
        }
        Collections.sort(Arrays.asList(brandArray));
        JComboBox brandBox = new JComboBox<>(brandArray);
        JTextComponent textComponent = (JTextComponent)brandBox.getEditor().getEditorComponent();
        brandBox.setEditable(true);
        textComponent.setDocument(new ComboBoxPlainDocumentComponent(brandBox));
        TableCellEditor brandEditor = new DefaultCellEditor(brandBox);
        TableColumn brandColumn = articlesTable.getColumn(messageSource.getMessage("mainForm.table.articles.column.brand.title", null, localeHolder.getLocale()));
        brandColumn.setCellEditor(brandEditor);

    }
}
