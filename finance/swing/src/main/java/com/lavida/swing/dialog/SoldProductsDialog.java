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
    private JMenuBar menuBar;
    private JMenu fileMenu;
    private JMenuItem printItem;

//    private JLabel errorMessage;

    @Override
    protected void initializeForm() {
        super.initializeForm();
        dialog.setTitle(messageSource.getMessage("dialog.sold.products.title", null, localeHolder.getLocale()));
        dialog.setResizable(true);
        dialog.setBounds(100, 100, 900, 700);
        dialog.setLocationRelativeTo(null);
    }


    @Override
    protected void initializeComponents() {
        rootContainer.setLayout(new BorderLayout());
        JPanel  southPanel, desktopPanel, filtersPanel, analyzePanel, mainPanel, buttonPanel;

        initializeMenuBar();
        dialog.setJMenuBar(menuBar);

//      desktop pane
        desktopPanel = new JPanel();
        desktopPanel.setLayout(new BorderLayout());
        desktopPanel.setBorder(BorderFactory.createEmptyBorder());

        articleTableComponent.initializeComponents(tableModel, messageSource, localeHolder);

//      main panel for table of goods
        mainPanel = articleTableComponent.getMainPanel();
        mainPanel.setBorder(BorderFactory.createEmptyBorder(1,5,0,5));
        desktopPanel.add(mainPanel, BorderLayout.CENTER);

//        south panel for desktopPanel
        southPanel = new JPanel(new GridBagLayout());
        southPanel.setBorder(BorderFactory.createEmptyBorder());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(1, 5, 1, 5);

//      analyze panel for total analyses
        analyzePanel = articleTableComponent.getArticleFiltersComponent().getArticleAnalyzeComponent().getAnalyzePanel();

//      panel for search operations
        filtersPanel= articleTableComponent.getArticleFiltersComponent().getFiltersPanel();

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.fill = GridBagConstraints.VERTICAL;
        constraints.gridwidth = GridBagConstraints.RELATIVE;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.weightx = 0.0;
        constraints.weighty = 1.0;
        southPanel.add(analyzePanel, constraints);

        constraints.gridx = 1;
        constraints.gridy = 0;
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
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.LINE_AXIS));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JButton refundButton = new JButton();
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


//        errorMessage = new JLabel();
//        errorMessage.setForeground(Color.RED);
//        errorMessage.setHorizontalAlignment(JLabel.LEFT);
//
//        JPanel errorPanel = new JPanel();
//        errorPanel.setLayout(new BoxLayout(errorPanel, BoxLayout.LINE_AXIS));
//        errorPanel.setPreferredSize(new Dimension(200, 30));
//        errorPanel.setMinimumSize(new Dimension(100, 30));
//        errorPanel.setMaximumSize(new Dimension(500, 50));
//        errorPanel.add(errorMessage);

        JButton cancelButton = new JButton();
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

        buttonPanel.add(refundButton);
//        buttonPanel.add(errorPanel);
        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(cancelButton);

        rootContainer.add(buttonPanel, BorderLayout.SOUTH);
    }

    private void initializeMenuBar() {
        menuBar = new JMenuBar();

        fileMenu = new JMenu();
        fileMenu.setText(messageSource.getMessage("mainForm.menu.table", null, localeHolder.getLocale()));

        printItem = new JMenuItem();
        printItem.setText(messageSource.getMessage("mainForm.menu.table.print", null, localeHolder.getLocale()));
        printItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handler.printItemClicked();
            }
        });

        fileMenu.add(printItem);

        menuBar.add(fileMenu);


    }

    /**
     * Filters the JTable by permissions of roles (ROLE_SELLER). It removes certain columns.
     *
     * @param userRoles the list of user's roles
     */
    public void filterTableByRoles(java.util.List<String> userRoles) {
        articleTableComponent.filterTableByRoles(userRoles);
    }

//    public void filterAnalyzePanelByRoles(java.util.List<String> userRoles) {
//        articleTableComponent.getArticleFiltersComponent().getArticleAnalyzeComponent().
//                filterAnalyzeComponentByRoles(userRoles);
//    }

    public ArticleTableComponent getArticleTableComponent() {
        return articleTableComponent;
    }

    public ArticlesTableModel getTableModel() {
        return tableModel;
    }

//    public JLabel getErrorMessage() {
//        return errorMessage;
//    }


    public JMenuItem getPrintItem() {
        return printItem;
    }
}
