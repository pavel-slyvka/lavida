package com.lavida.swing.service;

import com.lavida.service.ViewColumn;
import com.lavida.service.entity.ArticleChangedFieldJdo;
import com.lavida.service.entity.ArticleJdo;
import com.lavida.service.utils.DateConverter;
import com.lavida.swing.LocaleHolder;
import com.lavida.swing.event.ArticleChangedFieldEvent;
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

/**
 * The ArticleChangedFieldTableModel
 * <p/>
 * Created: 01.10.13 21:36.
 *
 * @author Ruslan.
 */
public class ArticleChangedFieldTableModel extends AbstractTableModel implements ApplicationListener<ArticleChangedFieldEvent> {
    private static final Logger logger = LoggerFactory.getLogger(ArticleChangedFieldTableModel.class);

    private List<String> headerTitles = new ArrayList<>();
    private List<String> fieldsSequence;
    private Map<Integer, SimpleDateFormat> columnIndexToDateFormat;
    private String queryName;
    private List<ArticleChangedFieldJdo> tableData;
    private ArticleChangedFieldJdo selectedArticleChangedField;

    @Resource
    private ArticleChangedFieldServiceSwingWrapper articleChangedFieldServiceSwingWrapper;

    @Resource
    private MessageSource messageSource;

    @Resource
    private LocaleHolder localeHolder;

    @Resource
    private ConcurrentOperationsService concurrentOperationsService;

    @Override
    public void onApplicationEvent(ArticleChangedFieldEvent event) {
        if (queryName != null) {
            tableData = articleChangedFieldServiceSwingWrapper.get(queryName);
        }
        fireTableDataChanged();
    }

    public List<ArticleChangedFieldJdo> getTableData() {
        if (tableData == null && queryName != null) {
            tableData = articleChangedFieldServiceSwingWrapper.get(queryName);
        }
        return tableData;
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

    public ArticleChangedFieldJdo getArticleChangedFieldJdoByRowIndex(int rowIndex) {
        return getTableData().get(rowIndex);
    }

    public Object getRawValueAt(int rowIndex, int columnIndex) {
        ArticleChangedFieldJdo articleChangedFieldJdo = getArticleChangedFieldJdoByRowIndex(rowIndex);
        try {
            Field field = ArticleChangedFieldJdo.class.getDeclaredField(fieldsSequence.get(columnIndex));
            field.setAccessible(true);
            return field.get(articleChangedFieldJdo);

        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    @PostConstruct
    public void initHeaderFieldAndTitles() {
        this.fieldsSequence = new ArrayList<>();
        this.headerTitles = new ArrayList<>();
        this.columnIndexToDateFormat = new HashMap<>();
        if (queryName != null) {
            this.tableData = articleChangedFieldServiceSwingWrapper.clearAndReturnForCurrentDay();
        } else {
            this.tableData = new ArrayList<>();
        }
        for (Field field : ArticleChangedFieldJdo.class.getDeclaredFields()) {
            ViewColumn viewColumn = field.getAnnotation(ViewColumn.class);
            if (viewColumn != null && viewColumn.show()) {
                field.setAccessible(true);
                if (ArticleChangedFieldJdo.FIND_All.equals(queryName)) {
                    this.fieldsSequence.add(field.getName());
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

    public Map<String, Integer> getColumnHeaderToWidth() {
        Map<String, Integer> columnHeaderToWidth = new HashMap<>(headerTitles.size());
        label:
        for (String columnHeader : headerTitles) {
            Integer width;
            for (Field field : ArticleChangedFieldJdo.class.getDeclaredFields()) {
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
        return !(fieldsSequence.get(columnIndex).equals("operationDate") ||
                fieldsSequence.get(columnIndex).equals("code") ||
                fieldsSequence.get(columnIndex).equals("size") ||
                fieldsSequence.get(columnIndex).equals("fieldName") ||
                fieldsSequence.get(columnIndex).equals("operationType")
        );
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
        return Object.class;
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
        ArticleChangedFieldJdo articleChangedFieldJdo = getArticleChangedFieldJdoByRowIndex(rowIndex);
        String value = aValue.toString();
        SimpleDateFormat calendarFormatter = new SimpleDateFormat("dd.MM.yyyy");
        calendarFormatter.setLenient(false);
        boolean toUpdate = true;
        try {
            Field field = ArticleChangedFieldJdo.class.getDeclaredField(fieldsSequence.get(columnIndex));
            field.setAccessible(true);
            if (int.class == field.getType()) {
                int typeValue;
                if (value.isEmpty()) {
                    typeValue = 0;
                } else {
                    typeValue = Integer.parseInt(value);
                }
                if (typeValue != field.getInt(articleChangedFieldJdo)) {
                    field.setInt(articleChangedFieldJdo, typeValue);
                } else return;
            } else if (boolean.class == field.getType()) {
                boolean typeValue = Boolean.parseBoolean(value);
                if (field.getName().equals("selected")) {
                    toUpdate = false;
                }
                if (typeValue != field.getBoolean(articleChangedFieldJdo)) {
                    field.setBoolean(articleChangedFieldJdo, typeValue);
                } else return;
            } else if (double.class == field.getType()) {
                double typeValue;
                if (value.isEmpty()) {
                    typeValue = 0.0;
                } else {
                    typeValue = fixIfNeedAndParseDouble(value);
                    typeValue = BigDecimal.valueOf(typeValue).setScale(2, BigDecimal.ROUND_HALF_DOWN).doubleValue();
                }
                if (typeValue != field.getDouble(articleChangedFieldJdo)) {
                        field.setDouble(articleChangedFieldJdo, typeValue);
                } else return;
            } else if (char.class == field.getType()) {
                char typeValue = value.charAt(0);
                if (typeValue != field.getChar(articleChangedFieldJdo)) {
                    field.setChar(articleChangedFieldJdo, typeValue);
                } else return;
            } else if (long.class == field.getType()) {
                long typeValue = Long.parseLong(value);
                if (typeValue != field.getLong(articleChangedFieldJdo)) {
                    field.setLong(articleChangedFieldJdo, typeValue);
                } else return;
            } else if (Integer.class == field.getType()) {
                Integer typeValue;
                if (value.isEmpty()) {
                    typeValue = 0;
                } else {
                    typeValue = Integer.parseInt(value);
                }
                if (!typeValue.equals(field.get(articleChangedFieldJdo))) {
                    field.set(articleChangedFieldJdo, typeValue);
                } else return;
            } else if (Boolean.class == field.getType()) {
                Boolean typeValue = Boolean.parseBoolean(value);
                if (field.getName().equals("selected")) {
                    toUpdate = false;
                }
                if (typeValue != field.getBoolean(articleChangedFieldJdo)) {
                    field.setBoolean(articleChangedFieldJdo, typeValue);
                } else return;
            } else if (Double.class == field.getType()) {
                Double typeValue;
                if (value.isEmpty()) {
                    typeValue = 0.0;
                } else {
                    typeValue = fixIfNeedAndParseDouble(value);
                    typeValue = BigDecimal.valueOf(typeValue).setScale(2, BigDecimal.ROUND_HALF_DOWN).doubleValue();
                }
                if (!typeValue.equals(field.get(articleChangedFieldJdo))) {
                        field.setDouble(articleChangedFieldJdo, typeValue);
                } else return;
            } else if (Character.class == field.getType()) {
                Character typeValue = value.charAt(0);
                if (!typeValue.equals(field.get(articleChangedFieldJdo))) {
                    field.set(articleChangedFieldJdo, typeValue);
                } else return;
            } else if (Long.class == field.getType()) {
                Long typeValue = Long.parseLong(value);
                if (!typeValue.equals(field.get(articleChangedFieldJdo))) {
                    field.set(articleChangedFieldJdo, typeValue);
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
                        if (!typeValue.equals(field.get(articleChangedFieldJdo))) {
                            field.set(articleChangedFieldJdo, typeValue);
                        } else return;
                    }
                } else {
                    if (field.get(articleChangedFieldJdo) != null) {
                        field.set(articleChangedFieldJdo, null);
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
                        if (!typeValue.equals(field.get(articleChangedFieldJdo))) {
                            field.set(articleChangedFieldJdo, typeValue);
                        } else return;
                    }
                } else {
                    if (field.get(articleChangedFieldJdo) != null) {
                        field.set(articleChangedFieldJdo, null);
                    } else return;
                }
            } else {
                if (!value.equals(field.get(articleChangedFieldJdo))) {
                    field.set(articleChangedFieldJdo, value);
                } else return;
            }
        } catch (NumberFormatException e) {
            logger.warn(e.getMessage(), e);
            throw new NumberFormatException(e.getMessage());
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
            throw new RuntimeException(e);
        }
        if (toUpdate) {
            updateTable(articleChangedFieldJdo);
        }
    }

    private double fixIfNeedAndParseDouble(String doubleString) {
        if (doubleString == null || doubleString.trim().isEmpty()) {
            return 0;
        }
        return Double.parseDouble(doubleString.replace(" ", "").replace(",", "."));
    }

    /**
     * Updates table with the articleChangedFieldJdo.
     *
     * @param articleChangedFieldJdo the articleChangedFieldJdo to be updated.
     */
    private void updateTable(final ArticleChangedFieldJdo articleChangedFieldJdo) {
        if (queryName != null) {

        concurrentOperationsService.startOperation(new Runnable() {
            @Override
            public void run() {
                    articleChangedFieldServiceSwingWrapper.update(articleChangedFieldJdo);
                    tableData = articleChangedFieldServiceSwingWrapper.get(queryName);
                }
        });
            fireTableDataChanged();

        }
    }


    public void setQueryName(String queryName) {
        this.queryName = queryName;
    }

    public String getQueryName() {
        return queryName;
    }

    public ArticleChangedFieldJdo getSelectedArticleChangedField() {
        return selectedArticleChangedField;
    }

    public void setSelectedArticleChangedField(ArticleChangedFieldJdo selectedArticleChangedField) {
        this.selectedArticleChangedField = selectedArticleChangedField;
    }
}
