package com.lavida.swing.form.tablemodel;

import com.lavida.service.ArticleService;
import com.lavida.service.entity.ArticleJdo;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.swing.table.AbstractTableModel;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

/**
 * Created: 15:33 06.08.13
 * <p/>
 * The <code>ArticlesTableModel</code> class specifies the methods the
 * <code>JTable</code> will use to interrogate a tabular data model of ArticlesJdo.
 *
 * @author Ruslan
 */
@Component
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

        if (columnIndex == 0) {              // id
            return articleJdo.getId();
        } else if (columnIndex == 1) {       // Code
            return articleJdo.getCode();
        } else if (columnIndex == 2) {       // Name
            return articleJdo.getName();
        } else if (columnIndex == 3) {       // Brand
            return articleJdo.getBrand();
        } else if (columnIndex == 4) {       // Quantity
            return articleJdo.getQuantity();
        } else if (columnIndex == 5) {       // Size
            return articleJdo.getSize();
        } else if (columnIndex == 6) {       // purchasing price
            return articleJdo.getPurchasingPriceEUR();
        } else if (columnIndex == 7) {       // transport cost
            return articleJdo.getTransportCostEUR();
        } else if (columnIndex == 8) {       // delivery date
              return (articleJdo.getDeliveryDate() == null) ? "":dateFormat.format(articleJdo.getDeliveryDate().getTime());
        } else if (columnIndex == 9) {       // current price
            return articleJdo.getPriceUAH();
        } else if (columnIndex == 10) {      // raised price
            return articleJdo.getRaisedPriceUAH();
        } else if (columnIndex == 11) {      // action price
            return articleJdo.getActionPriceUAH();
        } else if (columnIndex == 12) {      // sold
            return articleJdo.getSold();
        } else if (columnIndex == 13) {      // ours
            return articleJdo.getOurs();
        } else if (columnIndex == 14) {      // sale date
            return (articleJdo.getSaleDate() == null)? "" : dateFormat.format(articleJdo.getSaleDate().getTime());
        } else if (columnIndex == 15) {      // comment
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

    public void updateArticle(ArticleJdo newArticleJdo) {
        List<ArticleJdo> articles = getTableData();
        for(ArticleJdo articleJdo : articles) {
            if(articleJdo.getId() == newArticleJdo.getId()) {
                articles.remove(articleJdo);
                break;
            }
        }
    }
}
