package com.lavida.service.google;

import com.google.gdata.client.spreadsheet.FeedURLFactory;
import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.spreadsheet.*;
import com.google.gdata.util.ServiceException;
import com.lavida.service.entity.ArticleJdo;
import com.lavida.service.utils.CalendarConverter;
import com.lavida.service.utils.NormalFormatter;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created: 12:43 05.08.13
 * The {@code ArticlesFromGoogleDocHandler} retrieves data from google spreadsheet and creates a list of articles.
 *
 * @author Ruslan
 */
public class ArticlesFromGoogleDocUnmarshaller {
    private static final String APPLICATION_NAME = "lavida.articles";

    public ArticlesFromGoogleDocUnmarshaller() {
    }

    public List<ArticleJdo> unmarshal(String userNameGmail, String passwordGmail)
            throws ServiceException, IOException {
        List<ArticleJdo> articles = new ArrayList<ArticleJdo>();
        SpreadsheetService service = new SpreadsheetService(APPLICATION_NAME);
        FeedURLFactory factory = FeedURLFactory.getDefault();

        // login:
        service.setUserCredentials(userNameGmail, passwordGmail);

        // get sheets:
        SpreadsheetFeed feed = service.getFeed(factory.getSpreadsheetsFeedUrl(), SpreadsheetFeed.class);
        List spreadsheets = feed.getEntries();

        // load first sheet:
        SpreadsheetEntry spreadsheet = (SpreadsheetEntry) spreadsheets.get(0);
        List worksheets = spreadsheet.getWorksheets();
        URL cellFeedUrl = spreadsheet.getWorksheets().get(0).getCellFeedUrl();

        CellFeed cellFeed = service.getFeed(cellFeedUrl, CellFeed.class);
        int rowCount = cellFeed.getRowCount();       // total number of rows in the spreadsheet table

        ArticleJdo articleJdo = new ArticleJdo();
        int row = 2; // the current row  for creating ArticleJdo begins from 2 in the table of worksheet
        int cellRow = 0; // the row of the current cell
        List<CellEntry> cellEntryList = cellFeed.getEntries();
        for (CellEntry cellEntry : cellEntryList) {
            Cell cell = cellEntry.getCell();
            articleJdo.setId(cell.getRow());  // the id of articleJdo to the database is the number of row in worksheet
            if (cell.getRow() > 1) {  //  omit first row with table header.
                cellRow = cell.getRow();
                if (cellRow > row) {            // start of the next row
                    row = cellRow;
                    articles.add(articleJdo);
                    articleJdo = new ArticleJdo();
                }
                if (cell.getCol() == 1) {
//                    articleJdo.setId(Integer.parseInt(cell.getInputValue()));
                    continue;
                } else if (cell.getCol() == 2) {
                    articleJdo.setCode(cell.getInputValue());
                    continue;
                } else if (cell.getCol() == 3) {
                    articleJdo.setName(cell.getInputValue());
                    continue;
                } else if (cell.getCol() == 4) {
                    articleJdo.setBrand(cell.getInputValue());
                    continue;
                } else if (cell.getCol() == 5) {
                    articleJdo.setQuantity(Integer.parseInt(cell.getInputValue()));
                    continue;
                } else if (cell.getCol() == 6) {
                    articleJdo.setSize(cell.getInputValue());
                    continue;
                } else if (cell.getCol() == 7) {
                    articleJdo.setPurchasingPriceEUR(NormalFormatter.doubleNormalize(cell.getInputValue()));
                    continue;
                } else if (cell.getCol() == 8) {
                    articleJdo.setTransportCostEUR(NormalFormatter.doubleNormalize(cell.getInputValue()));
                    continue;
                } else if (cell.getCol() == 9) {
                    articleJdo.setDeliveryDate(CalendarConverter.convertStringDateToCalendar(cell.getInputValue()));
                    continue;
                } else if (cell.getCol() == 10) {
                    articleJdo.setPriceUAH(NormalFormatter.doubleNormalize(cell.getInputValue()));
                    continue;
                } else if (cell.getCol() == 11) {
                    articleJdo.setRaisedPriceUAH(NormalFormatter.doubleNormalize(cell.getInputValue()));
                    continue;
                } else if (cell.getCol() == 12) {
                    articleJdo.setActionPriceUAH(NormalFormatter.doubleNormalize(cell.getInputValue()));
                    continue;
                } else if (cell.getCol() == 13) {
                    articleJdo.setSold(cell.getInputValue());
                    continue;
                } else if (cell.getCol() == 14) {
                    articleJdo.setOurs(cell.getInputValue());
                    continue;
                } else if (cell.getCol() == 15) {
                    articleJdo.setSaleDate(CalendarConverter.convertStringDateToCalendar(cell.getInputValue()));
                    continue;
                } else if (cell.getCol() == 16) {
                    articleJdo.setComment(cell.getInputValue());
                    continue;
                }
            }
        }
        articles.add(articleJdo);
        return articles;
    }

    /**
     * @param userNameGmail
     * @param passwordGmail
     * @return
     * @throws ServiceException
     * @throws IOException
     */
    public List<String> readTableHeader(String userNameGmail, String passwordGmail) throws ServiceException, IOException {
        List<String> tableHeader = new ArrayList<String>();

        SpreadsheetService service = new SpreadsheetService(APPLICATION_NAME);
        FeedURLFactory factory = FeedURLFactory.getDefault();

        // login:
        service.setUserCredentials(userNameGmail, passwordGmail);

        // get sheets:
        SpreadsheetFeed feed = service.getFeed(factory.getSpreadsheetsFeedUrl(), SpreadsheetFeed.class);
        List spreadsheets = feed.getEntries();

        // load first sheet:
        SpreadsheetEntry spreadsheet = (SpreadsheetEntry) spreadsheets.get(0);
        List worksheets = spreadsheet.getWorksheets();
        URL cellFeedUrl = spreadsheet.getWorksheets().get(0).getCellFeedUrl();
        CellFeed cellFeed = service.getFeed(cellFeedUrl, CellFeed.class);

        int colCount = cellFeed.getColCount();
        List<CellEntry> cellEntryList = cellFeed.getEntries();
        Iterator<CellEntry> cellEntryIterator = cellEntryList.iterator();
        for (int i = 0; i < colCount; i++) {
            CellEntry cellEntry = cellEntryIterator.next();
            Cell cell = cellEntry.getCell();
            tableHeader.add(cell.getInputValue());
        }
        return tableHeader;
    }
}
