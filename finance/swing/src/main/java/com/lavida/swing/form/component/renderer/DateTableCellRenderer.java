package com.lavida.swing.form.component.renderer;

import javax.swing.table.DefaultTableCellRenderer;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * The DateTableCellRenderer
 * <p/>
 * Created: 16.10.13 15:51.
 *
 * @author Ruslan.
 */
public class DateTableCellRenderer extends DefaultTableCellRenderer {
    private SimpleDateFormat dateFormat;

    public DateTableCellRenderer(String datePattern) {
        super();
        this.dateFormat = new SimpleDateFormat(datePattern);
    }

    @Override
    protected void setValue(Object value) {
        if (value instanceof Date) {
            setText((value == null) ? "" : this.dateFormat.format(value));
        }
    }
}
