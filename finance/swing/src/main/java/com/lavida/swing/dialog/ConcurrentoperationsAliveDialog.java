package com.lavida.swing.dialog;

import com.lavida.swing.form.component.TableComponent;
import com.lavida.swing.service.ConcurrentOperation;
import com.lavida.swing.service.ConcurrentOperationTableModel;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

/**
 * The ConcurrentOperationsAliveDialog
 * <p/>
 * Created: 15.10.13 10:34.
 *
 * @author Ruslan.
 */
@Component
public class ConcurrentOperationsAliveDialog extends AbstractDialog {

    private TableComponent operationsTable;

    @Resource(name = "concurrentOperationTableModel")
    private ConcurrentOperationTableModel tableModel;

    @Override
    protected void initializeForm() {
        super.initializeForm();
        dialog.setTitle(messageSource.getMessage("dialog.concurrent.operations.alive", null, localeHolder.getLocale()));
        dialog.setResizable(true);
        dialog.setBounds(100, 100, 300, 300);
        dialog.setLocationRelativeTo(null);
    }

    @Override
    public void show() {
        super.show();
        tableModel.updateTableData();
    }

    @Override
    protected void initializeComponents() {
        rootContainer.setLayout(new BorderLayout());

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BorderLayout());
        centerPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        operationsTable = new TableComponent();
        operationsTable.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
        operationsTable.setSurrendersFocusOnKeystroke(true);
        operationsTable.setModel(tableModel);
        JTextField textField = new JTextField();
        operationsTable.setCellEditor(new DefaultCellEditor(textField));
        initTableColumnsWidth();
        operationsTable.doLayout();
        operationsTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
//      Filtering the table
        operationsTable.setFillsViewportHeight(true);
        operationsTable.setRowSelectionAllowed(true);
        operationsTable.setCellSelectionEnabled(true); // solution for copying one cell from table
        operationsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        operationsTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting()) return;
                ListSelectionModel listSelectionModel = (ListSelectionModel) e.getSource();
                if (!listSelectionModel.isSelectionEmpty()) {
                    int viewRow = listSelectionModel.getMinSelectionIndex();
                    int selectedRow = operationsTable.convertRowIndexToModel(viewRow);
                    ConcurrentOperation concurrentOperation = tableModel.getConcurrentOperationByRowIndex(selectedRow);
                    tableModel.setSelectedOperation(concurrentOperation);
                }
            }
        });

        JScrollPane tableScrollPane = new JScrollPane(operationsTable);
        tableScrollPane.setPreferredSize(new Dimension(290, 250));
        tableScrollPane.setMinimumSize(new Dimension(200, 150));
        tableScrollPane.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        centerPanel.add(tableScrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.LINE_AXIS));
        JButton okButton = new JButton();
        okButton.setText(messageSource.getMessage("dialog.concurrent.operations.button.ok", null, localeHolder.getLocale()));
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tableModel.updateTableData();
                hide();
            }
        });
        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(okButton);

        rootContainer.add(centerPanel, BorderLayout.CENTER);
        rootContainer.add(buttonPanel, BorderLayout.SOUTH);
    }

    /**
     * Sets preferred width to certain columns
     */
    private void initTableColumnsWidth() {
        Map<String, Integer> columnHeaderToWidth = tableModel.getColumnHeaderToWidth();
        for (Map.Entry<String, Integer> entry : columnHeaderToWidth.entrySet()) {
            operationsTable.getColumn(entry.getKey()).setPreferredWidth(entry.getValue());
        }
    }
}
