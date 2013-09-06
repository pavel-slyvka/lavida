package com.lavida.swing.form.component;

import com.lavida.service.FilterType;

import javax.swing.*;

/**
 * Created: 16:43 06.09.13
 *
 * @author Ruslan
 */
public class FilterUnit {
    public JTextField textField;
    public JLabel label;
    public FilterType filterType;
    public String columnTitle;
    public String columnDatePattern;
    public int order;

    @Override
    public String toString() {
        return "FilterUnit{" +
                "textField=" + textField +
                ", label=" + label +
                ", filterType=" + filterType +
                ", columnTitle='" + columnTitle + '\'' +
                ", columnDatePattern='" + columnDatePattern + '\'' +
                ", order=" + order +
                '}';
    }

}
