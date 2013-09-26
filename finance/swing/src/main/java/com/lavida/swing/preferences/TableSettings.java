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

    @XmlAttribute
    private String tableSettingsName;

   @XmlElement
    private List<ColumnSettings> columns;

    public TableSettings() {
    }

    public String getTableSettingsName() {
        return tableSettingsName;
    }

    public void setTableSettingsName(String tableSettingsName) {
        this.tableSettingsName = tableSettingsName;
    }

    public List<ColumnSettings> getColumns() {
        return columns;
    }

    public void setColumns(List<ColumnSettings> columns) {
        this.columns = columns;
    }

    @Override
    public String toString() {
        return "TableSettings{" +
                "tableSettingsName='" + tableSettingsName + '\'' +
                ", columns=" + columns +
                '}';
    }
}
