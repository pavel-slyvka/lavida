package com.lavida.swing.service;

import com.lavida.service.FiltersPurpose;
import com.lavida.service.UserService;
import com.lavida.service.ViewColumn;
import com.lavida.service.entity.DiscountCardJdo;
import com.lavida.service.remote.google.LavidaGoogleException;
import com.lavida.service.utils.DateConverter;
import com.lavida.swing.LocaleHolder;
import com.lavida.swing.event.DiscountCardUpdateEvent;
import com.lavida.swing.exception.LavidaSwingRuntimeException;
import com.lavida.swing.exception.RemoteUpdateException;
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
 * The table model for the DiscountCardsTableComponent.
 * Created: 12:00 06.09.13
 *
 * @author Ruslan
 */
public class DiscountCardsTableModel extends AbstractTableModel implements ApplicationListener<DiscountCardUpdateEvent> {
//    private static final Logger logger = LoggerFactory.getLogger(DiscountCardsTableModel.class);

    public List<String> headerTitles;
    private List<String> fieldsSequence;
    private Map<Integer, SimpleDateFormat> columnIndexToDateFormat;
    private List<DiscountCardJdo> tableData;
    private DiscountCardJdo selectedCard;

    @Resource
    private MessageSource messageSource;

    @Resource
    private LocaleHolder localeHolder;

    @Resource
    private UserService userService;

    @Resource
    private DiscountCardServiceSwingWrapper discountCardServiceSwingWrapper;

    @Resource
    private ConcurrentOperationsService concurrentOperationsService;

    private String query;
    private int totalCountCards;
    private double totalSumUAH;


    @PostConstruct
    public void initHeaderFieldAndTitles() {
        this.fieldsSequence = new ArrayList<>();
        this.headerTitles = new ArrayList<>();
        this.columnIndexToDateFormat = new HashMap<>();
        if (query != null) {
            this.tableData = discountCardServiceSwingWrapper.get(query);
        } else {
            this.tableData = new ArrayList<>();
        }
        for (Field field : DiscountCardJdo.class.getDeclaredFields()) {
            ViewColumn viewColumn = field.getAnnotation(ViewColumn.class);
            if (viewColumn != null) {
                field.setAccessible(true);
                if (DiscountCardJdo.FIND_ALL.equals(query)) {
                    this.fieldsSequence.add(field.getName());
                    if (viewColumn.titleKey().isEmpty()) {
                        this.headerTitles.add(field.getName());
                    } else {
                        this.headerTitles.add(messageSource.getMessage(viewColumn.titleKey(), null, localeHolder.getLocale()));
                    }
                } else if (query == null &&
                        !(viewColumn.titleKey().equals("dialog.discounts.card.all.column.title.postponedDate") ||
                                viewColumn.titleKey().equals("dialog.discounts.card.all.column.title.activationDate") ||
                                viewColumn.titleKey().equals("dialog.discounts.card.all.column.title.registrationDate") ||
                                viewColumn.titleKey().equals("dialog.discounts.card.all.column.title.bonusUAH") ||
                                viewColumn.titleKey().equals("dialog.discounts.card.all.column.title.discountRate") ||
                                viewColumn.titleKey().equals("dialog.discounts.card.all.column.title.sumTotalUAH"))
                        ) {
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

/*
    public List<String> getForbiddenHeadersToShow(MessageSource messageSource, Locale locale, List<String> userRoles) {
        List<String> forbiddenHeaders = new ArrayList<>();
        for (Field field : DiscountCardJdo.class.getDeclaredFields()) {
            ViewColumn viewColumn = field.getAnnotation(ViewColumn.class);
            if (viewColumn != null && viewColumn.show()) {
                if (DiscountCardJdo.FIND_ALL.equals(query)) {
                    if (!viewColumn.show() || isForbidden(userRoles, viewColumn.forbiddenRoles())) {
                        forbiddenHeaders.add(viewColumn.titleKey().isEmpty() ? field.getName()
                                : messageSource.getMessage(viewColumn.titleKey(), null, locale));
                    }
                } else if (query == null &&
                        !(viewColumn.titleKey().equals("dialog.discounts.card.all.column.title.postponedDate") ||
                                viewColumn.titleKey().equals("dialog.discounts.card.all.column.title.activationDate") ||
                                viewColumn.titleKey().equals("dialog.discounts.card.all.column.title.registrationDate") ||
                                viewColumn.titleKey().equals("dialog.discounts.card.all.column.title.bonusUAH") ||
                                viewColumn.titleKey().equals("dialog.discounts.card.all.column.title.discountRate") ||
                                viewColumn.titleKey().equals("dialog.discounts.card.all.column.title.sumTotalUAH"))
                        ) {
                    if (!viewColumn.show() || isForbidden(userRoles, viewColumn.forbiddenRoles())) {
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
*/

    public FiltersPurpose getFiltersPurpose() {
        return FiltersPurpose.ALL_DISCOUNT_CARDS;
    }

    public Map<String, Integer> getColumnHeaderToWidth() {
        Map<String, Integer> columnHeaderToWidth = new HashMap<>(headerTitles.size());
        label:
        for (String columnHeader : headerTitles) {
            Integer width;
            for (Field field : DiscountCardJdo.class.getDeclaredFields()) {
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

    public Object getRawValueAt(int rowIndex, int columnIndex) {
        DiscountCardJdo discountCardJdo = getDiscountCardByRowIndex(rowIndex);
        try {
            Field field = DiscountCardJdo.class.getDeclaredField(fieldsSequence.get(columnIndex));
            field.setAccessible(true);
            return field.get(discountCardJdo);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Returns true if the user has permissions.
     *
     * @param rowIndex    the row being queried
     * @param columnIndex the column being queried
     * @return true if the user has permissions.
     */
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        if (fieldsSequence.get(columnIndex).equals("sumTotalUAH") ||
                fieldsSequence.get(columnIndex).equals("registrationDate") ||
                fieldsSequence.get(columnIndex).equals("activationDate") ||
                fieldsSequence.get(columnIndex).equals("postponedDate")) {
            return false;
        } else if (fieldsSequence.get(columnIndex).equals("discountRate") ||
                fieldsSequence.get(columnIndex).equals("bonusUAH")) {
            return !userService.hasForbiddenRole();
        }
        return true;
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
            Field field = DiscountCardJdo.class.getDeclaredField(fieldsSequence.get(columnIndex));
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
     * This empty implementation is provided so users don't have to implement
     * this method if their data model is not editable.
     *
     * @param aValue      value to assign to cell
     * @param rowIndex    row of cell
     * @param columnIndex column of cell
     */
    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        DiscountCardJdo discountCardJdo = getDiscountCardByRowIndex(rowIndex);
        DiscountCardJdo oldDiscountCardJdo;
        try {
            oldDiscountCardJdo = (DiscountCardJdo) discountCardJdo.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
        String value = aValue.toString();
        SimpleDateFormat calendarFormatter = new SimpleDateFormat("dd.MM.yyyy");
        calendarFormatter.setLenient(false);
        boolean toUpdate = true;
        try {
            Field field = DiscountCardJdo.class.getDeclaredField(fieldsSequence.get(columnIndex));
            field.setAccessible(true);
            if (int.class == field.getType()) {
                int typeValue;
                if (value.isEmpty()) {
                    typeValue = 0;
                } else {
                    typeValue = Integer.parseInt(value);
                }
                if (typeValue != field.getInt(discountCardJdo)) {
                    field.setInt(discountCardJdo, typeValue);
                } else return;
            } else if (boolean.class == field.getType()) {
                boolean typeValue = Boolean.parseBoolean(value);
                if (field.getName().equals("selected")) {
                    toUpdate = false;
                }
                if (typeValue != field.getBoolean(discountCardJdo)) {
                    field.setBoolean(discountCardJdo, typeValue);
                } else return;
            } else if (double.class == field.getType()) {
                double typeValue;
                if (value.isEmpty()) {
                    typeValue = 0.0;
                } else {
                    typeValue = fixIfNeedAndParseDouble(value);
                    typeValue = BigDecimal.valueOf(typeValue).setScale(2, BigDecimal.ROUND_HALF_DOWN).doubleValue();
                }
                if (typeValue != field.getDouble(discountCardJdo)) {
                    if (field.getName().equals("discountRate")) {
                        if (typeValue > 10.0) {
                            typeValue = 10.0;
                        } else if (typeValue < 0.0) {
                            typeValue = 0.0;
                        }
                        field.setDouble(discountCardJdo, typeValue);
                    } else {
                        field.setDouble(discountCardJdo, typeValue);
                    }
                } else return;
            } else if (char.class == field.getType()) {
                char typeValue = value.charAt(0);
                if (typeValue != field.getChar(discountCardJdo)) {
                    field.setChar(discountCardJdo, typeValue);
                } else return;
            } else if (long.class == field.getType()) {
                long typeValue = Long.parseLong(value);
                if (typeValue != field.getLong(discountCardJdo)) {
                    field.setLong(discountCardJdo, typeValue);
                } else return;
            } else if (Integer.class == field.getType()) {
                Integer typeValue;
                if (value.isEmpty()) {
                    typeValue = 0;
                } else {
                    typeValue = Integer.parseInt(value);
                }
                if (!typeValue.equals(field.get(discountCardJdo))) {
                    field.set(discountCardJdo, typeValue);
                } else return;
            } else if (Boolean.class == field.getType()) {
                Boolean typeValue = Boolean.parseBoolean(value);
                if (field.getName().equals("selected")) {
                    toUpdate = false;
                }
                if (typeValue != field.getBoolean(discountCardJdo)) {
                    field.setBoolean(discountCardJdo, typeValue);
                } else return;
            } else if (Double.class == field.getType()) {
                Double typeValue;
                if (value.isEmpty()) {
                    typeValue = 0.0;
                } else {
                    typeValue = fixIfNeedAndParseDouble(value);
                    typeValue = BigDecimal.valueOf(typeValue).setScale(2, BigDecimal.ROUND_HALF_DOWN).doubleValue();
                }
                if (!typeValue.equals(field.get(discountCardJdo))) {
                    if (field.getName().equals("discountRate")) {
                        if (typeValue > 10.0) {
                            typeValue = 10.0;
                        } else if (typeValue < 0.0) {
                            typeValue = 0.0;
                        }
                        field.setDouble(discountCardJdo, typeValue);
                    } else {
                        field.setDouble(discountCardJdo, typeValue);
                    }
                } else return;
            } else if (Character.class == field.getType()) {
                Character typeValue = value.charAt(0);
                if (!typeValue.equals(field.get(discountCardJdo))) {
                    field.set(discountCardJdo, typeValue);
                } else return;
            } else if (Long.class == field.getType()) {
                Long typeValue = Long.parseLong(value);
                if (!typeValue.equals(field.get(discountCardJdo))) {
                    field.set(discountCardJdo, typeValue);
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
                            throw new LavidaSwingRuntimeException(LavidaSwingRuntimeException.DATE_FORMAT_EXCEPTION, e);
                        }
                        typeValue.setTime(time);
                        if (!typeValue.equals(field.get(discountCardJdo))) {
                            field.set(discountCardJdo, typeValue);
                        } else return;
                    }
                } else {
                    if (field.get(discountCardJdo) != null) {
                        field.set(discountCardJdo, null);
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
                            throw new LavidaSwingRuntimeException(LavidaSwingRuntimeException.DATE_FORMAT_EXCEPTION, e);
                        }
                        if (!typeValue.equals(field.get(discountCardJdo))) {
                            field.set(discountCardJdo, typeValue);
                        } else return;
                    }
                } else {
                    if (field.get(discountCardJdo) != null) {
                        field.set(discountCardJdo, null);
                    } else return;
                }
            } else {
                if (!value.equals(field.get(discountCardJdo))) {
                    field.set(discountCardJdo, value);
                } else return;
            }
        } catch (NumberFormatException e) {
            throw new NumberFormatException(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        if (toUpdate) {
            updateTable(oldDiscountCardJdo, discountCardJdo);
        }
    }

    private double fixIfNeedAndParseDouble(String doubleString) {
        if (doubleString == null || doubleString.trim().isEmpty()) {
            return 0;
        }
        return Double.parseDouble(doubleString.replace(" ", "").replace(",", "."));
    }

    /**
     * Updates table with the discountCardJdo.
     *
     * @param discountCardJdo the DiscountCardJdo to be updated.
     */
    private void updateTable(final DiscountCardJdo oldDiscountCardJdo, final DiscountCardJdo discountCardJdo) {
        if (query != null) {
            concurrentOperationsService.startOperation("Discount card update.", new Runnable() {
                @Override
                public void run() {
                    try {
                        discountCardServiceSwingWrapper.updateToSpreadsheet(oldDiscountCardJdo, discountCardJdo);
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
     * Initializes fields of the analyze component.
     */
    public void initAnalyzeFields() {
        int totalCards = 0;
        double totalSumUAH = 0;
        List<DiscountCardJdo> discountCardJdoList = getTableData();
        for (DiscountCardJdo discountCardJdo : discountCardJdoList) {
            ++totalCards;
            totalSumUAH += discountCardJdo.getSumTotalUAH();
        }
        this.totalCountCards = totalCards;
        this.totalSumUAH = totalSumUAH;
    }


    public DiscountCardJdo getDiscountCardByRowIndex(int rowIndex) {
        return getTableData().get(rowIndex);
    }

    public List<DiscountCardJdo> getTableData() {
        if (tableData == null && query != null) {
            tableData = discountCardServiceSwingWrapper.get(query);
        }
        return tableData;
    }

    public void setTableData(List<DiscountCardJdo> tableData) {
        this.tableData = tableData;
    }

    public DiscountCardJdo getSelectedCard() {
        return selectedCard;
    }

    public void setSelectedCard(DiscountCardJdo selectedCard) {
        this.selectedCard = selectedCard;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getQuery() {
        return query;
    }

    public int getTotalCountCards() {
        return totalCountCards;
    }

    public double getTotalSumUAH() {
        return totalSumUAH;
    }

    @Override
    public void onApplicationEvent(DiscountCardUpdateEvent event) {
        if (query != null) {
            tableData = discountCardServiceSwingWrapper.get(query);
            fireTableDataChanged();
        }
    }

    public List<String> getHeaderTitles() {
        return headerTitles;
    }
}
