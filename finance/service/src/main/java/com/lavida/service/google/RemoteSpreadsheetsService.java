package com.lavida.service.google;

import com.google.gdata.client.spreadsheet.FeedURLFactory;
import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.spreadsheet.*;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;
import com.lavida.service.entity.ArticleJdo;
import com.lavida.service.settings.Settings;
import com.lavida.service.settings.SettingsHolder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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
@Service
public class RemoteSpreadsheetsService {
    private static final String APPLICATION_NAME = "LA VIDA Finance.";

    @Resource
    private SettingsHolder settingsHolder;

    @Resource
    private SpreadsheetArticleTransformer spreadsheetArticleTransformer;

    public List<ArticleJdo> loadArticles() throws ServiceException, IOException {
        CellFeed cellFeed = getCellFeedFromGoogle();
        return spreadsheetArticleTransformer.transformCellFeedToArticleJdoList(cellFeed);
    }

    /**
     * @return
     * @throws ServiceException
     * @throws IOException
     */
    public List<String> readTableHeader() throws ServiceException, IOException {
        CellFeed cellFeed = getCellFeedFromGoogle();

        List<String> tableHeader = new ArrayList<String>();
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
