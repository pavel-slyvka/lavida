package com.lavida.swing.form.component;

import com.lavida.service.entity.ArticleJdo;
import com.lavida.swing.LocaleHolder;
import com.lavida.swing.service.ArticlesTableModel;
import org.springframework.context.MessageSource;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;

/**
 * ArticleTableComponent
 * Created: 20:09 16.08.13
 *
 * @author Pavel
 */
public class ArticleTableComponent {
    private ArticlesTableModel tableModel;
    private MessageSource messageSource;
    private LocaleHolder localeHolder;

    private JPanel mainPanel;
    private JTable articlesTable;
    private JScrollPane tableScrollPane;
    private ArticleFiltersComponent articleFiltersComponent = new ArticleFiltersComponent();
    private ArticleAnalyzeComponent articleAnalyzeComponent = new ArticleAnalyzeComponent();

    public void initializeComponents(ArticlesTableModel articlesTableModel, MessageSource messageSource, LocaleHolder localeHolder) {
        this.tableModel = articlesTableModel;
        this.messageSource = messageSource;
        this.localeHolder = localeHolder;

//      main panel for table of goods
        mainPanel = new JPanel();
        mainPanel.setBackground(Color.white);
        mainPanel.setLayout(new BorderLayout());

        articlesTable = new JTable(tableModel);
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
        packTable(articlesTable);
        mainPanel.add(tableScrollPane, BorderLayout.CENTER);

//      panel for search operations
        articleFiltersComponent.initializeComponents(tableModel, messageSource, localeHolder);
        articlesTable.setRowSorter(articleFiltersComponent.getSorter());

//        panel for analyzing total cost, price, count of products , shown in the table
        articleAnalyzeComponent.initializeComponents(tableModel, messageSource, localeHolder);

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
     *
     * @param table JTable to be changed
     */
    private void packTable(JTable table) {
//        table.getColumn(messageSource.getMessage("mainForm.table.articles.column.name.title", null,
//                localeHolder.getLocale())).setPreferredWidth(250);    // todo review and fix
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public ArticleFiltersComponent getArticleFiltersComponent() {
        return articleFiltersComponent;
    }

    public ArticleAnalyzeComponent getArticleAnalyzeComponent() {
        return articleAnalyzeComponent;
    }
}
