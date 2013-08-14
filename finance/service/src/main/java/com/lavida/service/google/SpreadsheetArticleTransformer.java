package com.lavida.service.google;

import com.google.gdata.data.spreadsheet.Cell;
import com.google.gdata.data.spreadsheet.CellEntry;
import com.google.gdata.data.spreadsheet.CellFeed;
import com.lavida.service.entity.ArticleJdo;
import com.lavida.service.utils.CalendarConverter;
import com.lavida.service.utils.NormalFormatter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pavel on 13.08.13.
 */
@Component
@Deprecated
public class SpreadsheetArticleTransformer {

    public List<ArticleJdo> transformCellFeedToArticleJdoList(CellFeed cellFeed) {
        List<ArticleJdo> articles = new ArrayList<ArticleJdo>();
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
}
