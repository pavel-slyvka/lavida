package com.lavida.service.google;

import com.google.gdata.client.spreadsheet.FeedURLFactory;
import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.spreadsheet.*;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;
import com.lavida.service.entity.ArticleJdo;
import com.lavida.service.settings.Settings;
import com.lavida.service.settings.SettingsHolder;
import com.lavida.service.utils.CalendarConverter;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.net.URL;
import java.util.*;

/**
 * Created: 12:43 05.08.13
 * The {@code ArticlesFromGoogleDocHandler} retrieves data from google spreadsheet and creates a list of articles.
 *
 * @author Ruslan
 */
@Service
public class RemoteSpreadsheetsService {
    private static final String APPLICATION_NAME = "LA VIDA Finance.";

    @Resource
    private SettingsHolder settingsHolder;

    private Map<Integer, String> transformCellsToHeaders(List<Cell> cells) {
        Map<Integer, String> headers = new HashMap<Integer, String>(cells.size());
        for (Cell cell : cells) {
            headers.put(cell.getCol(), cell.getValue());
        }
        return headers;
    }

    private double fixIfNeedAndParseDouble(String doubleString) {
        return Double.parseDouble(doubleString.replace(" ", "").replace(",", "."));
    }

    private ArticleJdo transformCellsToArticleJdo(List<Cell> cells, Map<Integer, String> headers) {
        ArticleJdo articleJdo = new ArticleJdo();
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
                                field.set(articleJdo, CalendarConverter.convertStringDateToCalendar(value));
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

    public List<ArticleJdo> loadArticles() throws ServiceException, IOException {
        CellFeed cellFeed = getCellFeedFromGoogle();

        CellEntriesIterator cellEntriesIterator = new CellEntriesIterator(cellFeed.getEntries());
        Map<Integer, String> headers = transformCellsToHeaders(cellEntriesIterator.getNextLine());
        Map<Integer, String> headersTitle = transformCellsToHeaders(cellEntriesIterator.getNextLine());

        List<ArticleJdo> articles = new ArrayList<ArticleJdo>();
        while (cellEntriesIterator.hasNext()) {
            ArticleJdo articleJdo = transformCellsToArticleJdo(cellEntriesIterator.getNextLine(), headers);
            articles.add(articleJdo);
        }
        return articles;
    }

    private class CellEntriesIterator {
        private List<CellEntry> cellEntries;
        private Iterator<CellEntry> cellEntryIterator;
        private Cell lastNotHandledCell;

        public CellEntriesIterator(List<CellEntry> cellEntries) {
            this.cellEntries = cellEntries;
        }

        public List<Cell> getNextLine() {
            if (cellEntryIterator == null) {
                cellEntryIterator = cellEntries.iterator();
            }
            List<Cell> cells = new ArrayList<Cell>();
            if (lastNotHandledCell != null) {
                cells.add(lastNotHandledCell);
                lastNotHandledCell = null;
            }
            while (cellEntryIterator.hasNext()) {
                CellEntry cellEntry = cellEntryIterator.next();
                if (cellEntry.getCell().getCol() == 1) {
                    lastNotHandledCell = cellEntry.getCell();
                    break;
                }
                cells.add(cellEntry.getCell());
            }
            return cells;
        }

        public boolean hasNext() {
            return (cellEntryIterator == null && !cellEntries.isEmpty()) || lastNotHandledCell != null;
        }
    }

    private SpreadsheetService createSpreadsheetService() {
        return new SpreadsheetService(APPLICATION_NAME);
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
        throw new RuntimeException("No spreadsheet found with name");   // todo change exception
    }

    private URL getWorksheetUrl(SpreadsheetEntry spreadsheet, int worksheetNumber) throws IOException, ServiceException {
        return spreadsheet.getWorksheets().get(worksheetNumber).getCellFeedUrl();
    }

    private CellFeed getCellFeedFromGoogle() throws ServiceException, IOException {
        Settings settings = settingsHolder.getSettings();
        SpreadsheetService spreadsheetService = createSpreadsheetService();

        loginToGmail(spreadsheetService, settings.getRemoteUser(), settings.getRemotePass());
        List spreadsheets = getSpreadsheetList(spreadsheetService);
        SpreadsheetEntry spreadsheet = getSpreadsheetByName(spreadsheets, settings.getSpreadsheetName());
        URL worksheetUrl = getWorksheetUrl(spreadsheet, settings.getWorksheetNumber());
        return spreadsheetService.getFeed(worksheetUrl, CellFeed.class);
    }

    public void updateArticle(ArticleJdo articleJdo) {
//       todo updateArticle to spreadsheet
    }
}
