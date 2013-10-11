package com.lavida.swing.form.component;

import com.lavida.service.entity.ChangedFieldJdo;
import com.lavida.swing.LocaleHolder;
import com.lavida.swing.service.ChangedFieldTableModel;
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
 * The ChangedFieldTableComponent
 * <p/>
 * Created: 02.10.13 11:27.
 *
 * @author Ruslan.
 */
public class ChangedFieldTableComponent implements TableModelListener {

    private ChangedFieldTableModel tableModel;
    private MessageSource messageSource;
    private LocaleHolder localeHolder;

    private JPanel mainPanel;
    private JTable table;
    private ChangedFieldFiltersComponent changedFieldFiltersComponent = new ChangedFieldFiltersComponent();

    public void initializeComponents(ChangedFieldTableModel aTableModel, MessageSource messageSource,
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
                    ChangedFieldJdo selectedArticleChangedField = tableModel.getChangedFieldJdoByRowIndex(selectedRow);
                    tableModel.setSelectedChangedField(selectedArticleChangedField);
                }
            }
        });

        JScrollPane tableScrollPane = new JScrollPane(table);
        tableScrollPane.setPreferredSize(new Dimension(1000, 700));
        mainPanel.add(tableScrollPane, BorderLayout.CENTER);

//      panel for search operations
        changedFieldFiltersComponent.initializeComponents(tableModel, messageSource, localeHolder);
        table.setRowSorter(changedFieldFiltersComponent.getSorter());


    }


    private void initTableColumnsEditors() {
        TableColumn selectionColumn = table.getColumn(messageSource.getMessage("component.article.table.column.selection", null, localeHolder.getLocale()));
        selectionColumn.setCellEditor(table.getDefaultEditor(Boolean.class));
        selectionColumn.setCellRenderer(table.getDefaultRenderer(Boolean.class));

    }


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

    public ChangedFieldFiltersComponent getChangedFieldFiltersComponent() {
        return changedFieldFiltersComponent;
    }

    public JTable getTable() {
        return table;
    }

    @Override
    public void tableChanged(TableModelEvent e) {

    }
}
