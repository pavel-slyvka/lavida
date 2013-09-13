package com.lavida.swing.form.component;

import com.lavida.service.entity.ArticleJdo;
import com.lavida.swing.LocaleHolder;
import com.lavida.swing.service.ArticlesTableModel;
import org.springframework.context.MessageSource;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * ArticleTableComponent
 * Created: 20:09 16.08.13
 *
 * @author Pavel
 */
public class ArticleTableComponent implements TableModelListener {
    private ArticlesTableModel tableModel;
    private MessageSource messageSource;
    private LocaleHolder localeHolder;

    private JPanel mainPanel;
    private JTable articlesTable;
    private JScrollPane tableScrollPane;
    private JComboBox brandBox, sizeBox, shopBox;
    private ArticleFiltersComponent articleFiltersComponent = new ArticleFiltersComponent();

    private String[] brandArray = {"H&M", "Mango", "Zara", "PULL&BEAR", "Westrags", "Bershka", "GoodLuck", "HTrand",
            "FeelingModa", "TodayFashion", "KR", "Glamour", "MGessi", "ProntoModa", "PuroLino", "Fashion",
            "RockerModa", "RealityJeans", "MASFashion", "PlayIN", "ModaFashion", "DanceForever", "AssaGold",
            "SweetMiss", "BestCopine", "Desmon", "Sahiba", "Amo&Roma", "Moda", "LantisJeans", "Victoria",
            "A&P", "Milano", "Luna", "ItalyModa", "Elena", "Unics", "RouuaRssi", "Gabarra", "Emmetrenta",
            "DKoton", "Sabiba"};

    private String[] sizeArray = {"34", "36", "38", "40", "42", "44", "46", "48", "50", "52", "54",  "25/32", "26/32",
            "27/32", "28/32", "29/32", "30/32", "31/32", "32/32", "S", "XS", "M", "L", "XL", "XXL", "XXXL",
            "Universal", "S/M", "S/L", "L/XL", "XL/XXL", "XXL/XXXL"};

    private String[] shopArray = {"LA VIDA", "СЛАВЯНСКИЙ", "НОВОМОСКОВСК"};

    public void initializeComponents(ArticlesTableModel articlesTableModel, MessageSource messageSource, LocaleHolder localeHolder) {
        this.tableModel = articlesTableModel;
        this.messageSource = messageSource;
        this.localeHolder = localeHolder;
        tableModel.initAnalyzeFields();
        tableModel.addTableModelListener(this);

//      main panel for table of goods
        mainPanel = new JPanel();
//        mainPanel.setBackground(Color.white);
        mainPanel.setLayout(new BorderLayout());

        articlesTable = new JTable(tableModel);
        articlesTable.setCellEditor(new DefaultCellEditor(new JTextField()));

        initTableColumnsEditors();

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
        articleFiltersComponent.initializeComponents(tableModel, messageSource, localeHolder);
        articlesTable.setRowSorter(articleFiltersComponent.getSorter());

    }

    private void initTableColumnsEditors() {
        brandBox = new JComboBox(brandArray);
        brandBox.setEditable(true);
        TableCellEditor brandEditor = new DefaultCellEditor(brandBox);
        TableColumn brandColumn = articlesTable.getColumn(messageSource.getMessage("mainForm.table.articles.column.brand.title", null, localeHolder.getLocale()));
        brandColumn.setCellEditor(brandEditor);

        sizeBox = new JComboBox(sizeArray);
        sizeBox.setEditable(true);
        TableCellEditor sizeEditor = new DefaultCellEditor(sizeBox);
        TableColumn sizeColumn = articlesTable.getColumn(messageSource.getMessage("mainForm.table.articles.column.size.title",
                 null, localeHolder.getLocale()));
        sizeColumn.setCellEditor(sizeEditor);

        shopBox = new JComboBox(shopArray);
        shopBox.setEditable(true);
        TableCellEditor shopEditor = new DefaultCellEditor(shopBox);
        TableColumn shopColumn = articlesTable.getColumn(messageSource.getMessage("mainForm.table.articles.column.shop.title",
                 null, localeHolder.getLocale()));
        shopColumn.setCellEditor(shopEditor);
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

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public ArticleFiltersComponent getArticleFiltersComponent() {
        return articleFiltersComponent;
    }

    public JTable getArticlesTable() {
        return articlesTable;
    }

    @Override
    public void tableChanged(TableModelEvent e) {
        if (e != null) {
            getArticleFiltersComponent().updateAnalyzeComponent();
            return;
        }

    }
}
