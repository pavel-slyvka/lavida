package com.lavida.swing;

import com.lavida.service.ArticleService;
import com.lavida.service.entity.ArticleJdo;

import javax.swing.table.AbstractTableModel;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * Created: 15:33 06.08.13
 * <p/>
 * The <code>ArticlesTableModel</code> class specifies the methods the
 * <code>JTable</code> will use to interrogate a tabular data model of ArticlesJdo.
 *
 * @author Ruslan
 */
public class ArticlesTableModel extends AbstractTableModel {

    private List<String> tableHeader;
    private List<ArticleJdo> tableData;

    public ArticlesTableModel() {
    }

    @Override
    public int getRowCount() {
        return tableData.size();
    }

    @Override
    public int getColumnCount() {
        return tableHeader.size();
    }
    @Override
    public String getColumnName(int columnIndex) {
        return tableHeader.get(columnIndex);
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        SimpleDateFormat  dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        ArticleJdo articleJdo = tableData.get(rowIndex);
        if (columnIndex == 0) {
            return articleJdo.getId();
        } else if (columnIndex == 1) {
            return articleJdo.getCode();
        } else if (columnIndex == 2) {
            return articleJdo.getName();
        } else if (columnIndex == 3) {
            return articleJdo.getBrand();
        } else if (columnIndex == 4) {
            return articleJdo.getQuantity();
        } else if (columnIndex == 5) {
            return articleJdo.getSize();
        } else if (columnIndex == 6) {
            return articleJdo.getPurchasingPriceEUR();
        } else if (columnIndex == 7) {
            return articleJdo.getTransportCostEUR();
        } else if (columnIndex == 8) {
              return (articleJdo.getDeliveryDate() == null) ? "":dateFormat.format(articleJdo.getDeliveryDate().getTime());
        } else if (columnIndex == 9) {
            return articleJdo.getPriceUAH();
        } else if (columnIndex == 10) {
            return articleJdo.getRaisedPriceUAH();
        } else if (columnIndex == 11) {
            return articleJdo.getActionPriceUAH();
        } else if (columnIndex == 12) {
            return articleJdo.getSold();
        } else if (columnIndex == 13) {
            return articleJdo.getOurs();
        } else if (columnIndex == 14) {
            return (articleJdo.getSaleDate() == null)? "" : dateFormat.format(articleJdo.getSaleDate().getTime());
        } else if (columnIndex == 15) {
            return articleJdo.getComment();
        } else return "";
    }

    public List<String> getTableHeader() {
        return tableHeader;
    }

    public void setTableHeader(List<String> tableHeader) {
        this.tableHeader = tableHeader;
    }

    public List<ArticleJdo> getTableData() {
        return tableData;
    }

    public void setTableData(List<ArticleJdo> tableData) {
        this.tableData = tableData;
    }
}
