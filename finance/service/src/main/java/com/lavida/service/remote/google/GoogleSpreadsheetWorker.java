package com.lavida.service.remote.google;

import com.google.gdata.client.spreadsheet.CellQuery;
import com.google.gdata.client.spreadsheet.FeedURLFactory;
import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.spreadsheet.*;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;
import com.lavida.service.settings.Settings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.util.List;

/**
 * GoogleSpreadsheetWorker
 * Created: 22:15 15.08.13
 *
 * @author Pavel
 */
public class GoogleSpreadsheetWorker {
    private static final Logger logger = LoggerFactory.getLogger(GoogleSpreadsheetWorker.class);
    private static final String APPLICATION_NAME = "LA VIDA Finance.";


    private SpreadsheetService spreadsheetService = new SpreadsheetService(APPLICATION_NAME);
    private URL articleWorksheetUrl;
    private WorksheetEntry articleWorksheetEntry;
    private WorksheetEntry discountCardsWorksheetEntry;
    private URL discountCardsWorksheetUrl;

    public GoogleSpreadsheetWorker(Settings settings) throws ServiceException, IOException, LavidaGoogleException {
        loginToGmail(spreadsheetService, settings.getRemoteUser(), settings.getRemotePass());
        List spreadsheets = getSpreadsheetList(spreadsheetService);
        SpreadsheetEntry articleSpreadsheet = getSpreadsheetByName(spreadsheets, settings.getSpreadsheetName());
        articleWorksheetEntry = getWorksheetEntry(articleSpreadsheet, settings.getWorksheetNumber());
        articleWorksheetUrl = getWorksheetUrl(articleWorksheetEntry);

        SpreadsheetEntry discountCardsSpreadsheet = getSpreadsheetByName(spreadsheets, settings.getDiscountSpreadsheetName());
        discountCardsWorksheetEntry = getWorksheetEntry(discountCardsSpreadsheet, settings.getDiscountWorksheetNumber());
        discountCardsWorksheetUrl = getWorksheetUrl(discountCardsWorksheetEntry);
    }

    private void loginToGmail(SpreadsheetService spreadsheetService, String username, String password) throws LavidaGoogleException {
        try {
            spreadsheetService.setUserCredentials(username, password);
        } catch (AuthenticationException e) {
            throw new LavidaGoogleException(LavidaGoogleException.UNAUTHORIZED, e);
        }
    }

    private List getSpreadsheetList(SpreadsheetService spreadsheetService) throws LavidaGoogleException {
        FeedURLFactory factory = FeedURLFactory.getDefault();
        SpreadsheetFeed feed ;
        try {
            feed = spreadsheetService.getFeed(factory.getSpreadsheetsFeedUrl(), SpreadsheetFeed.class);
        } catch (IOException | ServiceException e) {
            throw new LavidaGoogleException(LavidaGoogleException.NOT_FOUND_SPREADSHEET_LIST, e);
        }
        return feed.getEntries();
    }

    private SpreadsheetEntry getSpreadsheetByName(List spreadsheets, String spreadsheetName) throws LavidaGoogleException {
        if (spreadsheetName == null) {
            throw new LavidaGoogleException("Spreadsheet name is null!", LavidaGoogleException.EMPTY_SPREADSHEET_NAME);
        }
        for (Object spreadsheetObject : spreadsheets) {
            if (spreadsheetObject instanceof SpreadsheetEntry) {
                SpreadsheetEntry spreadsheet = (SpreadsheetEntry) spreadsheetObject;
                String sheetTitle = spreadsheet.getTitle().getPlainText();
                if (sheetTitle != null && sheetTitle.trim().equals(spreadsheetName.trim())) {
                    return spreadsheet;
                }

            } else {
                logger.warn("Found spreadsheet which is not SpreadsheetEntry instance: " + spreadsheetObject);
            }
        }
        throw new LavidaGoogleException("Not found spreadsheet with name " + spreadsheetName + " !", LavidaGoogleException.NOT_FOUND_SPREADSHEET);
    }

    private WorksheetEntry getWorksheetEntry(SpreadsheetEntry spreadsheetEntry, int worksheetNumber) throws LavidaGoogleException {
        WorksheetEntry worksheetEntry ;
        try {
            worksheetEntry =  spreadsheetEntry.getWorksheets().get(worksheetNumber);
        } catch (IOException | ServiceException e) {
            throw new LavidaGoogleException(LavidaGoogleException.NOT_FOUND_WORKSHEET, e);
        }
        return worksheetEntry;
    }

    private URL getWorksheetUrl(WorksheetEntry worksheetEntry) {
        return worksheetEntry.getCellFeedUrl();
    }

    public CellFeed getArticleWholeDocument() throws IOException, ServiceException {
        return spreadsheetService.getFeed(articleWorksheetUrl, CellFeed.class);
    }

    public CellFeed getDiscountCardsWholeDocument() throws IOException, ServiceException {
        return spreadsheetService.getFeed(discountCardsWorksheetUrl, CellFeed.class);
    }

    public CellFeed getArticleCellsInRange(Integer minRow, Integer maxRow, Integer minCol, Integer maxCol) throws IOException, ServiceException {
        CellQuery query = new CellQuery(articleWorksheetUrl);
        query.setMinimumRow(minRow);
        query.setMaximumRow(maxRow);
        query.setMinimumCol(minCol);
        query.setMaximumCol(maxCol);
        return spreadsheetService.query(query, CellFeed.class);
    }

    public CellFeed getDiscountCardsCellsInRange(Integer minRow, Integer maxRow, Integer minCol, Integer maxCol) throws IOException, ServiceException {
        CellQuery query = new CellQuery(discountCardsWorksheetUrl);
        query.setMinimumRow(minRow);
        query.setMaximumRow(maxRow);
        query.setMinimumCol(minCol);
        query.setMaximumCol(maxCol);
        return spreadsheetService.query(query, CellFeed.class);

    }

    public void saveOrUpdateArticleCells(List<CellEntry> cellEntries) throws IOException, ServiceException {
        for (CellEntry cellEntry : cellEntries) {
            spreadsheetService.insert(articleWorksheetUrl, cellEntry);
        }
    }

    public void saveOrUpdateDiscountCardsCells(List<CellEntry> cellEntries) throws IOException, ServiceException {
        for (CellEntry cellEntry : cellEntries) {
            spreadsheetService.insert(discountCardsWorksheetUrl, cellEntry);
        }
    }

    public CellFeed getArticleRow(int row) throws IOException, ServiceException {
        return getArticleCellsInRange(row, row, null, null);
    }

    public CellFeed getDiscountCardsRow(int row) throws IOException, ServiceException {
        return getDiscountCardsCellsInRange(row, row, null, null);
    }

    /**
     * Adds a row to the worksheet.
     * @return the number of the added row.
     * @throws IOException  if Connection error occurs when extending worksheet .
     * @throws ServiceException if Service error occurs when extending worksheet .
     */
    public int addArticleRow() throws IOException, ServiceException {
        int rowCount = articleWorksheetEntry.getRowCount();
        articleWorksheetEntry.setRowCount(++rowCount);
        articleWorksheetEntry.update();
        return rowCount;
    }

    /**
     * Adds a row to the worksheet.
     * @return the number of the added row.
     * @throws IOException  if Connection error occurs when extending worksheet .
     * @throws ServiceException if Service error occurs when extending worksheet .
     */
    public int addDiscountCardsRow() throws IOException, ServiceException {
        int rowCount = discountCardsWorksheetEntry.getRowCount();
        discountCardsWorksheetEntry.setRowCount(++rowCount);
        discountCardsWorksheetEntry.update();
        return rowCount;
    }

}
