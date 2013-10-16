package com.lavida.swing.form.component.renderer;

import javax.swing.table.DefaultTableCellRenderer;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * The CalendarTableCellRenderer
 * <p/>
 * Created: 16.10.13 16:17.
 *
 * @author Ruslan.
 */
public class CalendarTableCellRenderer extends DefaultTableCellRenderer {
    private SimpleDateFormat dateFormat;

    public CalendarTableCellRenderer(String datePattern) {
        this.dateFormat = new SimpleDateFormat(datePattern);
    }

    @Override
    protected void setValue(Object value) {
        if (value instanceof Calendar) {
            Calendar calendar = (Calendar)value;
            setText(value == null ? "" : dateFormat.format(calendar.getTime()));
        }
    }
}
