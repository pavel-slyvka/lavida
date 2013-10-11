package com.lavida.swing.service;

import com.lavida.service.FiltersPurpose;
import com.lavida.service.ViewColumn;
import com.lavida.service.entity.ChangedFieldJdo;
import com.lavida.service.utils.DateConverter;
import com.lavida.swing.LocaleHolder;
import com.lavida.swing.event.ChangedFieldEvent;
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
 * The ChangedFieldTableModel
 * <p/>
 * Created: 01.10.13 21:36.
 *
 * @author Ruslan.
 */
public class ChangedFieldTableModel extends AbstractTableModel implements ApplicationListener<ChangedFieldEvent> {
    private static final Logger logger = LoggerFactory.getLogger(ChangedFieldTableModel.class);

    private List<String> headerTitles = new ArrayList<>();
    private List<String> fieldsSequence;
    private Map<Integer, SimpleDateFormat> columnIndexToDateFormat;
    private String queryName;
    private List<ChangedFieldJdo> tableData;
    private ChangedFieldJdo selectedChangedField;

    @Resource
    private ChangedFieldServiceSwingWrapper changedFieldServiceSwingWrapper;

    @Resource
    private MessageSource messageSource;

    @Resource
    private LocaleHolder localeHolder;

    @Resource
    private ConcurrentOperationsService concurrentOperationsService;

    @Override
    public void onApplicationEvent(ChangedFieldEvent event) {
        if (queryName != null) {
            tableData = changedFieldServiceSwingWrapper.get(queryName);
        }
        fireTableDataChanged();
    }

    public FiltersPurpose getFiltersPurpose() {
        switch (queryName) {
            case  ChangedFieldJdo.FIND_POSTPONED :
                return FiltersPurpose.POSTPONED_CHANGED_FIELDS;

            default:
                return FiltersPurpose.REFRESHED_CHANGED_FIELDS;
        }
    }

        public List<ChangedFieldJdo> getTableData() {
        if (tableData == null && queryName != null) {
            tableData = changedFieldServiceSwingWrapper.get(queryName);
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

    public ChangedFieldJdo getChangedFieldJdoByRowIndex(int rowIndex) {
        return getTableData().get(rowIndex);
    }

    public Object getRawValueAt(int rowIndex, int columnIndex) {
        ChangedFieldJdo changedFieldJdo = getChangedFieldJdoByRowIndex(rowIndex);
        try {
            Field field = ChangedFieldJdo.class.getDeclaredField(fieldsSequence.get(columnIndex));
            field.setAccessible(true);
            return field.get(changedFieldJdo);

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
            this.tableData = changedFieldServiceSwingWrapper.clearAndReturnForCurrentDay();
        } else {
            this.tableData = new ArrayList<>();
        }
        for (Field field : ChangedFieldJdo.class.getDeclaredFields()) {
            ViewColumn viewColumn = field.getAnnotation(ViewColumn.class);
            if (viewColumn != null && viewColumn.show()) {
                field.setAccessible(true);
                if (ChangedFieldJdo.FIND_REFRESHED.equals(queryName) &&
                        !(viewColumn.titleKey().equals("dialog.changed.field.article.table.postponedDate.title"))) {
                    this.fieldsSequence.add(field.getName());
                    if (viewColumn.titleKey().isEmpty()) {
                        this.headerTitles.add(field.getName());
                    } else {
                        this.headerTitles.add(messageSource.getMessage(viewColumn.titleKey(), null, localeHolder.getLocale()));
                    }
                } else if (ChangedFieldJdo.FIND_POSTPONED.equals(queryName) &&
                        !(viewColumn.titleKey().equals("dialog.changed.field.article.table.refreshOperationType.title"))) {
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
            for (Field field : ChangedFieldJdo.class.getDeclaredFields()) {
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
        ChangedFieldJdo changedFieldJdo = getChangedFieldJdoByRowIndex(rowIndex);
        String value = aValue.toString();
        SimpleDateFormat calendarFormatter = new SimpleDateFormat("dd.MM.yyyy");
        calendarFormatter.setLenient(false);
        boolean toUpdate = true;
        try {
            Field field = ChangedFieldJdo.class.getDeclaredField(fieldsSequence.get(columnIndex));
            field.setAccessible(true);
            if (int.class == field.getType()) {
                int typeValue;
                if (value.isEmpty()) {
                    typeValue = 0;
                } else {
                    typeValue = Integer.parseInt(value);
                }
                if (typeValue != field.getInt(changedFieldJdo)) {
                    field.setInt(changedFieldJdo, typeValue);
                } else return;
            } else if (boolean.class == field.getType()) {
                boolean typeValue = Boolean.parseBoolean(value);
                if (field.getName().equals("selected")) {
                    toUpdate = false;
                }
                if (typeValue != field.getBoolean(changedFieldJdo)) {
                    field.setBoolean(changedFieldJdo, typeValue);
                } else return;
            } else if (double.class == field.getType()) {
                double typeValue;
                if (value.isEmpty()) {
                    typeValue = 0.0;
                } else {
                    typeValue = fixIfNeedAndParseDouble(value);
                    typeValue = BigDecimal.valueOf(typeValue).setScale(2, BigDecimal.ROUND_HALF_DOWN).doubleValue();
                }
                if (typeValue != field.getDouble(changedFieldJdo)) {
                        field.setDouble(changedFieldJdo, typeValue);
                } else return;
            } else if (char.class == field.getType()) {
                char typeValue = value.charAt(0);
                if (typeValue != field.getChar(changedFieldJdo)) {
                    field.setChar(changedFieldJdo, typeValue);
                } else return;
            } else if (long.class == field.getType()) {
                long typeValue = Long.parseLong(value);
                if (typeValue != field.getLong(changedFieldJdo)) {
                    field.setLong(changedFieldJdo, typeValue);
                } else return;
            } else if (Integer.class == field.getType()) {
                Integer typeValue;
                if (value.isEmpty()) {
                    typeValue = 0;
                } else {
                    typeValue = Integer.parseInt(value);
                }
                if (!typeValue.equals(field.get(changedFieldJdo))) {
                    field.set(changedFieldJdo, typeValue);
                } else return;
            } else if (Boolean.class == field.getType()) {
                Boolean typeValue = Boolean.parseBoolean(value);
                if (field.getName().equals("selected")) {
                    toUpdate = false;
                }
                if (typeValue != field.getBoolean(changedFieldJdo)) {
                    field.setBoolean(changedFieldJdo, typeValue);
                } else return;
            } else if (Double.class == field.getType()) {
                Double typeValue;
                if (value.isEmpty()) {
                    typeValue = 0.0;
                } else {
                    typeValue = fixIfNeedAndParseDouble(value);
                    typeValue = BigDecimal.valueOf(typeValue).setScale(2, BigDecimal.ROUND_HALF_DOWN).doubleValue();
                }
                if (!typeValue.equals(field.get(changedFieldJdo))) {
                        field.setDouble(changedFieldJdo, typeValue);
                } else return;
            } else if (Character.class == field.getType()) {
                Character typeValue = value.charAt(0);
                if (!typeValue.equals(field.get(changedFieldJdo))) {
                    field.set(changedFieldJdo, typeValue);
                } else return;
            } else if (Long.class == field.getType()) {
                Long typeValue = Long.parseLong(value);
                if (!typeValue.equals(field.get(changedFieldJdo))) {
                    field.set(changedFieldJdo, typeValue);
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
                        if (!typeValue.equals(field.get(changedFieldJdo))) {
                            field.set(changedFieldJdo, typeValue);
                        } else return;
                    }
                } else {
                    if (field.get(changedFieldJdo) != null) {
                        field.set(changedFieldJdo, null);
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
                        if (!typeValue.equals(field.get(changedFieldJdo))) {
                            field.set(changedFieldJdo, typeValue);
                        } else return;
                    }
                } else {
                    if (field.get(changedFieldJdo) != null) {
                        field.set(changedFieldJdo, null);
                    } else return;
                }
            } else {
                if (!value.equals(field.get(changedFieldJdo))) {
                    field.set(changedFieldJdo, value);
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
            updateTable(changedFieldJdo);
        }
    }

    private double fixIfNeedAndParseDouble(String doubleString) {
        if (doubleString == null || doubleString.trim().isEmpty()) {
            return 0;
        }
        return Double.parseDouble(doubleString.replace(" ", "").replace(",", "."));
    }

    /**
     * Updates table with the changedFieldJdo.
     *
     * @param changedFieldJdo the changedFieldJdo to be updated.
     */
    private void updateTable(final ChangedFieldJdo changedFieldJdo) {
        if (queryName != null) {

        concurrentOperationsService.startOperation("Field changing.", new Runnable() {
            @Override
            public void run() {
                    changedFieldServiceSwingWrapper.update(changedFieldJdo);
                    tableData = changedFieldServiceSwingWrapper.get(queryName);
                }
        });
            fireTableDataChanged();

        }
    }


    public void setQueryName(String queryName) {
        this.queryName = queryName;
    }

//    public String getQueryName() {
//        return queryName;
//    }
//
    public ChangedFieldJdo getSelectedChangedField() {
        return selectedChangedField;
    }

    public void setSelectedChangedField(ChangedFieldJdo selectedChangedField) {
        this.selectedChangedField = selectedChangedField;
    }

}
