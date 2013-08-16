package com.lavida.service.remote.google;

import com.google.gdata.client.spreadsheet.CellQuery;
import com.google.gdata.client.spreadsheet.FeedURLFactory;
import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.spreadsheet.CellEntry;
import com.google.gdata.data.spreadsheet.CellFeed;
import com.google.gdata.data.spreadsheet.SpreadsheetEntry;
import com.google.gdata.data.spreadsheet.SpreadsheetFeed;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;
import com.lavida.service.entity.ArticleJdo;
import com.lavida.service.remote.SpreadsheetColumn;
import com.lavida.service.settings.Settings;
import com.lavida.service.utils.CalendarConverter;
import com.lavida.service.utils.DateConverter;

import java.io.IOException;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * GoogleSpreadsheetWorker
 * Created: 22:15 15.08.13
 *
 * @author Pavel
 */
public class GoogleSpreadsheetWorker {
    private static final String APPLICATION_NAME = "LA VIDA Finance.";

    private SpreadsheetService spreadsheetService = new SpreadsheetService(APPLICATION_NAME);
    private URL worksheetUrl;

    public GoogleSpreadsheetWorker(Settings settings) throws ServiceException, IOException {
        loginToGmail(spreadsheetService, settings.getRemoteUser(), settings.getRemotePass());
        List spreadsheets = getSpreadsheetList(spreadsheetService);
        SpreadsheetEntry spreadsheet = getSpreadsheetByName(spreadsheets, settings.getSpreadsheetName());
        worksheetUrl = getWorksheetUrl(spreadsheet, settings.getWorksheetNumber());

    }

    public CellFeed getWholeDocument() throws IOException, ServiceException {
        return spreadsheetService.getFeed(worksheetUrl, CellFeed.class);
    }

    public CellFeed getCellsInRange(Integer minRow, Integer maxRow, Integer minCol, Integer maxCol) throws IOException, ServiceException {
        CellQuery query = new CellQuery(worksheetUrl);
        query.setMinimumRow(minRow);
        query.setMaximumRow(maxRow);
        query.setMinimumCol(minCol);
        query.setMaximumCol(maxCol);
        return spreadsheetService.query(query, CellFeed.class);
    }

    public void updateArticleRow(ArticleJdo articleJdo, Map<Integer, String> headers) throws IOException, ServiceException {
        int row = articleJdo.getSpreadsheetRow();
        int maxCol = getRow(row).getColCount();
        for (int i = 1; i <= maxCol; i++) {
            CellFeed cellFeed = getCellsInRange(row, row, i, i);
            CellEntry entry = cellFeed.getEntries().get(0);
            String header = headers.get(i);
            try {
                for (java.lang.reflect.Field field : ArticleJdo.class.getDeclaredFields()) {
                    SpreadsheetColumn spreadsheetColumn = field.getAnnotation(SpreadsheetColumn.class);
                    if (spreadsheetColumn != null && header.equals(spreadsheetColumn.column())) {
                        field.setAccessible(true);
                        if (int.class == field.getType()) {
                            entry.changeInputValueLocal(String.valueOf(field.getInt(articleJdo)));
                            entry.update();
                        } else if (boolean.class == field.getType()) {
                            entry.changeInputValueLocal(String.valueOf(field.getBoolean(articleJdo)));
                            entry.update();
                        } else if (double.class == field.getType()) {
                            entry.changeInputValueLocal(String.valueOf(field.getDouble(articleJdo)));
                            entry.update();
                        } else if (char.class == field.getType()) {
                            entry.changeInputValueLocal(String.valueOf(field.getChar(articleJdo)));
                            entry.update();
                        } else if (long.class == field.getType()) {
                            entry.changeInputValueLocal(String.valueOf(field.getLong(articleJdo)));
                            entry.update();
                        } else if (Integer.class == field.getType()) {
                            entry.changeInputValueLocal(String.valueOf(field.getInt(articleJdo)));
                            entry.update();
                        } else if (Boolean.class == field.getType()) {
                            entry.changeInputValueLocal(String.valueOf(field.getBoolean(articleJdo)));
                            entry.update();
                        } else if (Double.class == field.getType()) {
                            entry.changeInputValueLocal(String.valueOf(field.getDouble(articleJdo)));
                            entry.update();
                        } else if (Character.class == field.getType()) {
                            entry.changeInputValueLocal(String.valueOf(field.getChar(articleJdo)));
                            entry.update();
                        } else if (Long.class == field.getType()) {
                            entry.changeInputValueLocal(String.valueOf(field.getLong(articleJdo)));
                            entry.update();
                        } else if (Calendar.class == field.getType()) {
                            entry.changeInputValueLocal(CalendarConverter.convertCalendarToString((Calendar) field.get(articleJdo)));
                            entry.update();
                        } else if (Date.class == field.getType()) {
                            entry.changeInputValueLocal(DateConverter.convertDateToString((Date) field.get(articleJdo)));
                            entry.update();
                        } else {
                            entry.changeInputValueLocal((String) field.get(articleJdo));
                            entry.update();
                        }
                        break;
                    }
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();    // todo log the error.
            }
        }
    }

    private double fixIfNeedAndParseDouble(String doubleString) {
        return Double.parseDouble(doubleString.replace(" ", "").replace(",", "."));
    }

    public void updateCells(List<CellEntry> cellEntries) throws IOException, ServiceException {
        for (CellEntry cellEntry : cellEntries) {
            spreadsheetService.update(worksheetUrl, cellEntry);
        }
    }

    public CellFeed getRow(int row) throws IOException, ServiceException {
        return getCellsInRange(row, row, null, null);
    }

    private void loginToGmail(SpreadsheetService spreadsheetService, String username, String password) throws AuthenticationException {
        spreadsheetService.setUserCredentials(username, password);
    }

    private List getSpreadsheetList(SpreadsheetService spreadsheetService) throws IOException, ServiceException {
        FeedURLFactory factory = FeedURLFactory.getDefault();
        SpreadsheetFeed feed = spreadsheetService.getFeed(factory.getSpreadsheetsFeedUrl(), SpreadsheetFeed.class);
        return feed.getEntries();
    }

    private SpreadsheetEntry getSpreadsheetByName(List spreadsheets, String spreadsheetName) {
        for (Object spreadsheetObject : spreadsheets) {
            if (spreadsheetObject instanceof SpreadsheetEntry) {
                SpreadsheetEntry spreadsheet = (SpreadsheetEntry) spreadsheetObject;
                String sheetTitle = spreadsheet.getTitle().getPlainText();
                if (sheetTitle != null && sheetTitle.equals(spreadsheetName)) {
                    return spreadsheet;
                }

            } else {
                // todo add strange log
            }
        }
        throw new RuntimeException("No spreadsheet found with name");
        // todo change exception
    }

    private URL getWorksheetUrl(SpreadsheetEntry spreadsheet, int worksheetNumber) throws IOException, ServiceException {
        return spreadsheet.getWorksheets().get(worksheetNumber).getCellFeedUrl();
    }
}
