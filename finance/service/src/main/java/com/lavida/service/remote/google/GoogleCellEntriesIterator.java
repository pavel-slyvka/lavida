package com.lavida.service.remote.google;

import com.google.gdata.data.spreadsheet.Cell;
import com.google.gdata.data.spreadsheet.CellEntry;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * GoogleCellEntriesIterator
 * Created: 22:15 15.08.13
 *
 * @author Pavel
 */
public class GoogleCellEntriesIterator {
    private List<CellEntry> cellEntries;
    private Iterator<CellEntry> cellEntryIterator;
    private Cell lastNotHandledCell;
    private boolean isFirstNext = true;

    public GoogleCellEntriesIterator(List<CellEntry> cellEntries) {
        this.cellEntries = cellEntries;

    }

    public List<Cell> getNextLine() {
        if (cellEntryIterator == null) {
            cellEntryIterator = cellEntries.iterator();
        }
        if (isFirstNext && cellEntryIterator.hasNext()) {
            lastNotHandledCell = cellEntryIterator.next().getCell();
            isFirstNext = false;
        }
        List<Cell> cells = new ArrayList<Cell>();
        if (lastNotHandledCell != null) {
            cells.add(lastNotHandledCell);
            lastNotHandledCell = null;
        }
        int lastCol = lastNotHandledCell != null ? lastNotHandledCell.getCol() : 0;
        while (cellEntryIterator.hasNext()) {
            CellEntry cellEntry = cellEntryIterator.next();
            if (cellEntry.getCell().getCol() <= lastCol) {
                lastNotHandledCell = cellEntry.getCell();
                break;
            }
            cells.add(cellEntry.getCell());
            lastCol = cellEntry.getCell().getCol();
        }
        return cells;
    }

    public boolean hasNext() {
        return (cellEntryIterator == null && !cellEntries.isEmpty()) || lastNotHandledCell != null;
    }
}
