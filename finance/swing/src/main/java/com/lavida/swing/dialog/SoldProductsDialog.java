package com.lavida.swing.dialog;

import com.lavida.swing.form.component.ArticleTableComponent;
import com.lavida.swing.handler.SoldProductsDialogHandler;
import com.lavida.swing.service.ArticlesTableModel;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.*;

/**
 * Created: 15:29 18.08.13
 * The SoldProductsDialog is the dialog for selling the selected product.
 * @author Ruslan
 */
@Component
public class SoldProductsDialog extends AbstractDialog {

    @Resource(name = "soldArticleTableModel")
    private ArticlesTableModel tableModel;

    @Resource
    private SoldProductsDialogHandler handler;

    private ArticleTableComponent articleTableComponent = new ArticleTableComponent();

    private JPanel operationPanel, southPanel, desktopPanel, filtersPanel, analyzePanel, mainPanel,
            buttonPanel;
    private JButton refundButton, cancelButton;
    private JCheckBox currentDateCheckBox;

    @Override
    protected void initializeForm() {
        super.initializeForm();
        dialog.setTitle(messageSource.getMessage("dialog.sold.products.title", null, localeHolder.getLocale()));
        dialog.setResizable(true);
        dialog.setBounds(100, 100, 800, 700);
        dialog.setLocationRelativeTo(null);
    }


    @Override
    protected void initializeComponents() {
        rootContainer.setLayout(new BorderLayout());

//      desktop pane
        desktopPanel = new JPanel();
        desktopPanel.setLayout(new BorderLayout());
        desktopPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        articleTableComponent.initializeComponents(tableModel, messageSource, localeHolder);


//      panel for search operations
        currentDateCheckBox = new JCheckBox();
        currentDateCheckBox.setText(messageSource.getMessage("dialog.sold.products.checkBox.current.date.title",
                null, localeHolder.getLocale()));
        currentDateCheckBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                int state = e.getStateChange();
                if (state == ItemEvent.SELECTED) {
                    handler.currentDateCheckBoxSelected();
                } else if (state == ItemEvent.DESELECTED) {
                    handler.currentDateCheckBoxDeSelected();
                }
            }
        });
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridx = 1;
        constraints.gridy = articleTableComponent.getArticleFiltersComponent().getFilters().size();
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.anchor = GridBagConstraints.EAST;
        constraints.weightx = 1.0;
        articleTableComponent.getArticleFiltersComponent().getFiltersPanel().add(currentDateCheckBox, constraints);

//      main panel for table of goods
        mainPanel = articleTableComponent.getMainPanel();
        desktopPanel.add(mainPanel, BorderLayout.CENTER);


//        south panel for desktopPanel
        southPanel = new JPanel(new GridBagLayout());
        southPanel.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));



//      analyze panel for total analyses
        analyzePanel = articleTableComponent.getArticleFiltersComponent().getArticleAnalyzeComponent().getAnalyzePanel();
//      panel for search operations
        filtersPanel= articleTableComponent.getArticleFiltersComponent().getFiltersPanel();



        operationPanel = new JPanel();
        operationPanel.setLayout(new GridBagLayout());
        operationPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(messageSource.
                getMessage("mainForm.panel.operation.title", null, localeHolder.getLocale())),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        refundButton = new JButton();
        refundButton.setHorizontalTextPosition(JButton.CENTER);
        refundButton.setPreferredSize(new Dimension(150, 25));
        refundButton.setMaximumSize(new Dimension(150, 25));
        refundButton.setMinimumSize(new Dimension(150, 25));
        refundButton.setText(messageSource.getMessage("dialog.sold.products.button.refund.title", null, localeHolder.getLocale()));
        refundButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handler.refundButtonClicked();
            }
        });
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.anchor = GridBagConstraints.SOUTH;
        constraints.weightx = 1.0;
        operationPanel.add(refundButton, constraints);

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.fill = GridBagConstraints.REMAINDER;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.weightx = 0.0;
        constraints.weighty = 0.0;
        southPanel.add(analyzePanel, constraints);

        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.fill = GridBagConstraints.VERTICAL;
        constraints.gridwidth = GridBagConstraints.RELATIVE;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.weightx = 0.0;
        constraints.weighty = 1.0;
        southPanel.add(operationPanel, constraints);

        constraints.gridx = 1;
        constraints.gridy = 1;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.gridheight = GridBagConstraints.REMAINDER;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.weightx = 1.0;
        constraints.weighty = 1.0;
        southPanel.add(filtersPanel, constraints);

        desktopPanel.add(southPanel, BorderLayout.SOUTH);

        rootContainer.add(desktopPanel, BorderLayout.CENTER);

//        south panel for buttons
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.TRAILING));

        cancelButton = new JButton();
        cancelButton.setHorizontalTextPosition(JButton.CENTER);
        cancelButton.setPreferredSize(new Dimension(150, 25));
        cancelButton.setMaximumSize(new Dimension(150, 25));
        cancelButton.setMinimumSize(new Dimension(150, 25));
        cancelButton.setText(messageSource.getMessage("sellDialog.button.cancel.title", null, localeHolder.getLocale()));
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handler.cancelButtonClicked();
            }
        });
        buttonPanel.add(cancelButton);

        rootContainer.add(buttonPanel, BorderLayout.SOUTH);
    }

    /**
     * Filters the JTable by permissions of roles (ROLE_SELLER). It removes certain columns.
     *
     * @param userRoles
     */
    public void filterTableByRoles(java.util.List<String> userRoles) {
        articleTableComponent.filterTableByRoles(userRoles);
    }

    public void filterAnalyzePanelByRoles(java.util.List<String> userRoles) {
        articleTableComponent.getArticleFiltersComponent().getArticleAnalyzeComponent().
                filterAnalyzeComponentByRoles(userRoles);
    }

    public ArticleTableComponent getArticleTableComponent() {
        return articleTableComponent;
    }



//    public JButton getRefundButton() {
//        return refundButton;
//    }
//
//    public JButton getCancelButton() {
//        return cancelButton;
//    }
//
//    public JCheckBox getCurrentDateCheckBox() {
//        return currentDateCheckBox;
//    }
}
