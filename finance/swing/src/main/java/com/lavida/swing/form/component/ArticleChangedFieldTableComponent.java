package com.lavida.swing.form.component;

import com.lavida.service.entity.ArticleChangedFieldJdo;
import com.lavida.swing.LocaleHolder;
import com.lavida.swing.service.ArticleChangedFieldTableModel;
import org.springframework.context.MessageSource;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.util.*;

/**
 * The ArticleChangedFieldTableComponent
 * <p/>
 * Created: 02.10.13 11:27.
 *
 * @author Ruslan.
 */
public class ArticleChangedFieldTableComponent implements TableModelListener {

    private ArticleChangedFieldTableModel tableModel;
    private MessageSource messageSource;
    private LocaleHolder localeHolder;

    private JPanel mainPanel;
    private JTable table;
    private ArticleChangedFieldFiltersComponent articleChangedFieldFiltersComponent = new ArticleChangedFieldFiltersComponent();

    public void initializeComponents(ArticleChangedFieldTableModel aTableModel, MessageSource messageSource,
                                     LocaleHolder localeHolder) {
        this.tableModel = aTableModel;
        this.messageSource = messageSource;
        this.localeHolder = localeHolder;
//        tableModel.initAnalyzeFields();
        tableModel.addTableModelListener(this);

//      main panel for table of goods
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        table = new TableComponent();
        table.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
        table.setSurrendersFocusOnKeystroke(true);
        table.setModel(tableModel);
        JTextField textField = new JTextField();
        TableCellEditor tableCellEditor = new DefaultCellEditor(textField);
        table.setCellEditor(tableCellEditor);
        initTableColumnsEditors();
        initTableColumnsWidth();
        table.doLayout();
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
//      Filtering the table
        table.setFillsViewportHeight(true);
        table.setRowSelectionAllowed(true);
        table.setCellSelectionEnabled(true); // solution for copying one cell from table
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting()) return;
                ListSelectionModel listSelectionModel = (ListSelectionModel) e.getSource();
                if (!listSelectionModel.isSelectionEmpty()) {
                    int viewRow = listSelectionModel.getMinSelectionIndex();
                    int selectedRow = table.convertRowIndexToModel(viewRow);
                    ArticleChangedFieldJdo selectedArticleChangedField = tableModel.getArticleChangedFieldJdoByRowIndex(selectedRow);
                    tableModel.setSelectedArticleChangedField(selectedArticleChangedField);
                }
            }
        });

        JScrollPane tableScrollPane = new JScrollPane(table);
        tableScrollPane.setPreferredSize(new Dimension(1000, 700));
        mainPanel.add(tableScrollPane, BorderLayout.CENTER);

//      panel for search operations
        articleChangedFieldFiltersComponent.initializeComponents(tableModel, messageSource, localeHolder);
        table.setRowSorter(articleChangedFieldFiltersComponent.getSorter());


    }


    private void initTableColumnsEditors() {
        TableColumn selectionColumn = table.getColumn(messageSource.getMessage("component.article.table.column.selection", null, localeHolder.getLocale()));
        selectionColumn.setCellEditor(table.getDefaultEditor(Boolean.class));
        selectionColumn.setCellRenderer(table.getDefaultRenderer(Boolean.class));

/*
        sizeBox = new JComboBox(ArticleJdo.SIZE_ARRAY);
        sizeBox.setEditable(true);
        TableCellEditor sizeEditor = new DefaultCellEditor(sizeBox);
        TableColumn sizeColumn = table.getColumn(messageSource.getMessage("dialog.changed.field.article.table.size.title",
                null, localeHolder.getLocale()));
        sizeColumn.setCellEditor(sizeEditor);
*/

    }

    /**
     * Filters the JTable by permissions of roles (ROLE_SELLER). It removes certain columns.
     *
     * @param userRoles current user's roles.
     */
/*
    public void filterTableByRoles(java.util.List<String> userRoles) {
        java.util.List<String> forbiddenHeaders = tableModel.getForbiddenHeadersToShow(messageSource, localeHolder.getLocale(), userRoles);
        for (String forbiddenHeader : forbiddenHeaders) {
            table.removeColumn(table.getColumn(forbiddenHeader));
        }
    }
*/

    /**
     * Sets preferred width to certain columns
     */
    private void initTableColumnsWidth() {
        Map<String, Integer> columnHeaderToWidth = tableModel.getColumnHeaderToWidth();
        for (Map.Entry<String, Integer> entry : columnHeaderToWidth.entrySet()) {
            table.getColumn(entry.getKey()).setPreferredWidth(entry.getValue());
        }
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public ArticleChangedFieldFiltersComponent getArticleChangedFieldFiltersComponent() {
        return articleChangedFieldFiltersComponent;
    }

    public JTable getTable() {
        return table;
    }

    @Override
    public void tableChanged(TableModelEvent e) {

    }
}
