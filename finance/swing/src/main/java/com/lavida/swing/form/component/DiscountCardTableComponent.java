package com.lavida.swing.form.component;

import com.lavida.service.entity.DiscountCardJdo;
import com.lavida.swing.preferences.user.UsersSettingsHolder;
import com.lavida.swing.LocaleHolder;
import com.lavida.swing.service.DiscountCardsTableModel;
import org.springframework.context.MessageSource;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import java.awt.*;
import java.util.Map;

/**
 * The table component for the AllDiscountCardsDialog
 * Created: 11:48 06.09.13
 *
 * @author Ruslan
 */

public class DiscountCardTableComponent implements TableModelListener {

    private DiscountCardsTableModel tableModel;
    private MessageSource messageSource;
    private LocaleHolder localeHolder;
    private UsersSettingsHolder usersSettingsHolder;

    private JPanel mainPanel;
    private JTable discountCardsTable;
    private JScrollPane tableScrollPane;
    private DiscountCardFiltersComponent cardFiltersComponent = new DiscountCardFiltersComponent();

    public void initializeComponents(DiscountCardsTableModel discountCardsTableModel, MessageSource messageSource,
                                     LocaleHolder localeHolder, UsersSettingsHolder usersSettingsHolder) {
        this.tableModel = discountCardsTableModel;
        this.tableModel.addTableModelListener(this);
        this.messageSource = messageSource;
        this.localeHolder = localeHolder;
        this.usersSettingsHolder = usersSettingsHolder;

        tableModel.initAnalyzeFields();

//      main panel for table of goods
        mainPanel = new JPanel();
//        mainPanel.setBackground(Color.white);
        mainPanel.setLayout(new BorderLayout());

        discountCardsTable = new JTable(tableModel);
        discountCardsTable.setCellEditor(new DefaultCellEditor(new JTextField()));
        initTableColumnsWidth();
        discountCardsTable.doLayout();
        discountCardsTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
//      Filtering the table
        discountCardsTable.setFillsViewportHeight(true);
        discountCardsTable.setRowSelectionAllowed(true);
        discountCardsTable.setCellSelectionEnabled(true); // solution for copying one cell from table
        discountCardsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        discountCardsTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting()) return;
                ListSelectionModel listSelectionModel = (ListSelectionModel) e.getSource();
                if (!listSelectionModel.isSelectionEmpty()) {
                    int viewRow = listSelectionModel.getMinSelectionIndex();
                    int selectedRow = discountCardsTable.convertRowIndexToModel(viewRow);
                    DiscountCardJdo selectedCard = tableModel.getDiscountCardByRowIndex(selectedRow);
                    tableModel.setSelectedCard(selectedCard);
                }
            }
        });

        tableScrollPane = new JScrollPane(discountCardsTable);
        tableScrollPane.setPreferredSize(new Dimension(1000, 700));
        initTableColumnsWidth();
        mainPanel.add(tableScrollPane, BorderLayout.CENTER);

//      panel for search operations
        cardFiltersComponent.initializeComponents(tableModel, messageSource, localeHolder);
        discountCardsTable.setRowSorter(cardFiltersComponent.getSorter());

    }

    /**
     * Filters the JTable by permissions of roles (ROLE_SELLER). It removes certain columns.
     *
     * @param userRoles
     */
    public void filterTableByRoles(java.util.List<String> userRoles) {
        java.util.List<String> forbiddenHeaders = tableModel.getForbiddenHeadersToShow(messageSource, localeHolder.getLocale(), userRoles);
        for (String forbiddenHeader : forbiddenHeaders) {
            discountCardsTable.removeColumn(discountCardsTable.getColumn(forbiddenHeader));
        }
    }

    /**
     * Sets preferred width to certain columns
     */
    private void initTableColumnsWidth() {
        Map<String, Integer> columnHeaderToWidth = tableModel.getColumnHeaderToWidth();
        for (Map.Entry<String, Integer> entry : columnHeaderToWidth.entrySet()) {
            discountCardsTable.getColumn(entry.getKey()).setPreferredWidth(entry.getValue());
        }
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public DiscountCardFiltersComponent getCardFiltersComponent() {
        return cardFiltersComponent;
    }

    public JTable getDiscountCardsTable() {
        return discountCardsTable;
    }

    @Override
    public void tableChanged(TableModelEvent e) {
        if (e != null) {
            getCardFiltersComponent().updateAnalyzeComponent();
            return;
        }
    }

    public boolean applyUserSettings(String presetName) {
        // todo finish logic applyDefaultUserSettings

        fixColumnOrder(presetName);
        fixColumnWidth(presetName);
        fixColumnEditors(presetName);

        return false;
    }

    private void fixColumnEditors(String presetName) {

    }

    private void fixColumnWidth(String presetName) {

    }

    private void fixColumnOrder(String presetName) {

    }

}
