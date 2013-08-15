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

    public GoogleCellEntriesIterator(List<CellEntry> cellEntries) {
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
