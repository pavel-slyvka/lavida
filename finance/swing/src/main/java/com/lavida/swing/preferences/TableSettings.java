package com.lavida.swing.preferences;


import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * The settings for all table components.
 * Created: 15:25 18.09.13
 *
 * @author Ruslan
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tableType", namespace = "http://www.xml.lavida.com/schema/usersSettings.com")
@XmlRootElement
public class TableSettings {

   @XmlElement
    private List<ColumnSettings> columns;

    public TableSettings() {
    }

    public void setColumns(List<ColumnSettings> columns) {
        this.columns = columns;
    }

    @Override
    public String toString() {
        return "TableSettings{" +
                "columns=" + columns +
                '}';
    }
}
