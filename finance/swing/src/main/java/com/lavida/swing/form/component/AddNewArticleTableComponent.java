package com.lavida.swing.form.component;

import com.lavida.service.entity.ArticleJdo;
import com.lavida.swing.LocaleHolder;
import com.lavida.swing.service.AddNewArticleTableModel;
import com.lavida.swing.service.ArticlesTableModel;
import org.springframework.context.MessageSource;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.*;

/**
 * The table component for the AddNewProductsDialog
 * Created: 18:56 03.09.13
 *
 * @author Ruslan
 */
@Deprecated
public class AddNewArticleTableComponent implements TableModelListener {
    private AddNewArticleTableModel tableModel;
    private MessageSource messageSource;
    private LocaleHolder localeHolder;

    private JPanel mainPanel;
    private JTable articlesTable;
    private JScrollPane tableScrollPane;
    private AddNewArticleAnalyzeComponent analyzeComponent = new AddNewArticleAnalyzeComponent();
    private TableRowSorter<AddNewArticleTableModel> sorter ;

    public void initializeComponents(AddNewArticleTableModel articlesTableModel, MessageSource messageSource, LocaleHolder localeHolder) {
        this.tableModel = articlesTableModel;
        tableModel.addTableModelListener(this);
        this.messageSource = messageSource;
        this.localeHolder = localeHolder;
        tableModel.initAnalyzeFields();
        this.sorter = new TableRowSorter<AddNewArticleTableModel>(tableModel);
        sorter.setSortsOnUpdates(true);
//      main panel for table of goods
        mainPanel = new JPanel();
//        mainPanel.setBackground(Color.white);
        mainPanel.setLayout(new BorderLayout());

        articlesTable = new JTable(tableModel);
        articlesTable.setCellEditor(new DefaultCellEditor(new JTextField()));
        initTableColumnsWidth();
        articlesTable.doLayout();
        articlesTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
//      Filtering the table
        articlesTable.setFillsViewportHeight(true);
        articlesTable.setRowSelectionAllowed(true);
        articlesTable.setCellSelectionEnabled(true); // solution for copying one cell from table
        articlesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        articlesTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting()) return;
                ListSelectionModel listSelectionModel = (ListSelectionModel) e.getSource();
                if (!listSelectionModel.isSelectionEmpty()) {
                    int viewRow = listSelectionModel.getMinSelectionIndex();
                    int selectedRow = articlesTable.convertRowIndexToModel(viewRow);
                    ArticleJdo selectedArticle = tableModel.getArticleJdoByRowIndex(selectedRow);
                    tableModel.setSelectedArticle(selectedArticle);
                }
            }
        });

        tableScrollPane = new JScrollPane(articlesTable);
        tableScrollPane.setPreferredSize(new Dimension(1000, 700));
        initTableColumnsWidth();
        mainPanel.add(tableScrollPane, BorderLayout.CENTER);

//      panel for search operations
        analyzeComponent.initializeComponents(tableModel, messageSource, localeHolder);

    }

    /**
     * Filters the JTable by permissions of roles (ROLE_SELLER). It removes certain columns.
     *
     * @param userRoles
     */
    public void filterTableByRoles(java.util.List<String> userRoles) {
        java.util.List<String> forbiddenHeaders = tableModel.getForbiddenHeadersToShow(messageSource, localeHolder.getLocale(), userRoles);
        for (String forbiddenHeader : forbiddenHeaders) {
            articlesTable.removeColumn(articlesTable.getColumn(forbiddenHeader));
        }
    }

    /**
     * Sets preferred width to certain columns
     */
    private void initTableColumnsWidth() {
        Map<String, Integer> columnHeaderToWidth = tableModel.getColumnHeaderToWidth();
        for (Map.Entry<String, Integer> entry : columnHeaderToWidth.entrySet()) {
            articlesTable.getColumn(entry.getKey()).setPreferredWidth(entry.getValue());
        }
    }

    /**
     * Updates fields of the articleAnalyzeComponent.
     */
    public void updateAnalyzeComponent() {
        int totalCountArticles = 0;
        double totalOriginalCostEUR = 0;
        double totalPriceUAH = 0;

        int viewRows = sorter.getViewRowCount();
        java.util.List<ArticleJdo> selectedArticles = new ArrayList<ArticleJdo>();
        for (int i = 0; i < viewRows; i++) {
            int row = sorter.convertRowIndexToModel(i);
            selectedArticles.add(tableModel.getArticleJdoByRowIndex(row));
        }
        for (ArticleJdo articleJdo : selectedArticles) {
            ++totalCountArticles;
            totalOriginalCostEUR += (articleJdo.getTransportCostEUR() + articleJdo.getPurchasePriceEUR());
            totalPriceUAH += (articleJdo.getSalePrice());
        }
        analyzeComponent.updateFields(totalCountArticles, totalOriginalCostEUR, totalPriceUAH);
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public AddNewArticleAnalyzeComponent getAnalyzeComponent() {
        return analyzeComponent;
    }

    @Override
    public void tableChanged(TableModelEvent e) {
        if (e != null) {
            updateAnalyzeComponent();
            return;
        }
    }
}
