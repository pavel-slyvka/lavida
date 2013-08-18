package com.lavida.swing.dialog;

import com.lavida.swing.form.component.ArticleTableComponent;
import com.lavida.swing.handler.MainFormHandler;
import com.lavida.swing.handler.SoldProductsDialogHandler;
import com.lavida.swing.service.ArticlesTableModel;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

/**
 * Created: 15:29 18.08.13
 *
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

    }

    /**
     * Filters the JTable by permissions of roles (ROLE_SELLER). It removes certain columns.
     *
     * @param userRoles
     */
    public void filterTableByRoles(java.util.List<String> userRoles) {
        articleTableComponent.filterTableByRoles(userRoles);
    }


}
