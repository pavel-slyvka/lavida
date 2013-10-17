package com.lavida.swing.form.component.renderer;

import javax.swing.table.DefaultTableCellRenderer;

/**
 * The DoubleTableCellRenderer
 * <p/>
 * Created: 17.10.13 10:40.
 *
 * @author Ruslan.
 */
public class DoubleTableCellRenderer extends DefaultTableCellRenderer {
    public DoubleTableCellRenderer() {
        super();
    }

    @Override
    protected void setValue(Object value) {
        if (value instanceof Double) {
            setText((value == null || value == 0) ? "" : Double.toString((Double)value));
        }
    }
}
