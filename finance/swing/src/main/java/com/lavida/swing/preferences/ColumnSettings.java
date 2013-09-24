package com.lavida.swing.preferences;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The class for all table columns' settings.
 * Created by Pavel on 19.09.13.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "columnType", namespace = "http://www.xml.lavida.com/schema/usersSettings.com")
@XmlRootElement
public class ColumnSettings {
    @XmlElement
    private String header;

    @XmlElement
    private int index;

    @XmlElement
    private int width;

    @XmlElement
    private Integer backgroundColor;

    @XmlElement
    private Integer foregroundColor;


    public ColumnSettings() {
    }

    public ColumnSettings(String header, int index, int width, Integer backgroundColor, Integer foregroundColor) {
        this.header = header;
        this.index = index;
        this.width = width;
        this.backgroundColor = backgroundColor;
        this.foregroundColor = foregroundColor;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public Integer getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(Integer backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public Integer getForegroundColor() {
        return foregroundColor;
    }

    public void setForegroundColor(Integer foregroundColor) {
        this.foregroundColor = foregroundColor;
    }


    @Override
    public String toString() {
        return "ColumnSettings{" +
                "header='" + header + '\'' +
                ", index=" + index +
                ", width=" + width +
                ", backgroundColor=" + backgroundColor +
                ", foregroundColor=" + foregroundColor +
                '}';
    }
}
