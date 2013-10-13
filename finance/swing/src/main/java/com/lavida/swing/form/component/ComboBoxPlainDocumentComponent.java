package com.lavida.swing.form.component;

import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;
import javax.swing.text.PlainDocument;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * The ComboBoxPlainDocumentComponent
 * <p/>
 * Created: 13.10.13 11:18.
 *
 * @author Ruslan.
 */
public class ComboBoxPlainDocumentComponent extends PlainDocument {
    JComboBox comboBox;
    ComboBoxModel model;
    JTextComponent editor;
//    boolean selecting=false;

    public ComboBoxPlainDocumentComponent(final JComboBox comboBox) {
        this.comboBox = comboBox;
        model = comboBox.getModel();
        editor = (JTextComponent) comboBox.getEditor().getEditorComponent();
        comboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
//                if (!selecting) highlightCompletedText(0);
                highlightCompletedText(0);
            }
        });
        // Handle initially selected object
        Object selected = comboBox.getSelectedItem();
        if (selected!=null) setText(selected.toString());
        highlightCompletedText(0);
    }

    @Override
    public void remove(int offs, int len) throws BadLocationException {
        // return immediately when selecting an item
//        if (selecting) return;
            super.remove(offs, len);
    }

    @Override
    public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
        // return immediately when selecting an item
//        if (selecting) return;
        // insert the string into the document
        super.insertString(offs, str, a);
        // lookup and select a matching item
        Object item = lookupItem(getText(0, getLength()));
        boolean listContainsSelectedItem = true;
        if (item == null) {
            // no item matches => use the current input as selected item
            item=getText(0, getLength());
            listContainsSelectedItem=false;
        }
//        setSelectedItem(item);
        setText(item.toString());
// select the completed part
        if (listContainsSelectedItem) highlightCompletedText(offs+str.length());

    }

    private void setText(String text) {
        try {
            // remove all text and insert the completed string
            super.remove(0, getLength());
            super.insertString(0, text, null);
        } catch (BadLocationException e) {
            throw new RuntimeException(e.toString());
        }
    }

    private void highlightCompletedText(int start) {
        editor.setCaretPosition(getLength());
        editor.moveCaretPosition(start);
    }

/*
    private void setSelectedItem(Object item) {
        selecting = true;
        model.setSelectedItem(item);
        selecting = false;
    }
*/

/*
    private Object lookupItem(String pattern) {
        Object selectedItem = model.getSelectedItem();
        // only search for a different item if the currently selected does not match
        if (selectedItem != null && startsWithIgnoreCase(selectedItem.toString(), pattern)) {
            return selectedItem;
        } else {
            // iterate over all items
            for (int i=0, n=model.getSize(); i < n; i++) {
                Object currentItem = model.getElementAt(i);
                // current item starts with the pattern?
                if (startsWithIgnoreCase(currentItem.toString(), pattern)) {
                    return currentItem;
                }
            }
        }
        // no item starts with the pattern => return null
        return null;
    }
*/
private Object lookupItem(String pattern) {
    Object selectedItem = model.getSelectedItem();
    // only search for a different item if the currently selected does not match
    if (selectedItem != null && selectedItem.toString().startsWith( pattern)) {
        return selectedItem;
    } else {
        // iterate over all items
        for (int i=0, n=model.getSize(); i < n; i++) {
            Object currentItem = model.getElementAt(i);
            // current item starts with the pattern?
            if (currentItem.toString().startsWith(pattern)) {
                return currentItem;
            }
        }
    }
    // no item starts with the pattern => return null
    return null;
}



}
