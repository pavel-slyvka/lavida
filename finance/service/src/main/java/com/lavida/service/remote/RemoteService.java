package com.lavida.service.remote;

import com.google.gdata.data.spreadsheet.CellEntry;
import com.google.gdata.data.spreadsheet.CellFeed;
import com.google.gdata.util.ServiceException;
import com.lavida.service.entity.ArticleJdo;
import com.lavida.service.entity.DiscountCardJdo;
import com.lavida.service.remote.google.GoogleCellEntriesIterator;
import com.lavida.service.remote.google.GoogleCellsTransformer;
import com.lavida.service.remote.google.GoogleSpreadsheetWorker;
import com.lavida.service.settings.SettingsHolder;
import com.lavida.utils.ReflectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * RemoteService
 * Created: 12:43 05.08.13
 *
 * @author Pavel
 */
@Service
public class RemoteService {

    @Resource
    private SettingsHolder settingsHolder;

    @Resource
    private GoogleCellsTransformer cellsTransformer;

    public List<ArticleJdo> loadArticles() throws ServiceException, IOException {
        GoogleSpreadsheetWorker worker = new GoogleSpreadsheetWorker(settingsHolder.getSettings());
        CellFeed cellFeed = worker.getArticleWholeDocument();

        GoogleCellEntriesIterator cellEntriesIterator = new GoogleCellEntriesIterator(cellFeed.getEntries());
        Map<Integer, String> headers = cellsTransformer.cellsToColNumsAndItsValueMap(cellEntriesIterator.getNextLine());
        cellsTransformer.cellsToColNumsAndItsValueMap(cellEntriesIterator.getNextLine());   // header titles.

        List<ArticleJdo> articles = new ArrayList<ArticleJdo>();
        while (cellEntriesIterator.hasNext()) {
            ArticleJdo articleJdo = cellsTransformer.cellsToArticleJdo(cellEntriesIterator.getNextLine(), headers);
            articles.add(articleJdo);
        }
        return articles;
    }

    public List<DiscountCardJdo> loadDiscountCards() throws ServiceException, IOException {
        GoogleSpreadsheetWorker worker = new GoogleSpreadsheetWorker(settingsHolder.getSettings());
        CellFeed cellFeed = worker.getDiscountCardsWholeDocument();

        GoogleCellEntriesIterator cellEntriesIterator = new GoogleCellEntriesIterator(cellFeed.getEntries());
        Map<Integer, String> headers = cellsTransformer.cellsToColNumsAndItsValueMap(cellEntriesIterator.getNextLine());
        cellsTransformer.cellsToColNumsAndItsValueMap(cellEntriesIterator.getNextLine());   // header titles.

        List<DiscountCardJdo> discountCardJdoList = new ArrayList<DiscountCardJdo>();
        while (cellEntriesIterator.hasNext()) {
            DiscountCardJdo discountCardJdo = cellsTransformer.cellsToDicountCardJdo(cellEntriesIterator.getNextLine(), headers);
            discountCardJdoList.add(discountCardJdo);
        }
        return discountCardJdoList;
    }


    public void updateArticleToRemote(ArticleJdo articleJdo, Boolean isSold) throws IOException, ServiceException {
        GoogleSpreadsheetWorker spreadsheetWorker = new GoogleSpreadsheetWorker(settingsHolder.getSettings());

        // take headers
        CellFeed headersFeed = spreadsheetWorker.getArticleRow(1);
        GoogleCellEntriesIterator cellEntriesIterator = new GoogleCellEntriesIterator(headersFeed.getEntries());
        Map<Integer, String> headers = cellsTransformer.cellsToColNumsAndItsValueMap(cellEntriesIterator.getNextLine());
        CellFeed articleFeed = null;
        if (articleJdo.getSpreadsheetRow() > 0) {
            // take articles feed and check (by code and size) if all correct
            articleFeed = spreadsheetWorker.getArticleRow(articleJdo.getSpreadsheetRow());
            String codeColumnHeader = ReflectionUtils.getFieldAnnotation(ArticleJdo.class, "code", SpreadsheetColumn.class).column();
            String codeColumnValue = cellsTransformer.cellToValue(articleFeed, headers, codeColumnHeader);
            String sizeColumnHeader = ReflectionUtils.getFieldAnnotation(ArticleJdo.class, "size", SpreadsheetColumn.class).column();
            String sizeColumnValue = cellsTransformer.cellToValue(articleFeed, headers, sizeColumnHeader);
            String soldColumnHeader = ReflectionUtils.getFieldAnnotation(ArticleJdo.class, "sold", SpreadsheetColumn.class).column();
            String soldColumnValue = cellsTransformer.cellToValue(articleFeed, headers, soldColumnHeader);

            // ArticleJdo.spreadsheetRow has incorrect value -> search for correct
            if (isSold != null && isSold.booleanValue()) {    // for selling
                if ((codeColumnValue != null) ? !codeColumnValue.equals(articleJdo.getCode()) :
                        articleJdo.getCode() != null
                                || (sizeColumnValue != null) ? !sizeColumnValue.equals(articleJdo.getSize()) :
                                articleJdo.getSize() != null
                                        || soldColumnValue != null && !StringUtils.isEmpty(soldColumnValue)
                        ) {
                    Integer newSpreadsheetRowIndex = null;
                    List<ArticleJdo> realArticles = loadArticles();
                    for (ArticleJdo realArticleJdo : realArticles) {
                        if ((realArticleJdo.getCode() != null) ? realArticleJdo.getCode().equals(articleJdo.getCode()) :
                                articleJdo.getCode() == null
                                        && (realArticleJdo.getSize() != null) ? realArticleJdo.getSize().equals(articleJdo.getSize()) :
                                        articleJdo.getSize() == null
                                                && StringUtils.isEmpty(realArticleJdo.getSold())) {
                            newSpreadsheetRowIndex = realArticleJdo.getSpreadsheetRow();
                        }
                    }

                    if (newSpreadsheetRowIndex != null) {
                        articleJdo.setSpreadsheetRow(newSpreadsheetRowIndex);

                    } else {
                        throw new RuntimeException("Updating article doesn't exist anymore!");
                    }
                }
            } else if (isSold != null && !isSold.booleanValue()) {  //for refunding  , code != null
                if ((codeColumnValue != null) ? !codeColumnValue.equals(articleJdo.getCode()) :
                        articleJdo.getCode() != null
                                || (sizeColumnValue != null) ? !sizeColumnValue.equals(articleJdo.getSize()) :
                                articleJdo.getSize() != null
                                        || soldColumnValue == null && StringUtils.isEmpty(soldColumnValue)
                        ) {
                    Integer newSpreadsheetRowIndex = null;
                    List<ArticleJdo> realArticles = loadArticles();
                    for (ArticleJdo realArticleJdo : realArticles) {
                        if ((realArticleJdo.getCode() != null) ? realArticleJdo.getCode().equals(articleJdo.getCode()) :
                                articleJdo.getCode() == null
                                        && (realArticleJdo.getSize() != null) ? realArticleJdo.getSize().equals(articleJdo.getSize()) :
                                        articleJdo.getSize() == null
                                                && !StringUtils.isEmpty(realArticleJdo.getSold())) {
                            newSpreadsheetRowIndex = realArticleJdo.getSpreadsheetRow();
                        }
                    }
                    if (newSpreadsheetRowIndex != null) {
                        articleJdo.setSpreadsheetRow(newSpreadsheetRowIndex);
                    } else {
                        throw new RuntimeException("Updating article doesn't exist anymore!");
                    }
                }
            } else {   // no selling nor refunding, but simple changing
                if ((codeColumnValue != null ? !codeColumnValue.equals(articleJdo.getCode()) : articleJdo.getCode() != null)
                                || (sizeColumnValue != null ? !sizeColumnValue.equals(articleJdo.getSize()) :
                                articleJdo.getSize() != null)) {
                    Integer newSpreadsheetRowIndex = null;
                    List<ArticleJdo> realArticles = loadArticles();
                    for (ArticleJdo realArticleJdo : realArticles) {
                        if ((realArticleJdo.getCode() != null) ? realArticleJdo.getCode().equals(articleJdo.getCode()) :
                                articleJdo.getCode() == null
                                        && (realArticleJdo.getSize() != null) ? realArticleJdo.getSize().equals(articleJdo.getSize()) :
                                        articleJdo.getSize() == null
                                                && (realArticleJdo.getSold() != null) ? realArticleJdo.getSold().equals(articleJdo.getSold()) :
                                                articleJdo.getSold() == null
                                ) {
                            newSpreadsheetRowIndex = realArticleJdo.getSpreadsheetRow();
                        }
                    }
                    if (newSpreadsheetRowIndex != null) {
                        articleJdo.setSpreadsheetRow(newSpreadsheetRowIndex);
                    } else {
                        throw new RuntimeException("Updating article doesn't exist anymore!");
                    }
                }
            }
        } else if (articleJdo.getSpreadsheetRow() == 0) {
            articleJdo.setSpreadsheetRow(spreadsheetWorker.addArticleRow());
            articleFeed = spreadsheetWorker.getArticleRow(articleJdo.getSpreadsheetRow());
        }
        // transform and update.
        List<CellEntry> cellEntriesForUpdate = cellsTransformer.articleToCellEntriesForUpdate(articleJdo, headers, articleFeed);
        spreadsheetWorker.saveOrUpdateArticleCells(cellEntriesForUpdate);
    }

    public void updateDiscountCardToRemote(DiscountCardJdo discountCardJdo) throws IOException, ServiceException {
        GoogleSpreadsheetWorker spreadsheetWorker = new GoogleSpreadsheetWorker(settingsHolder.getSettings());

        // take headers
        CellFeed headersFeed = spreadsheetWorker.getDiscountCardsRow(1);
        GoogleCellEntriesIterator cellEntriesIterator = new GoogleCellEntriesIterator(headersFeed.getEntries());
        Map<Integer, String> headers = cellsTransformer.cellsToColNumsAndItsValueMap(cellEntriesIterator.getNextLine());
        CellFeed discountCardFeed = null;
        if (discountCardJdo.getSpreadsheetRow() > 0) {
            // take articles feed and check (by code and size) if all correct
            discountCardFeed = spreadsheetWorker.getDiscountCardsRow(discountCardJdo.getSpreadsheetRow());
            String numberColumnHeader = ReflectionUtils.getFieldAnnotation(DiscountCardJdo.class, "number", SpreadsheetColumn.class).column();
            String numberColumnValue = cellsTransformer.cellToValue(discountCardFeed, headers, numberColumnHeader);

            // DiscountCardJdo.spreadsheetRow has incorrect value -> search for correct
                if ((numberColumnValue != null) ? !numberColumnValue.equals(discountCardJdo.getNumber()) :
                        discountCardJdo.getNumber() != null) {
                    Integer newSpreadsheetRowIndex = null;
                    List<DiscountCardJdo> realDiscountCards = loadDiscountCards();
                    for (DiscountCardJdo realDiscountCardJdo : realDiscountCards) {
                        if ((realDiscountCardJdo.getNumber() != null) ? realDiscountCardJdo.getNumber().equals(discountCardJdo.getNumber()) :
                                discountCardJdo.getNumber() == null) {
                            newSpreadsheetRowIndex = realDiscountCardJdo.getSpreadsheetRow();
                        }
                    }

                    if (newSpreadsheetRowIndex != null) {
                        discountCardJdo.setSpreadsheetRow(newSpreadsheetRowIndex);

                    } else {
                        throw new RuntimeException("Updating discount card doesn't exist anymore!");
                    }
                }
        } else if (discountCardJdo.getSpreadsheetRow() == 0) {
            discountCardJdo.setSpreadsheetRow(spreadsheetWorker.addDiscountCardsRow());
            discountCardFeed = spreadsheetWorker.getDiscountCardsRow(discountCardJdo.getSpreadsheetRow());
        }
        // transform and update.
        List<CellEntry> cellEntriesForUpdate = cellsTransformer.discountCardToCellEntriesForUpdate(discountCardJdo, headers, discountCardFeed);
        spreadsheetWorker.saveOrUpdateDiscountCardsCells(cellEntriesForUpdate);
    }


}
