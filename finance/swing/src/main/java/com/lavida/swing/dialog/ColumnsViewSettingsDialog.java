package com.lavida.swing.dialog;

import com.lavida.swing.handler.ColumnsViewSettingsDialogHandler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

/**
 * The dialog for choosing which columns to show or hide.
 * Created: 12:40 04.09.13
 *
 * @author Ruslan
 */
@Component
public class ColumnsViewSettingsDialog extends AbstractDialog {

    @Resource
    private ColumnsViewSettingsDialogHandler handler;

    private JPanel buttonPanel, inputPanel, visibleColumnsPanel, notVisibleColumnsPanel;
    private JButton applyButton, cancelButton, hideColumnButton, showColumnButton;
    private JList visibleColumnsList, notVisibleColumnsList;
    private JScrollPane visibleListScrollPane, notVisibleListScrollPane;
    private DefaultListModel<String> visibleColumnsListModel, notVisibleColumnsListModel;

    //    private List<String>  visibleTableColumnHeaders, notVisibleTableColumnHeaders;
    private Map<String, TableColumn> mainFormHeadersAndColumns;
    private Map<String, TableColumn> soldProductsHeadersAndColumns;
    private JLabel errorMessage;

    @Override
    protected void initializeForm() {
        super.initializeForm();
        dialog.setTitle(messageSource.getMessage("dialog.settings.view.columns.title", null, localeHolder.getLocale()));
        dialog.setResizable(true);
        dialog.setSize(500, 400);
        dialog.setLocationRelativeTo(null);
    }

    public void initializeLists(JTable mainFormTable, JTable soldProductsTable) {
        Enumeration<TableColumn> mainFormColumns = mainFormTable.getColumnModel().getColumns();
        TableColumn column;
        String header;
        while (mainFormColumns.hasMoreElements()) {
            column = mainFormColumns.nextElement();
            header = column.getHeaderValue().toString();
            mainFormHeadersAndColumns.put(header, column);
            visibleColumnsListModel.addElement(header);
        }
        Enumeration<TableColumn> soldProductsColumns = soldProductsTable.getColumnModel().getColumns();
        while (soldProductsColumns.hasMoreElements()) {
            column = soldProductsColumns.nextElement();
            header = column.getHeaderValue().toString();
            soldProductsHeadersAndColumns.put(header, column);
        }
    }

    public void updateListModels(JTable articlesTable) {
        visibleColumnsListModel.removeAllElements();
        notVisibleColumnsListModel.removeAllElements();
        Enumeration<TableColumn> visibleColumns = articlesTable.getColumnModel().getColumns();
        TableColumn column;
        String header;
        while (visibleColumns.hasMoreElements()) {
            column = visibleColumns.nextElement();
            header = column.getHeaderValue().toString();
            visibleColumnsListModel.addElement(header);
        }
        Set<Map.Entry<String, TableColumn>> tableColumnEntries = mainFormHeadersAndColumns.entrySet();
        for (Map.Entry<String, TableColumn> tableColumnEntry : tableColumnEntries) {
            header = tableColumnEntry.getKey();
            if (!visibleColumnsListModel.contains(header)) {
                notVisibleColumnsListModel.addElement(header);
            }
        }
    }

    @Override
    protected void initializeComponents() {
        mainFormHeadersAndColumns = new HashMap<String, TableColumn>();
        soldProductsHeadersAndColumns = new HashMap<String, TableColumn>();
        visibleColumnsListModel = new DefaultListModel();
        notVisibleColumnsListModel = new DefaultListModel();

        rootContainer.setLayout(new BorderLayout());
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
                getMessage("dialog.settings.view.columns.label.columns.visible", null, localeHolder.getLocale())),
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

        hideColumnButton = new JButton(messageSource.getMessage("dialog.settings.view.columns.button.hide", null, localeHolder.getLocale()));
        hideColumnButton.setHorizontalTextPosition(JButton.CENTER);
        hideColumnButton.setPreferredSize(new Dimension(150, 30));
        hideColumnButton.setMaximumSize(new Dimension(500, 30));
        hideColumnButton.setMinimumSize(new Dimension(150, 30));
        hideColumnButton.setEnabled(false);
        hideColumnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handler.hideColumnButtonClicked();
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
                getMessage("dialog.settings.view.columns.label.columns.notVisible", null, localeHolder.getLocale())),
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

        showColumnButton = new JButton(messageSource.getMessage("dialog.settings.view.columns.button.show", null, localeHolder.getLocale()));
        showColumnButton.setHorizontalTextPosition(JButton.CENTER);
        showColumnButton.setPreferredSize(new Dimension(150, 30));
        showColumnButton.setMaximumSize(new Dimension(500, 30));
        showColumnButton.setMinimumSize(new Dimension(150, 30));
        showColumnButton.setEnabled(false);
        showColumnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handler.showColumnButtonClicked();
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

        errorMessage = new JLabel();
        errorMessage.setForeground(Color.RED);
        errorMessage.setHorizontalAlignment(JLabel.LEFT);

        JPanel errorPanel = new JPanel();
        errorPanel.setLayout(new BoxLayout(errorPanel, BoxLayout.LINE_AXIS));
        errorPanel.setPreferredSize(new Dimension(200, 30));
        errorPanel.setMinimumSize(new Dimension(100, 30));
        errorPanel.setMaximumSize(new Dimension(500, 50));
        errorPanel.add(errorMessage);
        buttonPanel.add(errorPanel);
        buttonPanel.add(Box.createHorizontalGlue());

        JPanel buttons = new JPanel();
        buttons.setLayout(new BoxLayout(buttons, BoxLayout.LINE_AXIS));
        applyButton = new JButton(messageSource.getMessage("dialog.settings.view.columns.button.apply", null,
                localeHolder.getLocale()));
        applyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handler.applyButtonClicked();
            }
        });

        cancelButton = new JButton(messageSource.getMessage("sellDialog.button.cancel.title", null, localeHolder.getLocale()));
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handler.cancelButtonClicked();
            }
        });
        buttons.add(applyButton);
        buttons.add(Box.createHorizontalStrut(5));
        buttons.add(cancelButton);
        buttonPanel.add(buttons);

        rootContainer.add(inputPanel, BorderLayout.CENTER);
        rootContainer.add(buttonPanel, BorderLayout.SOUTH);
    }


    public DefaultListModel<String> getVisibleColumnsListModel() {
        return visibleColumnsListModel;
    }

    public DefaultListModel<String> getNotVisibleColumnsListModel() {
        return notVisibleColumnsListModel;
    }

    public Map<String, TableColumn> getMainFormHeadersAndColumns() {
        return mainFormHeadersAndColumns;
    }

    public Map<String, TableColumn> getSoldProductsHeadersAndColumns() {
        return soldProductsHeadersAndColumns;
    }

    public JList getVisibleColumnsList() {
        return visibleColumnsList;
    }

    public JList getNotVisibleColumnsList() {
        return notVisibleColumnsList;
    }

    public JButton getHideColumnButton() {
        return hideColumnButton;
    }

    public JButton getShowColumnButton() {
        return showColumnButton;
    }

    public JLabel getErrorMessage() {
        return errorMessage;
    }

    public static boolean applyUserSettings(String presetName) {
        return false;
    }
}
