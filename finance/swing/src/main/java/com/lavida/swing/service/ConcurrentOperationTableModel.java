package com.lavida.swing.service;

import com.lavida.service.ViewColumn;
import com.lavida.swing.LocaleHolder;
import com.lavida.swing.event.ConcurrentOperationCompleteEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.swing.table.AbstractTableModel;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * The ConcurrentOperationTableModel
 * <p/>
 * Created: 15.10.13 9:45.
 *
 * @author Ruslan.
 */
public class ConcurrentOperationTableModel extends AbstractTableModel implements ApplicationListener<ConcurrentOperationCompleteEvent> {

    private List<String> headerTitles = new ArrayList<>();
    private List<String> fieldsSequence;
    private Map<Integer, SimpleDateFormat> columnIndexToDateFormat;
    private List<ConcurrentOperation> tableData;
    private ConcurrentOperation selectedOperation;

    @Resource
    private ConcurrentOperationsService concurrentOperationsService;

    @Resource
    private MessageSource messageSource;

    @Resource
    private LocaleHolder localeHolder;



    public List<ConcurrentOperation> getTableData() {
        if (tableData == null) {
            tableData = concurrentOperationsService.getActiveThreads();
        }
        return tableData;
    }


    @Override
    public int getRowCount() {
        return getTableData().size();
    }

    @Override
    public int getColumnCount() {
        return getHeaderTitles().size();
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

    public ConcurrentOperation getConcurrentOperationByRowIndex(int rowIndex) {
        return getTableData().get(rowIndex);
    }

    public Object getRawValueAt(int rowIndex, int columnIndex) {
        ConcurrentOperation operation = getConcurrentOperationByRowIndex(rowIndex);
        try {
            Field field = ConcurrentOperation.class.getDeclaredField(fieldsSequence.get(columnIndex));
            field.setAccessible(true);
            return field.get(operation);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PostConstruct
    public void initHeaderFieldAndTitles() {
        this.fieldsSequence = new ArrayList<>();
        this.headerTitles = new ArrayList<>();
        this.columnIndexToDateFormat = new HashMap<>();
        this.tableData = new ArrayList<>();
        for (Field field : ConcurrentOperation.class.getDeclaredFields()) {
            ViewColumn viewColumn = field.getAnnotation(ViewColumn.class);
            if (viewColumn != null && viewColumn.show()) {
                field.setAccessible(true);
                this.fieldsSequence.add(field.getName());
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

/*
    public List<String> getForbiddenHeadersToShow(MessageSource messageSource, Locale locale, List<String> userRoles) {
        List<String> forbiddenHeaders = new ArrayList<>();
        for (Field field : ConcurrentOperation.class.getDeclaredFields()) {
            ViewColumn viewColumn = field.getAnnotation(ViewColumn.class);
            if (viewColumn != null && viewColumn.show()) {
                    if (isForbidden(userRoles, viewColumn.forbiddenRoles())) {
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
*/

    public Map<String, Integer> getColumnHeaderToWidth() {
        Map<String, Integer> columnHeaderToWidth = new HashMap<>(headerTitles.size());
        label:
        for (String columnHeader : headerTitles) {
            Integer width;
            for (Field field : ConcurrentOperation.class.getDeclaredFields()) {
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
        return false;
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


    public List<String> getHeaderTitles() {
        return headerTitles;
    }

    @Override
    public void onApplicationEvent(ConcurrentOperationCompleteEvent event) {
       updateTableData();
    }

    public void updateTableData() {
        tableData = concurrentOperationsService.getActiveThreads();
        fireTableDataChanged();
    }

//    public ConcurrentOperation getSelectedOperation() {
//        return selectedOperation;
//    }

    public void setSelectedOperation(ConcurrentOperation selectedOperation) {
        this.selectedOperation = selectedOperation;
    }
}
