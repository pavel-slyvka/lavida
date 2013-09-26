package com.lavida.swing.form.component;

import com.lavida.swing.LocaleHolder;
import com.lavida.swing.dialog.AbstractDialog;
import com.lavida.swing.preferences.*;
import org.springframework.context.MessageSource;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

/**
 * The TableViewComponent
 * <p/>
 * Created: 24.09.13 21:17.
 *
 * @author Ruslan.
 */
public class TableViewComponent {
    private MessageSource messageSource;
    private LocaleHolder localeHolder;
    private JTable table;
    private AbstractDialog dialog;
    private UsersSettingsHolder usersSettingsHolder;

    private JPanel mainPanel, inputPanel, visibleColumnsPanel, notVisibleColumnsPanel, buttonPanel;
    private JButton hideColumnButton, showColumnButton, applyButton, cancelButton;
    private JList visibleColumnsList, notVisibleColumnsList;
    private JScrollPane visibleListScrollPane, notVisibleListScrollPane;
    private DefaultListModel<String> visibleColumnsListModel, notVisibleColumnsListModel;

    private Map<String, TableColumn> headersAndColumnsMap;


    public void initializeComponents(AbstractDialog dialog, JTable table, Map<String, TableColumn> headersAndColumnsMap,
                                     MessageSource messageSource, LocaleHolder localeHolder, UsersSettingsHolder usersSettingsHolder) {
        this.messageSource = messageSource;
        this.localeHolder = localeHolder;
        this.table = table;
        this.dialog = dialog;
        this.usersSettingsHolder = usersSettingsHolder;
        this.headersAndColumnsMap = headersAndColumnsMap;

        visibleColumnsListModel = new DefaultListModel<>();
        notVisibleColumnsListModel = new DefaultListModel<>();
        Set<Map.Entry<String, TableColumn>> entrySet = headersAndColumnsMap.entrySet();
        for (Map.Entry<String, TableColumn> entry : entrySet) {
            visibleColumnsListModel.addElement(entry.getKey());
        }
        visibleColumnsListModel = sortListModel(visibleColumnsListModel);

        mainPanel = new JPanel(new BorderLayout());
//      input panel
        inputPanel = new JPanel(new GridLayout(1, 2));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(5, 0, 5, 0);

        visibleColumnsPanel = new JPanel(new GridBagLayout());
        visibleColumnsPanel.setPreferredSize(new Dimension(220, 270));
        visibleColumnsPanel.setMaximumSize(new Dimension(520, 570));
        visibleColumnsPanel.setMinimumSize(new Dimension(120, 170));
        visibleColumnsPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(messageSource.
                getMessage("component.table.view.label.columns.visible", null, localeHolder.getLocale())),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        visibleColumnsList = new JList(visibleColumnsListModel);
        visibleColumnsList.setLayoutOrientation(JList.VERTICAL);
        visibleColumnsList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting() == false) {
                    if (visibleColumnsList.getSelectedIndex() == -1) {
                        hideColumnButton.setEnabled(false);
                    } else {
                        hideColumnButton.setEnabled(true);
                    }
                }
            }
        });
        visibleColumnsList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        visibleListScrollPane = new JScrollPane(visibleColumnsList);
        visibleListScrollPane.setPreferredSize(new Dimension(200, 200));
        visibleListScrollPane.setMaximumSize(new Dimension(500, 500));
        visibleListScrollPane.setMinimumSize(new Dimension(100, 50));
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.gridheight = GridBagConstraints.RELATIVE;
        constraints.anchor = GridBagConstraints.EAST;
        constraints.weightx = 1.0;
        constraints.gridx = 0;
        constraints.gridy = 0;
        visibleColumnsPanel.add(visibleListScrollPane, constraints);

        hideColumnButton = new JButton(messageSource.getMessage("component.table.view.button.hide", null, localeHolder.getLocale()));
        hideColumnButton.setHorizontalTextPosition(JButton.CENTER);
        hideColumnButton.setPreferredSize(new Dimension(150, 30));
        hideColumnButton.setMaximumSize(new Dimension(500, 30));
        hideColumnButton.setMinimumSize(new Dimension(150, 30));
        hideColumnButton.setEnabled(false);
        hideColumnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                java.util.List selectedHeaders = visibleColumnsList.getSelectedValuesList();
                for (Object header : selectedHeaders) {
                    notVisibleColumnsListModel.addElement((String) header);
                    visibleColumnsListModel.removeElement(header);
                }

            }
        });
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.gridheight = GridBagConstraints.RELATIVE;
        constraints.anchor = GridBagConstraints.EAST;
        constraints.weightx = 1.0;
        constraints.gridx = 0;
        constraints.gridy = 1;
        visibleColumnsPanel.add(hideColumnButton, constraints);
        inputPanel.add(visibleColumnsPanel);

        notVisibleColumnsPanel = new JPanel(new GridBagLayout());
        notVisibleColumnsPanel.setPreferredSize(new Dimension(220, 270));
        notVisibleColumnsPanel.setMaximumSize(new Dimension(520, 570));
        notVisibleColumnsPanel.setMinimumSize(new Dimension(120, 170));
        notVisibleColumnsPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(messageSource.
                getMessage("component.table.view.label.columns.notVisible", null, localeHolder.getLocale())),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));

        notVisibleColumnsList = new JList(notVisibleColumnsListModel);
        notVisibleColumnsList.setLayoutOrientation(JList.VERTICAL);
        notVisibleColumnsList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting() == false) {
                    if (notVisibleColumnsList.getSelectedIndex() == -1) {
                        showColumnButton.setEnabled(false);
                    } else {
                        showColumnButton.setEnabled(true);
                    }
                }
            }
        });
        notVisibleColumnsList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        notVisibleListScrollPane = new JScrollPane(notVisibleColumnsList);
        notVisibleListScrollPane.setPreferredSize(new Dimension(200, 200));
        notVisibleListScrollPane.setMaximumSize(new Dimension(500, 500));
        notVisibleListScrollPane.setMinimumSize(new Dimension(100, 50));
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridwidth = GridBagConstraints.RELATIVE;
        constraints.gridheight = GridBagConstraints.RELATIVE;
        constraints.anchor = GridBagConstraints.EAST;
        constraints.weightx = 1.0;
        constraints.gridx = 0;
        constraints.gridy = 0;
        notVisibleColumnsPanel.add(notVisibleListScrollPane, constraints);

        showColumnButton = new JButton(messageSource.getMessage("component.table.view.button.show", null, localeHolder.getLocale()));
        showColumnButton.setHorizontalTextPosition(JButton.CENTER);
        showColumnButton.setPreferredSize(new Dimension(150, 30));
        showColumnButton.setMaximumSize(new Dimension(500, 30));
        showColumnButton.setMinimumSize(new Dimension(150, 30));
        showColumnButton.setEnabled(false);
        showColumnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                java.util.List selectedHeaders = notVisibleColumnsList.getSelectedValuesList();
                for (Object header : selectedHeaders) {
                    visibleColumnsListModel.addElement((String) header);
                    notVisibleColumnsListModel.removeElement(header);
                }

            }
        });
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.gridheight = GridBagConstraints.RELATIVE;
        constraints.anchor = GridBagConstraints.EAST;
        constraints.weightx = 1.0;
        constraints.gridx = 0;
        constraints.gridy = 1;
        notVisibleColumnsPanel.add(showColumnButton, constraints);

        inputPanel.add(notVisibleColumnsPanel);

        //        button panel
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.LINE_AXIS));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        buttonPanel.add(Box.createHorizontalGlue());

        JPanel buttons = new JPanel();
        buttons.setLayout(new BoxLayout(buttons, BoxLayout.LINE_AXIS));
        applyButton = new JButton(messageSource.getMessage("component.table.view.button.apply", null,
                localeHolder.getLocale()));
        applyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                applyButtonClicked();
            }
        });

        cancelButton = new JButton(messageSource.getMessage("sellDialog.button.cancel.title", null, localeHolder.getLocale()));
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cancelButtonClicked();
            }
        });
        buttons.add(applyButton);
        buttons.add(Box.createHorizontalStrut(5));
        buttons.add(cancelButton);
        buttonPanel.add(buttons);

        mainPanel.add(inputPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

    }

    private void cancelButtonClicked() {
        updateListModels();
        visibleColumnsList.clearSelection();
        notVisibleColumnsList.clearSelection();
        showColumnButton.setEnabled(false);
        hideColumnButton.setEnabled(false);
        dialog.hide();

    }

    private void applyButtonClicked() {
        TableColumnModel tableColumnModel = table.getColumnModel();
        Enumeration<TableColumn> columnEnumeration = tableColumnModel.getColumns();
        List<TableColumn> tableColumnList = tableColumnEnumerationToList(columnEnumeration);

        Enumeration<String> showHeaders = visibleColumnsListModel.elements();
        while (showHeaders.hasMoreElements()) {
            String showColumnHeader = showHeaders.nextElement();
            TableColumn tableColumn = headersAndColumnsMap.get(showColumnHeader);
            if (!tableColumnList.contains(tableColumn)) {
                tableColumnList.add(tableColumn);
                tableColumnModel.addColumn(tableColumn);
                int position = getColumnPosition(tableColumnModel, tableColumn);
                if (position != -1) {
                    tableColumnModel.moveColumn(tableColumnModel.getColumnCount() - 1, position);
                }
            }
        }

        Enumeration<String> hideHeaders = notVisibleColumnsListModel.elements();
        while (hideHeaders.hasMoreElements()) {
            String hideColumnHeaders = hideHeaders.nextElement();
            TableColumn tableColumn = headersAndColumnsMap.get(hideColumnHeaders);
            if (tableColumnList.contains(tableColumn)) {
                tableColumnList.remove(tableColumn);
                tableColumnModel.removeColumn(tableColumn);
            }
        }

        visibleColumnsList.clearSelection();
        notVisibleColumnsList.clearSelection();
        updateListModels();
        showColumnButton.setEnabled(false);
        hideColumnButton.setEnabled(false);
        dialog.hide();
    }

    private int getColumnPosition(TableColumnModel tableColumnModel, TableColumn tableColumn) {
        int columnPosition = 0;
        Enumeration<TableColumn> columnEnumeration = tableColumnModel.getColumns();
        List<TableColumn> tableColumnList = tableColumnEnumerationToList(columnEnumeration);
        int currentPosition = 0;
        boolean last = true;
        for (TableColumn column : tableColumnList) {
            if (tableColumn.getModelIndex() < column.getModelIndex()) {
                columnPosition = currentPosition;
                last = false;
                break;
            }
            currentPosition ++;
        }

        if (last){
            return -1;
        }else {
            return columnPosition;
        }
    }

    /**
     * Converts the Enumeration of TableColumn  to the List of TableColumn.
     *
     * @param tableColumnEnumeration the Enumeration to be converted.
     * @return the List of TableColumn.
     */
    private List<TableColumn> tableColumnEnumerationToList(Enumeration<TableColumn> tableColumnEnumeration) {
        List<TableColumn> tableColumnList = new ArrayList<>();
        while (tableColumnEnumeration.hasMoreElements()) {
            tableColumnList.add(tableColumnEnumeration.nextElement());
        }
        return tableColumnList;
    }

    public void updateListModels() {
        visibleColumnsListModel.removeAllElements();
        notVisibleColumnsListModel.removeAllElements();
        Enumeration<TableColumn> visibleColumns = table.getColumnModel().getColumns();
        TableColumn column;
        String header;
        while (visibleColumns.hasMoreElements()) {
            column = visibleColumns.nextElement();
            header = column.getHeaderValue().toString();
            visibleColumnsListModel.addElement(header);
        }
        visibleColumnsListModel = sortListModel(visibleColumnsListModel);

        Set<Map.Entry<String, TableColumn>> tableColumnEntries = headersAndColumnsMap.entrySet();
        for (Map.Entry<String, TableColumn> tableColumnEntry : tableColumnEntries) {
            header = tableColumnEntry.getKey();
            if (!visibleColumnsListModel.contains(header)) {
                notVisibleColumnsListModel.addElement(header);
            }
        }
        notVisibleColumnsListModel = sortListModel(notVisibleColumnsListModel);
    }

    /**
     * Sorts the DefaultListModel according to the table column's index.
     *
     * @param listModel the DefaultListModel to be sorted.
     * @return the sorted DefaultListModel.
     */
    private DefaultListModel<String> sortListModel(DefaultListModel<String> listModel) {
        String[] intermediateHeaders = new String[getMaxIndex(headersAndColumnsMap) + 1];
        for (Object item : listModel.toArray()) {
            String header = (String) item;
            TableColumn column = headersAndColumnsMap.get(header);
            int index = column.getModelIndex();
            intermediateHeaders[index] = header;
        }
        listModel.removeAllElements();
        for (String header : intermediateHeaders) {
            if (header != null) {
                listModel.addElement(header);
            }
        }
        return listModel;
    }

    private int getMaxIndex(Map<String, TableColumn> map) {
        int maxIndex = 0;
        Set<Map.Entry<String, TableColumn>> entrySet = map.entrySet();
        for (Map.Entry<String, TableColumn> entry : entrySet) {
            if (maxIndex < entry.getValue().getModelIndex()) {
                maxIndex = entry.getValue().getModelIndex();
            }
        }
        return maxIndex;
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

}
