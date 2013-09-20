package com.lavida.swing.handler;

import com.lavida.swing.service.UserSettingsService;
import com.lavida.swing.preferences.user.UsersSettingsHolder;
import com.lavida.swing.dialog.ColumnsViewSettingsDialog;
import com.lavida.swing.dialog.SoldProductsDialog;
import com.lavida.swing.form.MainForm;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * Created: 13:02 04.09.13
 *
 * @author Ruslan
 */
@Component
public class ColumnsViewSettingsDialogHandler {

    @Resource
    private ColumnsViewSettingsDialog dialog;

    @Resource
    private MainForm mainForm;

    @Resource
    private SoldProductsDialog soldProductsDialog;

    @Resource
    private UsersSettingsHolder usersSettingsHolder;

    @Resource
    private UserSettingsService userSettingsService;

    public void hideColumnButtonClicked() {
        List<String> selectedHeaders = dialog.getVisibleColumnsList().getSelectedValuesList();
        for (String header : selectedHeaders) {
            dialog.getNotVisibleColumnsListModel().addElement(header);
            dialog.getVisibleColumnsListModel().removeElement(header);
        }
    }

    public void showColumnButtonClicked() {
        List<String> selectedHeaders = dialog.getNotVisibleColumnsList().getSelectedValuesList();
        for (String header : selectedHeaders) {
            dialog.getVisibleColumnsListModel().addElement(header);
            dialog.getNotVisibleColumnsListModel().removeElement(header);
        }
    }

    public void applyButtonClicked() {
        TableColumnModel mainFormColumnModel = mainForm.getArticleTableComponent().getArticlesTable().getColumnModel();
        Enumeration<TableColumn> mainFormColumns = mainFormColumnModel.getColumns();
        List<TableColumn> mainFormColumnList = tableColumnEnumerationToList(mainFormColumns);

        TableColumnModel soldProductsDialogColumnModel = soldProductsDialog.getArticleTableComponent().getArticlesTable().getColumnModel();
        Enumeration<TableColumn> soldProductsDialogColumns = soldProductsDialogColumnModel.getColumns();
        List<TableColumn> soldProductsColumnList = tableColumnEnumerationToList(soldProductsDialogColumns);

        Enumeration<String> showHeaders = dialog.getVisibleColumnsListModel().elements();
        while (showHeaders.hasMoreElements()) {
            String showColumnHeader = showHeaders.nextElement();
            TableColumn mainFormColumn = dialog.getMainFormHeadersAndColumns().get(showColumnHeader);
            if (!mainFormColumnList.contains(mainFormColumn)) {
                mainFormColumnList.add(mainFormColumn);
                mainFormColumnModel.addColumn(mainFormColumn);
                mainFormColumnModel.moveColumn(mainFormColumnModel.getColumnCount() - 1, mainFormColumn.getModelIndex());
            }

            TableColumn soldProductsColumn = dialog.getSoldProductsHeadersAndColumns().get(showColumnHeader);
            if (!soldProductsColumnList.contains(soldProductsColumn)) {
                soldProductsColumnList.add(soldProductsColumn);
                soldProductsDialogColumnModel.addColumn(soldProductsColumn);
                soldProductsDialogColumnModel.moveColumn(soldProductsDialogColumnModel.getColumnCount() - 1, soldProductsColumn.getModelIndex());
            }
        }

        Enumeration<String> hideHeaders = dialog.getNotVisibleColumnsListModel().elements();
        while (hideHeaders.hasMoreElements()) {
            String hideColumnHeaders = hideHeaders.nextElement();
            TableColumn mainFormColumn = dialog.getMainFormHeadersAndColumns().get(hideColumnHeaders);
            if (mainFormColumnList.contains(mainFormColumn)) {
                mainFormColumnList.remove(mainFormColumn);
                mainFormColumnModel.removeColumn(mainFormColumn);
            }

            TableColumn soldProductsColumn = dialog.getSoldProductsHeadersAndColumns().get(hideColumnHeaders);
            if (soldProductsColumnList.contains(soldProductsColumn)) {
                soldProductsColumnList.remove(soldProductsColumn);
                soldProductsDialogColumnModel.removeColumn(soldProductsColumn);
            }
        }

        dialog.getVisibleColumnsList().clearSelection();
        dialog.getNotVisibleColumnsList().clearSelection();
        dialog.getShowColumnButton().setEnabled(false);
        dialog.getHideColumnButton().setEnabled(false);
        dialog.hide();
    }

    public void cancelButtonClicked() {
        dialog.updateListModels(mainForm.getArticleTableComponent().getArticlesTable());
        dialog.getVisibleColumnsList().clearSelection();
        dialog.getNotVisibleColumnsList().clearSelection();
        dialog.getShowColumnButton().setEnabled(false);
        dialog.getHideColumnButton().setEnabled(false);
        dialog.hide();
    }

    /**
     * Converts the Enumeration of TableColumn  to the List of TableColumn.
     * @param tableColumnEnumeration  the Enumeration to be converted.
     * @return the List of TableColumn.
     */
    private List<TableColumn> tableColumnEnumerationToList (Enumeration<TableColumn> tableColumnEnumeration) {
        List<TableColumn> tableColumnList = new ArrayList<TableColumn>();
        while (tableColumnEnumeration.hasMoreElements()) {
            tableColumnList.add(tableColumnEnumeration.nextElement());
        }
        return tableColumnList;
    }
}
