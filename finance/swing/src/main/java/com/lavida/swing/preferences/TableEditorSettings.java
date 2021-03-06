package com.lavida.swing.preferences;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * The TableEditorSettings
 * <p/>
 * Created: 24.09.13 15:57.
 *
 * @author Ruslan.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tableEditorType", namespace = "http://www.xml.lavida.com/schema/usersSettings.com")
@XmlRootElement
public class TableEditorSettings {

    @XmlAttribute
    private String tableEditorSettingsName;

    @XmlElement
    private List<ColumnEditorSettings> columnEditors;

    public TableEditorSettings() {
    }

    public List<ColumnEditorSettings> getColumnEditors() {
        return columnEditors;
    }

    public void setColumnEditors(List<ColumnEditorSettings> columnEditors) {
        this.columnEditors = columnEditors;
    }

    public String getTableEditorSettingsName() {
        return tableEditorSettingsName;
    }

    public void setTableEditorSettingsName(String tableEditorSettingsName) {
        this.tableEditorSettingsName = tableEditorSettingsName;
    }

    @Override
    public String toString() {
        return "TableEditorSettings{" +
                "tableEditorSettingsName='" + tableEditorSettingsName + '\'' +
                ", columnEditors=" + columnEditors +
                '}';
    }
}
