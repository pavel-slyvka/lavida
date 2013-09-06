package com.lavida.swing.service;

import com.google.gdata.util.ServiceException;
import com.lavida.service.FiltersPurpose;
import com.lavida.service.UserService;
import com.lavida.service.ViewColumn;
import com.lavida.service.dao.ArticleDao;
import com.lavida.service.entity.ArticleJdo;
import com.lavida.service.utils.DateConverter;
import com.lavida.swing.LocaleHolder;
import com.lavida.swing.event.ArticleUpdateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.swing.table.AbstractTableModel;
import java.io.IOException;
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
public class ArticlesTableModel extends AbstractTableModel implements ApplicationListener<ArticleUpdateEvent> {
    private static final Logger logger = LoggerFactory.getLogger(ArticlesTableModel.class);

    private List<String> headerTitles = new ArrayList<String>();
    private List<String> articleFieldsSequence;
    private Map<Integer, SimpleDateFormat> columnIndexToDateFormat;
    private ArticleJdo selectedArticle;
    private int totalCountArticles;
    private double totalOriginalCostEUR;
    private double totalPriceUAH;
    private String sellerName;

    @Resource
    private ArticleDao articleDao;

    @Resource
    private ArticleServiceSwingWrapper articleServiceSwingWrapper;

    @Resource
    private UserService userService;

    @Resource
    private MessageSource messageSource;

    @Resource
    private LocaleHolder localeHolder;

    private String queryName;
    private List<ArticleJdo> tableData;
    private static final List<String> FORBIDDEN_ROLES = new ArrayList<String>();

    static {
          FORBIDDEN_ROLES.add("ROLE_SELLER");
    }

    @Override
    public void onApplicationEvent(ArticleUpdateEvent event) {
        tableData = articleDao.get(queryName);
    }

    public List<ArticleJdo> getTableData() {
        if (tableData == null) {
            tableData = articleDao.get(queryName);
        }
        return tableData;
    }

    public FiltersPurpose getFiltersPurpose() {
        if (ArticleJdo.FIND_SOLD.equals(queryName)) {
            return FiltersPurpose.SOLD_PRODUCTS;
        } else if (ArticleJdo.FIND_NOT_SOLD.equals(queryName)){
            return FiltersPurpose.SELL_PRODUCTS;
        } else {
            return FiltersPurpose.ADD_NEW_PRODUCTS;
        }
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
                if (field.getType() == Calendar.class) {          // todo for Date fields
                    this.columnIndexToDateFormat.put(headerTitles.size() - 1,      //todo no patterns in articleJdo fields
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
                            columnHeader.equals(messageSource.getMessage(viewColumn.titleKey(), null, localeHolder.getLocale()))){
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
     *  Returns true if the user has permissions.
     *
     *  @param  rowIndex  the row being queried
     *  @param  columnIndex the column being queried
     *  @return true the user has permissions.
     */
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        if (isForbidden(userService.getCurrentUserRoles(), FORBIDDEN_ROLES)) {
            return false;
        }
        return true;
    }

    /**
     *  This empty implementation is provided so users don't have to implement
     *  this method if their data model is not editable.
     *
     *  @param  aValue   value to assign to cell
     *  @param  rowIndex   row of cell
     *  @param  columnIndex  column of cell
     */
    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        ArticleJdo articleJdo = getArticleJdoByRowIndex(rowIndex);
        String value = (String)aValue;
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
                        ArticleJdo newArticle = (ArticleJdo)articleJdo.clone();
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
                String formattedValue = value.replace("," , ".").trim();
                calendar.setTime(calendarFormatter.parse(formattedValue));
                field.set(articleJdo, calendar);
            } else if (Date.class == field.getType()) {
                String formattedValue = value.replace("," , ".").trim();
                field.set(articleJdo, DateConverter.convertStringToDate(formattedValue));
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
     *  Returns <code>Object.class</code> regardless of <code>columnIndex</code>.
     *
     *  @param columnIndex  the column being queried
     *  @return the {@code Class} object representing the class or interface
     * that declares the field represented by this {@code Field} object.
     */
    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return String.class;
    }

    /**
     * Updates table and remote spreadsheet with the changed article
     * @param changedArticle
     */
    private void updateTable(ArticleJdo changedArticle) {
        try {
            articleServiceSwingWrapper.updateToSpreadsheet(changedArticle, null);
            articleServiceSwingWrapper.update(changedArticle);
            fireTableDataChanged();
        } catch (IOException e) {
            logger.warn(e.getMessage(), e);
            changedArticle.setPostponedOperationDate(new Date());
            articleServiceSwingWrapper.update(changedArticle);
        } catch (ServiceException e) {
            logger.warn(e.getMessage(), e);
            changedArticle.setPostponedOperationDate(new Date());
            articleServiceSwingWrapper.update(changedArticle);
        }

    }

    public void setSelectedArticle(ArticleJdo selectedArticle) {
        this.selectedArticle = selectedArticle;
    }

    public ArticleJdo getSelectedArticle() {
        return selectedArticle;
    }

    public void setQueryName(String queryName) {
        this.queryName = queryName;
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

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public void setTableData(List<ArticleJdo> tableData) {
        this.tableData = tableData;
    }

    public List<String> getHeaderTitles() {
        return headerTitles;
    }
}
