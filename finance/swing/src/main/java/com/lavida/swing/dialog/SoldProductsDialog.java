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

    private JDesktopPane desktopPane;
    private JPanel  westPanel, southPanel;
    private JButton refundButton, cancelButton;
    private JCheckBox currentDateCheckBox;

    @Override
    protected void initializeForm() {
        super.initializeForm();
        dialog.setTitle(messageSource.getMessage("dialog.sold.products.title", null, localeHolder.getLocale()));
        dialog.setResizable(true);
        dialog.setBounds(100, 100, 800, 500);
        dialog.setLocationRelativeTo(null);
    }


    @Override
    protected void initializeComponents() {
        rootContainer.setLayout(new BorderLayout());

//      desktop pane
        desktopPane = new JDesktopPane();
        desktopPane.setBackground(Color.white);
        desktopPane.setLayout(new BorderLayout());

//      main panel for table of goods
        articleTableComponent.initializeComponents(tableModel, messageSource, localeHolder);
        desktopPane.add(articleTableComponent.getMainPanel(), BorderLayout.CENTER);

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
        constraints.gridx = 2;
        constraints.gridy = 3;
        articleTableComponent.getArticleFiltersComponent().getFiltersPanel().add(currentDateCheckBox, constraints);

        desktopPane.add(articleTableComponent.getArticleFiltersComponent().getFiltersPanel(), BorderLayout.SOUTH);

        rootContainer.add(desktopPane, BorderLayout.CENTER);

//        west panel with buttons
        westPanel = new JPanel();
        westPanel.setBackground(Color.lightGray);
        westPanel.setLayout(new BoxLayout(westPanel, BoxLayout.LINE_AXIS));
        westPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        refundButton = new JButton();
        refundButton.setText(messageSource.getMessage("dialog.sold.products.button.refund.title", null, localeHolder.getLocale()));
        refundButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handler.refundButtonClicked();
            }
        });
        westPanel.add(refundButton);

        rootContainer.add(westPanel, BorderLayout.WEST);

//        south panel for buttons
        southPanel = new JPanel();
        southPanel.setBackground(Color.lightGray);
        southPanel.setLayout(new GridLayout(1,1));

        cancelButton = new JButton();
        cancelButton.setText(messageSource.getMessage("sellDialog.button.cancel.title", null, localeHolder.getLocale()));
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handler.cancelButtonClicked();
            }
        });
        southPanel.add(cancelButton);

        rootContainer.add(southPanel, BorderLayout.SOUTH);
    }

    /**
     * Filters the JTable by permissions of roles (ROLE_SELLER). It removes certain columns.
     *
     * @param userRoles
     */
    public void filterTableByRoles(java.util.List<String> userRoles) {
        articleTableComponent.filterTableByRoles(userRoles);
    }

    public ArticleTableComponent getArticleTableComponent() {
        return articleTableComponent;
    }

    public JDesktopPane getDesktopPane() {
        return desktopPane;
    }

    public JPanel getWestPanel() {
        return westPanel;
    }

    public JPanel getSouthPanel() {
        return southPanel;
    }

    public JButton getRefundButton() {
        return refundButton;
    }

    public JButton getCancelButton() {
        return cancelButton;
    }

    public JCheckBox getCurrentDateCheckBox() {
        return currentDateCheckBox;
    }
}
