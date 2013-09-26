package com.lavida.swing.form.component;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.EventObject;

/**
 * The TableComponent is the customized JTable.
 * <p/>
 * Created: 26.09.13 12:24.
 *
 * @author Ruslan.
 */
public class TableComponent extends JTable {

    private int lastRow;
    private int lastColumn;
    private int selectedRow;
    private int selectedColumn;

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
                if (Character.isDefined(enteredChar)) {
                    textComponent.setText(String.valueOf(enteredChar));
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

}
