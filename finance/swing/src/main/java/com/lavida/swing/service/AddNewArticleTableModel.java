package com.lavida.swing.service;

import com.lavida.service.FiltersPurpose;
import com.lavida.service.ViewColumn;
import com.lavida.service.entity.ArticleJdo;
import com.lavida.service.utils.DateConverter;
import com.lavida.swing.LocaleHolder;
import com.lavida.swing.form.component.AddNewArticleTableComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.swing.table.AbstractTableModel;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * The table model for the addNewArticlesDialog
 * Created: 18:28 03.09.13
 *
 * @author Ruslan
 */
public class AddNewArticleTableModel extends AbstractTableModel {
    private static final Logger logger = LoggerFactory.getLogger(AddNewArticleTableModel.class);

    private List<String> headerTitles = new ArrayList<String>();
    private List<String> articleFieldsSequence;
    private Map<Integer, SimpleDateFormat> columnIndexToDateFormat;
    private ArticleJdo selectedArticle;
    private int totalCountArticles;
    private double totalOriginalCostEUR;
    private double totalPriceUAH;


    @Resource
    private MessageSource messageSource;

    @Resource
    private LocaleHolder localeHolder;

    private List<ArticleJdo> tableData = new ArrayList<ArticleJdo>();

    private static final List<String> FORBIDDEN_ROLES = new ArrayList<String>();

    static {
        FORBIDDEN_ROLES.add("ROLE_SELLER");
    }

    public List<ArticleJdo> getTableData() {
        return tableData;
    }

    public FiltersPurpose getFiltersPurpose() {
        return FiltersPurpose.ADD_NEW_PRODUCTS;
    }

    @Override
    public int getRowCount() {
        return getTableData().size();
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
        Object value = getRawValueAt(rowIndex, columnIndex);
        if (value instanceof Calendar) {
            return columnIndexToDateFormat.get(columnIndex).format(((Calendar) value).getTime());
        } else if (value instanceof Date) {
            return columnIndexToDateFormat.get(columnIndex).format(value);
        } else {
            return value;
        }
    }

    public ArticleJdo getArticleJdoByRowIndex(int rowIndex) {
        return getTableData().get(rowIndex);
    }

    public Object getRawValueAt(int rowIndex, int columnIndex) {
        ArticleJdo articleJdo = getArticleJdoByRowIndex(rowIndex);
        try {
            Field field = ArticleJdo.class.getDeclaredField(articleFieldsSequence.get(columnIndex));
            field.setAccessible(true);
            return field.get(articleJdo);

        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    @PostConstruct
    public void initHeaderFieldAndTitles() {
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
                    this.headerTitles.add(messageSource.getMessage(viewColumn.titleKey(), null, localeHolder.getLocale()));
                }
                if (field.getType() == Calendar.class) {
                    this.columnIndexToDateFormat.put(headerTitles.size() - 1,
                            new SimpleDateFormat(viewColumn.datePattern()));
                } else if (field.getType() == Date.class) {
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

    private boolean isForbidden(List<String> userRoles, List<String> forbiddenRoles) {
        for (String forbiddenRole : forbiddenRoles) {
            if (userRoles.contains(forbiddenRole)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Initializes fields of the analyze component.
     */
    public void initAnalyzeFields() {
        int totalCount = 0;
        double totalCost = 0;
        double totalPrice = 0;
        List<ArticleJdo> articleJdoList = getTableData();
        for (ArticleJdo articleJdo : articleJdoList) {
            ++totalCount;
            totalCost += (articleJdo.getTransportCostEUR() + articleJdo.getPurchasePriceEUR());
            totalPrice += (articleJdo.getSalePrice());
        }
        this.totalCountArticles = totalCount;
        this.totalOriginalCostEUR = totalCost;
        this.totalPriceUAH = totalPrice;
    }


    public Map<String, Integer> getColumnHeaderToWidth() {
        Map<String, Integer> columnHeaderToWidth = new HashMap<String, Integer>(headerTitles.size());
        label:
        for (String columnHeader : headerTitles) {
            Integer width = 0;
            for (Field field : ArticleJdo.class.getDeclaredFields()) {
                ViewColumn viewColumn = field.getAnnotation(ViewColumn.class);
                if (viewColumn != null) {
                    if (viewColumn.titleKey().isEmpty() && columnHeader.equals(field.getName())) {
                        width = viewColumn.columnWidth();
                        columnHeaderToWidth.put(columnHeader, new Integer(width));
                        continue label;
                    } else if (!viewColumn.titleKey().isEmpty() &&
                            columnHeader.equals(messageSource.getMessage(viewColumn.titleKey(), null, localeHolder.getLocale()))) {
                        width = viewColumn.columnWidth();
                        columnHeaderToWidth.put(columnHeader, new Integer(width));
                        continue label;
                    }
                }
            }
        }
        return columnHeaderToWidth;
    }

    /**
     * Returns true.
     *
     * @param rowIndex    the row being queried
     * @param columnIndex the column being queried
     * @return true .
     */
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return true;
    }

    /**
     * This empty implementation is provided so users don't have to implement
     * this method if their data model is not editable.
     *
     * @param aValue      value to assign to cell
     * @param rowIndex    row of cell
     * @param columnIndex column of cell
     */
    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        ArticleJdo articleJdo = getArticleJdoByRowIndex(rowIndex);
        String value = (String) aValue;
        SimpleDateFormat calendarFormatter = new SimpleDateFormat("dd.MM.yyyy");
        calendarFormatter.setLenient(false);
        try {
            Field field = ArticleJdo.class.getDeclaredField(articleFieldsSequence.get(columnIndex));
            field.setAccessible(true);
            if (int.class == field.getType()) {
                field.setInt(articleJdo, Integer.parseInt(value));
                if (field.getName().equals("quantity") && Integer.parseInt(value) > 1) {
                    field.set(articleJdo, new Integer(1));
                    int quantity = Integer.parseInt(value);
                    for (int i = 1; i < quantity; ++i) {
                        ArticleJdo newArticle = articleJdo;
                        newArticle.setSpreadsheetRow(0);
                        newArticle.setId(0);
                        tableData.add(newArticle);
                        updateTable(newArticle);
                    }
                }
            } else if (boolean.class == field.getType()) {
                field.setBoolean(articleJdo, Boolean.parseBoolean(value));
            } else if (double.class == field.getType()) {
                field.setDouble(articleJdo, fixIfNeedAndParseDouble(value));
            } else if (char.class == field.getType()) {
                field.setChar(articleJdo, value.charAt(0));
            } else if (long.class == field.getType()) {
                field.setLong(articleJdo, Long.parseLong(value));
            } else if (Integer.class == field.getType()) {
                field.set(articleJdo, Integer.parseInt(value));
            } else if (Boolean.class == field.getType()) {
                field.set(articleJdo, Boolean.parseBoolean(value));
            } else if (Double.class == field.getType()) {
                field.set(articleJdo, fixIfNeedAndParseDouble(value));
            } else if (Character.class == field.getType()) {
                field.set(articleJdo, value.charAt(0));
            } else if (Long.class == field.getType()) {
                field.set(articleJdo, Long.parseLong(value));
            } else if (Calendar.class == field.getType()) {
                Calendar calendar = Calendar.getInstance();
                String formattedValue = value.replace(",", ".").trim();
                calendar.setTime(!formattedValue.isEmpty() ? (calendarFormatter.parse(formattedValue)) : null);
                field.set(articleJdo, calendar);
            } else if (Date.class == field.getType()) {
                String formattedValue = value.replace(",", ".").trim();
                Date date = !formattedValue.isEmpty() ? DateConverter.convertStringToDate(formattedValue) : null;
                field.set(articleJdo, date);
            } else {
                field.set(articleJdo, value);
            }
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
            throw new RuntimeException(e);
        }
        updateTable(articleJdo);
    }

    private double fixIfNeedAndParseDouble(String doubleString) {  // todo make reusable with GoogleCellsTransformer
        if (doubleString == null || doubleString.trim().isEmpty()) {
            return 0;
        }
        return Double.parseDouble(doubleString.replace(" ", "").replace(",", "."));
    }


    /**
     * Returns <code>Object.class</code> regardless of <code>columnIndex</code>.
     *
     * @param columnIndex the column being queried
     * @return the {@code Class} object representing the class or interface
     * that declares the field represented by this {@code Field} object.
     */
    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return String.class;
    }

    /**
     * Updates table and remote spreadsheet with the changed article
     *
     * @param changedArticle
     */
    private void updateTable(ArticleJdo changedArticle) {
        fireTableDataChanged();
    }

    public void setSelectedArticle(ArticleJdo selectedArticle) {
        this.selectedArticle = selectedArticle;
    }

    public ArticleJdo getSelectedArticle() {
        return selectedArticle;
    }

    public int getTotalCountArticles() {
        return totalCountArticles;
    }

    public double getTotalOriginalCostEUR() {
        return totalOriginalCostEUR;
    }

    public double getTotalPriceUAH() {
        return totalPriceUAH;
    }

    public void setTableData(List<ArticleJdo> tableData) {
        this.tableData = tableData;
    }


}
