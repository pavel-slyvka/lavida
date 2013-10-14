package com.lavida.swing.dialog.settings;

import com.lavida.swing.dialog.AbstractDialog;
import com.lavida.swing.handler.SelectingCategoriesEditingDialogHandler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * The SelectingCategoriesEditingDialog for brand, size, shop, seller, tags categories.
 * <p/>
 * Created: 14.10.13 11:35.
 *
 * @author Ruslan.
 */
@Component
public class SelectingCategoriesEditingDialog extends AbstractDialog {

    @Resource
    private SelectingCategoriesEditingDialogHandler handler;

    private java.util.List<CategoryType> categoriesNamesList;
    private Map<String, JList<String>> categoriesJListMap;
    private JComboBox<String> categoriesBox;
    private JButton changeButton, deleteButton;
    private JScrollPane scrollPane;

    @Override
    protected void initializeForm() {
        super.initializeForm();
        dialog.setTitle(messageSource.getMessage("dialog.selectingCategories.editing", null, localeHolder.getLocale()));
        dialog.setResizable(false);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(null);
    }

    @Override
    protected void initializeComponents() {
        rootContainer.setLayout(new BorderLayout());
        initializeCategoriesNamesList();
        categoriesJListMap = new HashMap<>();
        handler.initializeListModelsMap();

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(5, 5, 5, 5);
        constraints.anchor = GridBagConstraints.EAST;


        String[] categoriesArray = new String[categoriesNamesList.size()];
        for (int i = 0; i < categoriesNamesList.size(); i++) {
            categoriesArray[i] = categoriesNamesList.get(i).getName();
        }
        Arrays.sort(categoriesArray);
        categoriesBox = new JComboBox<>(categoriesArray);
        categoriesBox.setEditable(false);
        categoriesBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    JList<String> viewList = getViewList();
                    viewList.clearSelection();
                    changeButton.setEnabled(false);
                    deleteButton.setEnabled(false);
                    scrollPane.setViewportView(viewList);
                } else if (e.getStateChange() == ItemEvent.DESELECTED) {
                    java.awt.Component viewComponent = scrollPane.getViewport().getView();
                    if (viewComponent instanceof JList) {
                        ((JList) viewComponent).clearSelection();
                    }

                }
            }
        });

        JLabel categoriesLabel = new JLabel();
        categoriesLabel.setText(messageSource.getMessage("dialog.selectingCategories.label.categories", null, localeHolder.getLocale()));
        categoriesLabel.setLabelFor(categoriesBox);
        constraints.gridwidth = GridBagConstraints.RELATIVE;
        constraints.weightx = 0.0;
        mainPanel.add(categoriesLabel, constraints);

        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.weightx = 1.0;
        mainPanel.add(categoriesBox, constraints);

        scrollPane = new JScrollPane(getViewList());
        scrollPane.setPreferredSize(new Dimension(230, 250));
        scrollPane.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        scrollPane.setMinimumSize(new Dimension(230, 200));
        constraints.gridwidth = GridBagConstraints.RELATIVE;
        constraints.gridheight = GridBagConstraints.REMAINDER;
        constraints.weightx = 0.0;
        constraints.gridx = 0;
        constraints.gridy = 1;
        mainPanel.add(scrollPane, constraints);

        JPanel operationPanel = new JPanel();
        operationPanel.setLayout(new BoxLayout(operationPanel, BoxLayout.PAGE_AXIS));

        changeButton = new JButton();
        changeButton.setText(messageSource.getMessage("dialog.selectingCategories.button.change", null, localeHolder.getLocale()));
        changeButton.setHorizontalTextPosition(JButton.CENTER);
        changeButton.setPreferredSize(new Dimension(150, 30));
        changeButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        changeButton.setMinimumSize(new Dimension(100, 30));
        changeButton.setEnabled(false);
        changeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handler.changeButtonClicked();
            }
        });

        JButton addButton = new JButton();
        addButton.setText(messageSource.getMessage("dialog.selectingCategories.button.add", null, localeHolder.getLocale()));
        addButton.setHorizontalTextPosition(JButton.CENTER);
        addButton.setPreferredSize(new Dimension(150, 30));
        addButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        addButton.setMinimumSize(new Dimension(100, 30));
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handler.addButtonClicked();
            }
        });

        deleteButton = new JButton();
        deleteButton.setText(messageSource.getMessage("dialog.selectingCategories.button.delete", null, localeHolder.getLocale()));
        deleteButton.setHorizontalTextPosition(JButton.CENTER);
        deleteButton.setPreferredSize(new Dimension(150, 30));
        deleteButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        deleteButton.setMinimumSize(new Dimension(100, 30));
        deleteButton.setEnabled(false);
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handler.deleteButtonClicked();
            }
        });

//        operationPanel.add(Box.createVerticalGlue());
        operationPanel.add(changeButton);
        operationPanel.add(Box.createVerticalGlue());
        operationPanel.add(addButton);
        operationPanel.add(Box.createVerticalGlue());
        operationPanel.add(deleteButton);
//        operationPanel.add(Box.createVerticalGlue());

        constraints.fill = GridBagConstraints.BOTH;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.gridheight = GridBagConstraints.REMAINDER;
        constraints.weightx = 1.0;
        constraints.weighty = 1.0;
        constraints.gridx = 1;
        constraints.gridy = 1;
        mainPanel.add(operationPanel, constraints);

        JPanel southPanel = new JPanel();
        southPanel.setLayout(new BoxLayout(southPanel, BoxLayout.LINE_AXIS));
        southPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JButton cancelButton = new JButton();
        cancelButton.setText(messageSource.getMessage("dialog.selectingCategories.button.cancel", null, localeHolder.getLocale()));
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handler.cancelButtonClicked();
            }
        });

        southPanel.add(Box.createHorizontalGlue());
        southPanel.add(cancelButton);

        rootContainer.add(mainPanel, BorderLayout.CENTER);
        rootContainer.add(southPanel, BorderLayout.SOUTH);
    }


    private JList<String> getViewList() {
        JList<String> jList = null;
        String listName = (String) categoriesBox.getSelectedItem();
        if (listName != null) {
            jList = categoriesJListMap.get(listName);
        }
        return jList;
    }

    private void initializeCategoriesNamesList() {
        categoriesNamesList = new ArrayList<>();
        categoriesNamesList.addAll(Arrays.asList(CategoryType.values()));
    }


    public Map<String, JList<String>> getCategoriesJListMap() {
        return categoriesJListMap;
    }

    public JButton getChangeButton() {
        return changeButton;
    }

    public JButton getDeleteButton() {
        return deleteButton;
    }

    public JComboBox<String> getCategoriesBox() {
        return categoriesBox;
    }


    public enum CategoryType {
        BRAND("Бренд"),
        SIZE("Размер"),
        SHOP("Магазин"),
        SELLER("Продавец"),
        TAG("Тег");
        private String name;

        CategoryType(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

}
