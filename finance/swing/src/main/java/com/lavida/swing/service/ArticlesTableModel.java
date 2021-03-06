package com.lavida.swing.service;

import com.lavida.service.*;
import com.lavida.service.dao.ArticleDao;
import com.lavida.service.entity.ArticleJdo;
import com.lavida.service.remote.google.LavidaGoogleException;
import com.lavida.service.utils.DateConverter;
import com.lavida.swing.LocaleHolder;
import com.lavida.swing.event.ArticleUpdateEvent;
import com.lavida.swing.exception.LavidaSwingRuntimeException;
import com.lavida.swing.exception.RemoteUpdateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.swing.table.AbstractTableModel;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

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

    public List<String> headerTitles = new ArrayList<>();
    private List<String> articleFieldsSequence;
    private Map<Integer, SimpleDateFormat> columnIndexToDateFormat;

    private ArticleJdo selectedArticle;
    private int totalCountArticles;
    private double totalPurchaseCostEUR, totalCostEUR, totalPriceUAH, totalCostUAH, minimalMultiplier, normalMultiplier,
            totalTransportCostEUR, profitUAH;
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

    @Resource
    private ArticleCalculator articleCalculator;

    @Resource
    private ConcurrentOperationsService concurrentOperationsService;

    @Resource
    private BrandService brandService;

    @Resource
    private SizeService sizeService;

    @Resource
    private ShopService shopService;

    private String queryName;
    private List<ArticleJdo> tableData;

    @Override
    public void onApplicationEvent(ArticleUpdateEvent event) {
        updateTableData();
    }

    private void updateTableData() {
        if (queryName != null) {
            tableData = articleDao.get(queryName);
        }
    }

    public List<ArticleJdo> getTableData() {
        if (tableData == null && queryName != null) {
            tableData = articleDao.get(queryName);
        }
        return tableData;
    }

    public FiltersPurpose getFiltersPurpose() {
        if (queryName != null) {
            switch (queryName) {
                case ArticleJdo.FIND_SOLD:
                    return FiltersPurpose.SOLD_PRODUCTS;
                case ArticleJdo.FIND_SOLD_LA_VIDA:
                    return FiltersPurpose.SOLD_PRODUCTS;
                case ArticleJdo.FIND_SOLD_SLAVYANKA:
                    return FiltersPurpose.SOLD_PRODUCTS;
                case ArticleJdo.FIND_SOLD_NOVOMOSKOVSK:
                    return FiltersPurpose.SOLD_PRODUCTS;
                case ArticleJdo.FIND_SOLD_ALEXANDRIA:
                    return FiltersPurpose.SOLD_PRODUCTS;

                case ArticleJdo.FIND_NOT_SOLD:
                    return FiltersPurpose.SELL_PRODUCTS;
                case ArticleJdo.FIND_NOT_SOLD_LA_VIDA:
                    return FiltersPurpose.SELL_PRODUCTS;
                case ArticleJdo.FIND_NOT_SOLD_SLAVYANKA:
                    return FiltersPurpose.SELL_PRODUCTS;
                case ArticleJdo.FIND_NOT_SOLD_NOVOMOSKOVSK:
                    return FiltersPurpose.SELL_PRODUCTS;
                case ArticleJdo.FIND_NOT_SOLD_ALEXANDRIA:
                    return FiltersPurpose.SELL_PRODUCTS;

            }
        }
        return FiltersPurpose.ADD_NEW_PRODUCTS;
/*
        if (ArticleJdo.FIND_SOLD.equals(queryName) || ArticleJdo.FIND_SOLD_LA_VIDA.equals(queryName) ||
                ArticleJdo.FIND_SOLD_SLAVYANKA.equals(queryName) || ArticleJdo.FIND_SOLD_NOVOMOSKOVSK.equals(queryName)) {
            return FiltersPurpose.SOLD_PRODUCTS;
        } else if (ArticleJdo.FIND_NOT_SOLD.equals(queryName) || ArticleJdo.FIND_NOT_SOLD_LA_VIDA.equals(queryName) ||
                ArticleJdo.FIND_NOT_SOLD_SLAVYANKA.equals(queryName) || ArticleJdo.FIND_NOT_SOLD_NOVOMOSKOVSK.equals(queryName)) {
            return FiltersPurpose.SELL_PRODUCTS;
        } else {
            return FiltersPurpose.ADD_NEW_PRODUCTS;
        }
*/
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
//        if (value instanceof Calendar) {
//            return columnIndexToDateFormat.get(columnIndex).format(((Calendar) value).getTime());
//        } else if (value instanceof Date) {
//            return columnIndexToDateFormat.get(columnIndex).format(value);
//        } else {
        return value;
//        }
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
        this.articleFieldsSequence = new ArrayList<>();
        this.headerTitles = new ArrayList<>();
        this.columnIndexToDateFormat = new HashMap<>();
        if (queryName != null) {
            this.tableData = articleDao.get(queryName);
        } else {
            this.tableData = new ArrayList<>();
        }
        for (Field field : ArticleJdo.class.getDeclaredFields()) {
            ViewColumn viewColumn = field.getAnnotation(ViewColumn.class);
            if (viewColumn != null && viewColumn.show()) {
                field.setAccessible(true);
                if (ArticleJdo.FIND_NOT_SOLD.equals(queryName) &&
                        !(viewColumn.titleKey().equals("mainForm.table.articles.column.sell.marker.title") ||
                                viewColumn.titleKey().equals("mainForm.table.articles.column.sell.date.title") ||
                                viewColumn.titleKey().equals("mainForm.table.articles.column.seller.title") ||
                                viewColumn.titleKey().equals("mainForm.table.articles.column.tags.title") ||
                                viewColumn.titleKey().equals("mainForm.table.articles.column.refund.title"))) {
                    this.articleFieldsSequence.add(field.getName());
                    if (viewColumn.titleKey().isEmpty()) {
                        this.headerTitles.add(field.getName());
                    } else {
                        this.headerTitles.add(messageSource.getMessage(viewColumn.titleKey(), null, localeHolder.getLocale()));
                    }
                } else if (ArticleJdo.FIND_SOLD.equals(queryName)) {
                    this.articleFieldsSequence.add(field.getName());
                    if (viewColumn.titleKey().isEmpty()) {
                        this.headerTitles.add(field.getName());
                    } else {
                        this.headerTitles.add(messageSource.getMessage(viewColumn.titleKey(), null, localeHolder.getLocale()));
                    }
                } else if (queryName == null &&
                        !(viewColumn.titleKey().equals("mainForm.table.articles.column.sell.marker.title") ||
                                viewColumn.titleKey().equals("mainForm.table.articles.column.sell.date.title") ||
                                viewColumn.titleKey().equals("mainForm.table.articles.column.seller.title") ||
                                viewColumn.titleKey().equals("mainForm.table.articles.column.tags.title") ||
                                viewColumn.titleKey().equals("mainForm.table.articles.column.raised.price.uah.title") ||
                                viewColumn.titleKey().equals("mainForm.table.articles.column.old.price.uah.title") ||
                                viewColumn.titleKey().equals("mainForm.table.articles.column.refund.title"))
                        ) {
                    this.articleFieldsSequence.add(field.getName());
                    if (viewColumn.titleKey().isEmpty()) {
                        this.headerTitles.add(field.getName());
                    } else {
                        this.headerTitles.add(messageSource.getMessage(viewColumn.titleKey(), null, localeHolder.getLocale()));
                    }
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
        List<String> forbiddenHeaders = new ArrayList<>();
        for (Field field : ArticleJdo.class.getDeclaredFields()) {
            ViewColumn viewColumn = field.getAnnotation(ViewColumn.class);
            if (viewColumn != null && viewColumn.show()) {
                if (ArticleJdo.FIND_NOT_SOLD.equals(queryName) &&
                        !(viewColumn.titleKey().equals("mainForm.table.articles.column.sell.marker.title") ||
                                viewColumn.titleKey().equals("mainForm.table.articles.column.sell.date.title") ||
                                viewColumn.titleKey().equals("mainForm.table.articles.column.seller.title") ||
                                viewColumn.titleKey().equals("mainForm.table.articles.column.tags.title") ||
                                viewColumn.titleKey().equals("mainForm.table.articles.column.refund.title"))) {
                    if (isForbidden(userRoles, viewColumn.forbiddenRoles())) {
                        forbiddenHeaders.add(viewColumn.titleKey().isEmpty() ? field.getName()
                                : messageSource.getMessage(viewColumn.titleKey(), null, locale));
                    }
                } else if (ArticleJdo.FIND_SOLD.equals(queryName)) {
                    if (isForbidden(userRoles, viewColumn.forbiddenRoles())) {
                        forbiddenHeaders.add(viewColumn.titleKey().isEmpty() ? field.getName()
                                : messageSource.getMessage(viewColumn.titleKey(), null, locale));
                    }
                } else if (queryName == null &&
                        !(viewColumn.titleKey().equals("mainForm.table.articles.column.sell.marker.title") ||
                                viewColumn.titleKey().equals("mainForm.table.articles.column.sell.date.title") ||
                                viewColumn.titleKey().equals("mainForm.table.articles.column.seller.title") ||
                                viewColumn.titleKey().equals("mainForm.table.articles.column.tags.title") ||
                                viewColumn.titleKey().equals("mainForm.table.articles.column.raised.price.uah.title") ||
                                viewColumn.titleKey().equals("mainForm.table.articles.column.old.price.uah.title") ||
                                viewColumn.titleKey().equals("mainForm.table.articles.column.refund.title"))) {
                    if (isForbidden(userRoles, viewColumn.forbiddenRoles())) {
                        forbiddenHeaders.add(viewColumn.titleKey().isEmpty() ? field.getName()
                                : messageSource.getMessage(viewColumn.titleKey(), null, locale));
                    }
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

    /**
     * Initializes fields of the analyze component.
     */
    public void initAnalyzeFields() {
        int totalCount = 0;
        double totalCostEUR = 0;
        double totalPrice = 0;
        double totalPurchaseCostEUR = 0;
        double totalCostUAH = 0;
        double normalMultiplierSum = 0;
        double normalMultiplier = 0;
        double minimalMultiplier = 0;
        double totalTransportCostEUR = 0;
        List<ArticleJdo> articleJdoList = getTableData();
        if (articleJdoList.size() > 0) {
            minimalMultiplier = articleJdoList.get(0).getMultiplier();
            for (ArticleJdo articleJdo : articleJdoList) {
                ++totalCount;
                totalPurchaseCostEUR += articleJdo.getPurchasePriceEUR();
                totalTransportCostEUR += articleJdo.getTransportCostEUR();
                totalCostEUR += articleJdo.getTotalCostEUR();
                totalCostUAH += articleJdo.getTotalCostUAH();
                totalPrice += (articleJdo.getSalePrice());
                if (minimalMultiplier > articleJdo.getMultiplier()) {
                    minimalMultiplier = articleJdo.getMultiplier();
                }
                normalMultiplierSum += articleJdo.getMultiplier();
            }
            normalMultiplier = normalMultiplierSum / totalCount;
        }

        this.totalCountArticles = totalCount;
        this.totalCostEUR = totalCostEUR;
        this.totalPriceUAH = totalPrice;
        this.totalPurchaseCostEUR = totalPurchaseCostEUR;
        this.totalCostUAH = totalCostUAH;
        this.minimalMultiplier = minimalMultiplier;
        this.normalMultiplier = normalMultiplier;
        this.totalTransportCostEUR = totalTransportCostEUR;
        this.profitUAH = totalPrice - totalCostUAH;

    }


    public Map<String, Integer> getColumnHeaderToWidth() {
        Map<String, Integer> columnHeaderToWidth = new HashMap<>(headerTitles.size());
        label:
        for (String columnHeader : headerTitles) {
            Integer width;
            for (Field field : ArticleJdo.class.getDeclaredFields()) {
                ViewColumn viewColumn = field.getAnnotation(ViewColumn.class);
                if (viewColumn != null) {
                    if (viewColumn.titleKey().isEmpty() && columnHeader.equals(field.getName())) {
                        width = viewColumn.columnWidth();
                        columnHeaderToWidth.put(columnHeader, width);
                        continue label;
                    } else if (!viewColumn.titleKey().isEmpty() &&
                            columnHeader.equals(messageSource.getMessage(viewColumn.titleKey(), null, localeHolder.getLocale()))) {
                        width = viewColumn.columnWidth();
                        columnHeaderToWidth.put(columnHeader, width);
                        continue label;
                    }
                }
            }
        }
        return columnHeaderToWidth;
    }

    /**
     * Returns true if the user has permissions.
     *
     * @param rowIndex    the row being queried
     * @param columnIndex the column being queried
     * @return true the user has permissions.
     */
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        if (articleFieldsSequence.get(columnIndex).equals("totalCostEUR") ||
                articleFieldsSequence.get(columnIndex).equals("calculatedSalePrice")) {
            return false;
        }
        if (queryName == null) {
            return true;
        }
        if (articleFieldsSequence.get(columnIndex).equals("code") ||
                articleFieldsSequence.get(columnIndex).equals("size")) {
            if (queryName != null) {
                return false;
            }
        }
        return !userService.hasForbiddenRole();
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
        ArticleJdo oldArticle;
        String value = aValue.toString();
        SimpleDateFormat calendarFormatter = new SimpleDateFormat("dd.MM.yyyy");
        calendarFormatter.setLenient(false);
        boolean toUpdate = true;
        try {
            oldArticle = (ArticleJdo) articleJdo.clone();
            Field field = ArticleJdo.class.getDeclaredField(articleFieldsSequence.get(columnIndex));
            field.setAccessible(true);
            if (int.class == field.getType()) {
                int typeValue;
                if (value.isEmpty()) {
                    typeValue = 1;
                } else {
                    typeValue = Integer.parseInt(value);
                }
                if (typeValue != field.getInt(articleJdo)) {
                    field.setInt(articleJdo, typeValue);
                    if (field.getName().equals("quantity") && typeValue > 1) {
                        field.set(articleJdo, 1);
                        for (int i = 1; i < typeValue; ++i) {
                            ArticleJdo newArticle = (ArticleJdo) articleJdo.clone();
                            newArticle.setSpreadsheetRow(0);
                            newArticle.setId(0);
                            tableData.add(newArticle);
                            updateTable(oldArticle, newArticle);
                        }
                    }
                } else return;
            } else if (boolean.class == field.getType()) {
                boolean typeValue = Boolean.parseBoolean(value);
                if (field.getName().equals("selected")) {
                    toUpdate = false;
                }
                if (typeValue != field.getBoolean(articleJdo)) {
                    field.setBoolean(articleJdo, typeValue);
                } else return;
            } else if (double.class == field.getType()) {
                double typeValue;
                if (value.isEmpty()) {
                    typeValue = 0.0;
                } else {
                    typeValue = articleCalculator.fixIfNeedAndParseDouble(value);
                    typeValue = BigDecimal.valueOf(typeValue).setScale(2, BigDecimal.ROUND_HALF_DOWN).doubleValue();
                }
                if (typeValue != field.getDouble(articleJdo)) {
                    field.setDouble(articleJdo, typeValue);
                    if (field.getName().equals("transportCostEUR") || field.getName().equals("purchasePriceEUR")) {
                        articleCalculator.calculateTotalCostsAndCalculatedSalePrice(articleJdo);
                    }
                    if (field.getName().equals("multiplier") || field.getName().equals("totalCostUAH")) {
                        articleCalculator.calculateCalculatedSalePrice(articleJdo);
                    }
                    if (field.getName().equals("salePrice") && queryName == null) {
                        articleCalculator.calculateMultiplier(articleJdo);
                    }
                } else return;
            } else if (char.class == field.getType()) {
                char typeValue = value.charAt(0);
                if (typeValue != field.getChar(articleJdo)) {
                    field.setChar(articleJdo, typeValue);
                } else return;
            } else if (long.class == field.getType()) {
                long typeValue = Long.parseLong(value);
                if (typeValue != field.getLong(articleJdo)) {
                    field.setLong(articleJdo, typeValue);
                } else return;
            } else if (Integer.class == field.getType()) {
                Integer typeValue;
                if (value.isEmpty()) {
                    typeValue = 0;
                } else {
                    typeValue = Integer.parseInt(value);
                }
                if (!typeValue.equals(field.get(articleJdo))) {
                    field.set(articleJdo, typeValue);
                    if (field.getName().equals("quantity") && typeValue > 1) {
                        field.set(articleJdo, 1);
                        int quantity = typeValue;
                        for (int i = 1; i < quantity; ++i) {
                            ArticleJdo newArticle = (ArticleJdo) articleJdo.clone();
                            newArticle.setSpreadsheetRow(0);
                            newArticle.setId(0);
                            tableData.add(newArticle);
                            updateTable(oldArticle, newArticle);
                        }
                    }
                } else return;
            } else if (Boolean.class == field.getType()) {
                Boolean typeValue = Boolean.parseBoolean(value);
                if (field.getName().equals("selected")) {
                    toUpdate = false;
                }
                if (!typeValue.equals(field.get(articleJdo))) {
                    field.set(articleJdo, typeValue);
                } else return;
            } else if (Double.class == field.getType()) {
                Double typeValue;
                if (value.isEmpty()) {
                    typeValue = 0.0;
                } else {
                    typeValue = articleCalculator.fixIfNeedAndParseDouble(value);
                    typeValue = BigDecimal.valueOf(typeValue).setScale(2, BigDecimal.ROUND_HALF_DOWN).doubleValue();
                }
                if (!typeValue.equals(field.get(articleJdo))) {
                    field.set(articleJdo, typeValue);
                    if (field.getName().equals("transportCostEUR") || field.getName().equals("purchasePriceEUR")) {
                        articleCalculator.calculateTotalCostsAndCalculatedSalePrice(articleJdo);
                    }
                    if (field.getName().equals("multiplier") || field.getName().equals("totalCostUAH")) {
                        articleCalculator.calculateCalculatedSalePrice(articleJdo);
                    }
                    if (field.getName().equals("salePrice") && queryName == null) {
                        articleCalculator.calculateMultiplier(articleJdo);
                    }
                } else return;
            } else if (Character.class == field.getType()) {
                Character typeValue = value.charAt(0);
                if (!typeValue.equals(field.get(articleJdo))) {
                    field.set(articleJdo, typeValue);
                } else return;
            } else if (Long.class == field.getType()) {
                Long typeValue = Long.parseLong(value);
                if (!typeValue.equals(field.get(articleJdo))) {
                    field.set(articleJdo, typeValue);
                } else return;
            } else if (Calendar.class == field.getType()) {
                Calendar typeValue = Calendar.getInstance();
                if (!value.isEmpty()) {
                    String formattedValue = value.replace(",", ".").trim();
                    if (formattedValue.matches("\\d{2}.\\d{2}.\\d{4}")) {
                        Date time;
                        try {
                            time = calendarFormatter.parse(formattedValue);
                        } catch (ParseException e) {
                            logger.warn(e.getMessage(), e);
                            return;
                        }
                        typeValue.setTime(time);
                        if (!typeValue.equals(field.get(articleJdo))) {
                            field.set(articleJdo, typeValue);
                        } else return;
                    }
                } else {
                    if (field.get(articleJdo) != null) {
                        field.set(articleJdo, null);
                    } else return;
                }
            } else if (Date.class == field.getType()) {
                if (!value.isEmpty()) {
                    String formattedValue = value.replace(",", ".").trim();
                    if (formattedValue.matches("\\d{2}.\\d{2}.\\d{4} \\d{2}:\\d{2}:\\d{2}")) {
                        Date typeValue;
                        try {
                            typeValue = DateConverter.convertStringToDate(formattedValue);
                        } catch (ParseException e) {
                            logger.warn(e.getMessage(), e);
                            return;
                        }
                        if (!typeValue.equals(field.get(articleJdo))) {
                            field.set(articleJdo, typeValue);
                        } else return;
                    }
                } else {
                    if (field.get(articleJdo) != null) {
                        field.set(articleJdo, null);
                    } else return;
                }
            } else {
                Object fieldValue = field.get(articleJdo) != null ? field.get(articleJdo) : "";
                if (!value.equals(fieldValue)) {
                    field.set(articleJdo, value);
                } else return;
            }
        } catch (NumberFormatException e) {
            throw new NumberFormatException(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        if (toUpdate) {
            updateTable(oldArticle, articleJdo);
        }
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
        Class columnClass;
        try {
            Field field = ArticleJdo.class.getDeclaredField(articleFieldsSequence.get(columnIndex));
            field.setAccessible(true);
            if (int.class == field.getType()) {
                columnClass = Integer.class;
            } else if (boolean.class == field.getType()) {
                columnClass = Boolean.class;
            } else if (double.class == field.getType()) {
                columnClass = Double.class;
            } else if (char.class == field.getType()) {
                columnClass = Character.class;
            } else if (long.class == field.getType()) {
                columnClass = Long.class;
            } else {
                columnClass = field.getType();
            }
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }

        return columnClass;
//        return Object.class;
    }

    /**
     * Updates table and remote spreadsheet with the changed article
     *
     * @param changedArticle the articleJdo to be updated.
     */
    private void updateTable(final ArticleJdo oldArticle, final ArticleJdo changedArticle) {
        if (queryName != null) {
            concurrentOperationsService.startOperation("Article updating.", new Runnable() {

                @Override
                public void run() {
                    try {
                        articleServiceSwingWrapper.updateToSpreadsheet(oldArticle, changedArticle, null);
                    } catch (RemoteUpdateException | LavidaGoogleException e) {
                        fireTableDataChanged();
                        if (e instanceof LavidaGoogleException) {
                            throw new LavidaSwingRuntimeException(((LavidaGoogleException) e).getErrorCode(), e);
                        } else
                            throw new LavidaSwingRuntimeException(LavidaSwingRuntimeException.GOOGLE_SERVICE_EXCEPTION, e);
                    }
                }
            });
        }
        fireTableDataChanged();
    }

    /**
     * Changes the ArticlesTableModel queryName according to the user's first role.
     *
     * @param userRoles the list of user's roles.
     */
    public void filterTableDataByRole(List<String> userRoles) {
        if (ArticleJdo.FIND_NOT_SOLD.equals(queryName)) {
            for (String role : userRoles) {
                switch (role) {
                    case "ROLE_SELLER_LA_VIDA": {
                        this.setQueryName(ArticleJdo.FIND_NOT_SOLD_LA_VIDA);
                        updateTableData();
                        fireTableDataChanged();
                        return;
                    }
                    case "ROLE_SELLER_SLAVYANKA": {
                        this.setQueryName(ArticleJdo.FIND_NOT_SOLD_SLAVYANKA);
                        updateTableData();
                        fireTableDataChanged();
                        return;
                    }
                    case "ROLE_SELLER_NOVOMOSKOVSK": {
                        this.setQueryName(ArticleJdo.FIND_NOT_SOLD_NOVOMOSKOVSK);
                        updateTableData();
                        fireTableDataChanged();
                        return;
                    }
                    case "ROLE_SELLER_ALEXANDRIA": {
                        this.setQueryName(ArticleJdo.FIND_NOT_SOLD_ALEXANDRIA);
                        updateTableData();
                        fireTableDataChanged();
                        return;
                    }
                }
            }
        } else if (ArticleJdo.FIND_SOLD.equals(queryName)) {
            for (String role : userRoles) {
                switch (role) {
                    case "ROLE_SELLER_LA_VIDA": {
                        this.setQueryName(ArticleJdo.FIND_SOLD_LA_VIDA);
                        updateTableData();
                        fireTableDataChanged();
                        return;
                    }
                    case "ROLE_SELLER_SLAVYANKA": {
                        this.setQueryName(ArticleJdo.FIND_SOLD_SLAVYANKA);
                        updateTableData();
                        fireTableDataChanged();
                        return;
                    }
                    case "ROLE_SELLER_NOVOMOSKOVSK": {
                        this.setQueryName(ArticleJdo.FIND_SOLD_NOVOMOSKOVSK);
                        updateTableData();
                        fireTableDataChanged();
                        return;
                    }
                    case "ROLE_SELLER_ALEXANDRIA": {
                        this.setQueryName(ArticleJdo.FIND_SOLD_ALEXANDRIA);
                        updateTableData();
                        fireTableDataChanged();
                        return;
                    }
                }
/*
                if ("ROLE_SELLER_LA_VIDA".equals(role)) {
                    this.setQueryName(ArticleJdo.FIND_SOLD_LA_VIDA);
                    updateTableData();
                    fireTableDataChanged();
                    return;
                } else if ("ROLE_SELLER_SLAVYANKA".equals(role)) {
                    this.setQueryName(ArticleJdo.FIND_SOLD_SLAVYANKA);
                    updateTableData();
                    fireTableDataChanged();
                    return;
                } else if ("ROLE_SELLER_NOVOMOSKOVSK".equals(role)) {
                    this.setQueryName(ArticleJdo.FIND_SOLD_NOVOMOSKOVSK);
                    updateTableData();
                    fireTableDataChanged();
                    return;
                }
*/
            }
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

    public double getTotalCostEUR() {
        return totalCostEUR;
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

//    public List<String> getHeaderTitles() {
//        return headerTitles;
//    }

    public double getTotalPurchaseCostEUR() {
        return totalPurchaseCostEUR;
    }

    public double getTotalCostUAH() {
        return totalCostUAH;
    }

    public double getMinimalMultiplier() {
        return minimalMultiplier;
    }

    public double getNormalMultiplier() {
        return normalMultiplier;
    }

    public double getTotalTransportCostEUR() {
        return totalTransportCostEUR;
    }

    public double getProfitUAH() {
        return profitUAH;
    }

    public String getQueryName() {
        return queryName;
    }

//    public File getOpenedFile() {
//        return openedFile;
//    }

//    public void setOpenedFile(File openedFile) {
//        this.openedFile = openedFile;
//    }


    public BrandService getBrandService() {
        return brandService;
    }

    public SizeService getSizeService() {
        return sizeService;
    }

    public ShopService getShopService() {
        return shopService;
    }

    public List<String> getHeaderTitles() {
        return headerTitles;
    }
}
