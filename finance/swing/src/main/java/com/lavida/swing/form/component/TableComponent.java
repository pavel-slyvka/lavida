package com.lavida.swing.form.component;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.lang.reflect.Field;
import java.util.Enumeration;
import java.util.EventObject;

/**
 * The TableComponent is the customized JTable.
 * <p/>
 * Created: 26.09.13 12:24.
 *
 * @author Ruslan.
 */
public class TableComponent extends JTable {
    private Throwable printError;

    private int lastRow;
    private int lastColumn;
    private int selectedRow;
    private int selectedColumn;
    private int oldTableScale = 100;
    private int tableScale = 100;

    @Override
    public boolean editCellAt(int row, int column, EventObject e) {
        lastRow = getRowCount() - 1;
        lastColumn = getColumnCount() - 1;
        selectedRow = row;
        selectedColumn = column;

        boolean result = super.editCellAt(row, column, e);
        final Component editor = getEditorComponent();
        if (editor == null || !((editor instanceof JTextComponent) || (editor instanceof JComboBox))) {
            return result;
        }
        if (e instanceof KeyEvent) {
            char enteredChar = ((KeyEvent) e).getKeyChar();
            int code = ((KeyEvent) e).getKeyCode();
            if (editor instanceof JTextComponent) {
                JTextComponent textComponent = (JTextComponent) editor;
                textComponent.selectAll();
            } else if (editor instanceof JComboBox) {
                JComboBox comboBox = (JComboBox) editor;
                comboBox.setEditable(true);
                JTextComponent textComponent = (JTextComponent) comboBox.getEditor().getEditorComponent();
                if (Character.isDefined(enteredChar) && code != KeyEvent.VK_DELETE) {
                    textComponent.setText(String.valueOf(enteredChar));
                } else if (code == KeyEvent.VK_DELETE) {
                    textComponent.setText("");
                }
            }
        }

        return result;
    }

    @Override
    public void editingStopped(ChangeEvent e) {
        super.editingStopped(e);
        if (selectedRow < lastRow) {
            setRowSelectionInterval(selectedRow + 1, selectedRow + 1);
            setColumnSelectionInterval(selectedColumn, selectedColumn);
        } else if (selectedRow == lastRow) {
            setRowSelectionInterval(selectedRow, selectedRow);
            setColumnSelectionInterval(selectedColumn, selectedColumn);
        }
    }

    public int getTableScale() {
        return tableScale;
    }

    public void setTableScale(int tableScale) {
        this.tableScale = tableScale;
        adjustTableScale();
    }

    public void adjustTableScale() {
        if (tableScale == oldTableScale) return;
        if (tableScale > 9) {
            int tableRowHeight = this.getRowHeight();
            tableRowHeight =  (tableRowHeight * tableScale / oldTableScale);
//            tableRowHeight =  (int)Math.ceil((double)(this.getRowHeight()) * tableScale / oldTableScale);
            this.setRowHeight(tableRowHeight);

            int tableRowMargin = this.getRowMargin();
            tableRowMargin =  (tableRowMargin * tableScale / oldTableScale);
            this.setRowMargin(tableRowMargin);

            Font tableFont = this.getFont();
            tableFont = tableFont.deriveFont(tableFont.getSize2D() * ((float)tableScale) / oldTableScale);
            this.setFont(tableFont);

            Font headerFont = (this.getTableHeader()).getFont();
            headerFont = tableFont.deriveFont(headerFont.getSize2D() * ((float)tableScale) / oldTableScale);
            (this.getTableHeader()).setFont(headerFont);

            Enumeration<TableColumn> columnEnumeration = this.getColumnModel().getColumns();
//            int oldTotalWidth = this.getColumnModel().getTotalColumnWidth();
            JTableHeader tableHeader1 = this.getTableHeader();
            while (columnEnumeration.hasMoreElements()) {
                TableColumn tableColumn = columnEnumeration.nextElement();
                int width = tableColumn.getPreferredWidth();
                width = (width * tableScale / oldTableScale);
                tableColumn.setPreferredWidth(width);
                tableColumn.setWidth(width);
                if(width > tableColumn.getMinWidth()) {
                    tableColumn.setMinWidth(width);
                } else if (width > 15) {
                    tableColumn.setMinWidth(15);
                }
                Rectangle columnRectangle = tableHeader1.getHeaderRect(tableColumn.getModelIndex());
                int headerHeight = columnRectangle.height;
                headerHeight = (headerHeight * tableScale / oldTableScale);
                columnRectangle.height = headerHeight;
            }
            oldTableScale = tableScale;
        }
    }
}
