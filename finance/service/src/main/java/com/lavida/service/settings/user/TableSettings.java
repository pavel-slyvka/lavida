package com.lavida.service.settings.user;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
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
//   @XmlList
    private List<ColumnSettings> columns;

    public TableSettings() {
    }

    public TableSettings(List<ColumnSettings> columns) {
        this.columns = columns;
    }

    public List<ColumnSettings> getColumns() {
        if (columns == null) {
            columns = new ArrayList<>();
        }
        return columns;
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
