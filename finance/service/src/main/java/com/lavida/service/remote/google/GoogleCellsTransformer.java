package com.lavida.service.remote.google;

import com.google.gdata.data.spreadsheet.Cell;
import com.google.gdata.data.spreadsheet.CellEntry;
import com.google.gdata.data.spreadsheet.CellFeed;
import com.lavida.service.entity.ArticleJdo;
import com.lavida.service.remote.SpreadsheetColumn;
import com.lavida.service.utils.CalendarConverter;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * GoogleCellsTransformer
 * Created: 22:15 15.08.13
 *
 * @author Pavel
 */
@Component
public class GoogleCellsTransformer {

    public Map<Integer, String> cellsToColNumsAndItsValueMap(List<Cell> cells) {
        Map<Integer, String> headers = new HashMap<Integer, String>(cells.size());
        for (Cell cell : cells) {
            headers.put(cell.getCol(), cell.getValue());
        }
        return headers;
    }

    public ArticleJdo cellsToArticleJdo(List<Cell> cells, Map<Integer, String> headers) {
        ArticleJdo articleJdo = new ArticleJdo();
        if (!cells.isEmpty()) {
            articleJdo.setSpreadsheetRow(cells.get(0).getRow());
        }
        for (Cell cell : cells) {
            if (headers.containsKey(cell.getCol())) {
                String header = headers.get(cell.getCol());
                try {
                    for (java.lang.reflect.Field field : ArticleJdo.class.getDeclaredFields()) {
                        SpreadsheetColumn spreadsheetColumn = field.getAnnotation(SpreadsheetColumn.class);
                        if (spreadsheetColumn != null && header.equals(spreadsheetColumn.sheetColumn())) {
                            String value = cell.getValue();
                            field.setAccessible(true);
                            if (int.class == field.getType()) {
                                field.setInt(articleJdo, Integer.parseInt(value));
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
                                field.set(articleJdo, CalendarConverter.convertStringToCalendar(value));
                            } else {
                                field.set(articleJdo, value);
                            }
                            break;
                        }
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();    // todo log the error.
                }
            }
        }
        return articleJdo;
    }

    public String cellToValue(CellFeed cellFeed, Map<Integer, String> googleHeadersMap, String googleHeader) {
        Integer cellColumn = null;
        for (Map.Entry<Integer, String> googleHeadersMapEntry : googleHeadersMap.entrySet()) {
            if (googleHeadersMapEntry.getValue() != null && googleHeadersMapEntry.getValue().equals(googleHeader)) {
                cellColumn = googleHeadersMapEntry.getKey();
                break;
            }
        }
        if (cellColumn != null) {
            List<CellEntry> cellEntries = cellFeed.getEntries();
            for (CellEntry cellEntry : cellEntries) {
                if (cellEntry.getCell().getCol() == cellColumn) {
                    return cellEntry.getCell().getValue();
                }
            }
        }
        return null;
    }

    private double fixIfNeedAndParseDouble(String doubleString) {
        return Double.parseDouble(doubleString.replace(" ", "").replace(",", "."));
    }

    public List<CellEntry> articleToCellEntriesForUpdate(ArticleJdo articleJdo, Map<Integer, String> headers, CellFeed oldArticleFeed) {
        try {
            List<CellEntry> cellEntriesForUpdate = new ArrayList<CellEntry>();
            List<CellEntry> oldCellEntries = oldArticleFeed.getEntries();
            for (CellEntry oldCellEntry : oldCellEntries) {
                String googleHeader = headers.get(oldCellEntry.getCell().getCol());

                for (Field field : ArticleJdo.class.getDeclaredFields()) {
                    SpreadsheetColumn spreadsheetColumn = field.getAnnotation(SpreadsheetColumn.class);
                    if (spreadsheetColumn != null && spreadsheetColumn.sheetColumn().equals(googleHeader)) {
                        field.setAccessible(true);
                        String oldCellValue = oldCellEntry.getCell().getValue();
                        String newCellValue = articleFieldValueToString(field.get(articleJdo), spreadsheetColumn);
                        if (!oldCellValue.equals(newCellValue)) {
                            cellEntriesForUpdate.add(new CellEntry(
                                    oldCellEntry.getCell().getRow(), oldCellEntry.getCell().getCol(), newCellValue));
                        }
                    }
                }
            }
            return cellEntriesForUpdate;

        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private String articleFieldValueToString(Object fieldValue, SpreadsheetColumn spreadsheetColumn) {
        if (fieldValue instanceof Calendar) {
            return new SimpleDateFormat(spreadsheetColumn.sheetDatePattern()).format(((Calendar) fieldValue).getTime());

        } else if (fieldValue instanceof Double) {
            String value = fieldValue.toString();
            if (value.endsWith(".0")) {
                return value.substring(0, value.length() - 2);
            }
        }
        return fieldValue.toString();
    }
}
