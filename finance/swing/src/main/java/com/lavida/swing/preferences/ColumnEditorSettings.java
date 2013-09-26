package com.lavida.swing.preferences;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The ColumnEditorSettings
 * <p/>
 * Created: 24.09.13 15:49.
 *
 * @author Ruslan.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "columnEditorType", namespace = "http://www.xml.lavida.com/schema/usersSettings.com")
@XmlRootElement
public class ColumnEditorSettings {

    @XmlAttribute
    private String header;

    @XmlElement
    private List<String> comboBoxItem;

    public ColumnEditorSettings() {
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public List<String> getComboBoxItem() {
        if (comboBoxItem == null) {
            comboBoxItem = new ArrayList<>();
        }
        return comboBoxItem;
    }

    public void setComboBoxItem(List<String> comboBoxItem) {
        this.comboBoxItem = comboBoxItem;
    }


    @Override
    public String toString() {
        return "ColumnEditorSettings{" +
                "header='" + header + '\'' +
                ", comboBoxItem=" + comboBoxItem +
                '}';
    }
}
