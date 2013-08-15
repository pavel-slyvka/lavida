package com.lavida.swing.form.tablemodel;

import com.lavida.service.ViewColumn;
import com.lavida.service.entity.ArticleJdo;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import javax.swing.table.AbstractTableModel;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.*;

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

    private List<String> headerTitles = new ArrayList<String>();
    private List<ArticleJdo> tableData = new ArrayList<ArticleJdo>();
    private List<String> articleFieldsSequence;
    private Map<Integer, SimpleDateFormat> columnIndexToDateFormat;

    public ArticlesTableModel() {
    }

    @Override
    public int getRowCount() {
        return tableData.size();
    }

    @Override
    public int getColumnCount() {
        return headerTitles.size();
    }

    @Override
    public String getColumnName(int columnIndex) {
        return headerTitles.get(columnIndex);
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        ArticleJdo articleJdo = getArticleJdoByRowIndex(rowIndex);

        try {
            Field field = ArticleJdo.class.getDeclaredField(articleFieldsSequence.get(columnIndex));
            field.setAccessible(true);
            Object value = field.get(articleJdo);
            if (value instanceof Calendar) {
                return columnIndexToDateFormat.get(columnIndex).format(((Calendar) value).getTime());

            } else {
                return value;
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();    // todo
        } catch (IllegalAccessException e) {
            e.printStackTrace();    // todo
        }
        throw new RuntimeException("Something wrong!");
    }

    public ArticleJdo getArticleJdoByRowIndex(int rowIndex) {
        return tableData.get(rowIndex);
    }

    public void initHeaderFieldAndTitles(MessageSource messageSource, Locale locale) {
        this.articleFieldsSequence = new ArrayList<String>();
        this.headerTitles = new ArrayList<String>();
        this.columnIndexToDateFormat = new HashMap<Integer, SimpleDateFormat>();
        for (Field field : ArticleJdo.class.getDeclaredFields()) {
            ViewColumn viewColumn = field.getAnnotation(ViewColumn.class);
            if (viewColumn != null) {
                field.setAccessible(true);
                this.articleFieldsSequence.add(field.getName());
                if (viewColumn.titleKey().isEmpty()) {
                    this.headerTitles.add(field.getName());
                } else {
                    this.headerTitles.add(messageSource.getMessage(viewColumn.titleKey(), null, locale));
                }
                if (field.getType() == Calendar.class) {
                    this.columnIndexToDateFormat.put(headerTitles.size() - 1,
                            new SimpleDateFormat(viewColumn.datePattern()));
                }
            }
        }
    }

    public List<String> getForbiddenHeadersToShow(MessageSource messageSource, Locale locale, List<String> userRoles) {
        List<String> forbiddenHeaders = new ArrayList<String>();
        for (Field field : ArticleJdo.class.getDeclaredFields()) {
            ViewColumn viewColumn = field.getAnnotation(ViewColumn.class);
            if (viewColumn != null) {
                if (!viewColumn.show() || isForbidden(userRoles, viewColumn.forbiddenRoles())) {
                    forbiddenHeaders.add(viewColumn.titleKey().isEmpty() ? field.getName()
                            : messageSource.getMessage(viewColumn.titleKey(), null, locale));
                }
            }
        }
        return forbiddenHeaders;
    }

    private boolean isForbidden(List<String> userRoles, String forbiddenRoles) {
        if (!forbiddenRoles.isEmpty()) {
            String[] forbiddenRolesArray = forbiddenRoles.split(",");
            for (String forbiddenRole : forbiddenRolesArray) {
                if (userRoles.contains(forbiddenRole.trim())) {
                    return true;
                }
            }
        }
        return false;
    }

    public List<ArticleJdo> getTableData() {
        return tableData;
    }

    public void setTableData(List<ArticleJdo> tableData) {
        this.tableData = tableData;
    }

    public void removeArticle(ArticleJdo removingArticleJdo) {
        for (ArticleJdo articleJdo : tableData) {
            if (articleJdo.getId() == removingArticleJdo.getId()) {
                tableData.remove(articleJdo);
                break;
            }
        }
    }
}
