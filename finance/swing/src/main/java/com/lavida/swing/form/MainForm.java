package com.lavida.swing.form;

import com.lavida.swing.dialog.SoldProductsDialog;
import com.lavida.swing.form.component.ArticleTableComponent;
import com.lavida.swing.handler.MainFormHandler;
import com.lavida.swing.service.ArticlesTableModel;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

/**
 * The main form of application.
 * Created: 20:09 03.08.13
 *
 * @author Ruslan
 */
@Component
public class MainForm extends AbstractForm {

    @Resource
    private MainFormHandler handler;

    @Resource(name = "notSoldArticleTableModel")
    private ArticlesTableModel tableModel;

    @Resource
    private SoldProductsDialog soldProductsDialog;

    private JPanel operationPanel, refreshPanel, southPanel, desktopPanel, filtersPanel, analyzePanel, mainPanel,
            buttonPanel, statusBarPanel;
    private Button refreshButton, recommitButton, sellButton, showSoldProductsButton;
    private JLabel postponedOperations, postponedMessage;

    private ArticleTableComponent articleTableComponent = new ArticleTableComponent();

    @Override
    protected void initializeForm() {
        super.initializeForm();
        form.setTitle(messageSource.getMessage("mainForm.form.title", null, localeHolder.getLocale()));
        form.setResizable(true);
        form.setBounds(100, 100, 800, 700);
        form.setLocationRelativeTo(null);
    }

    @Override
    protected void initializeComponents() {
        rootContainer.setLayout(new BorderLayout());

//      desktop pane
        desktopPanel = new JPanel();
//        desktopPanel.setBackground(Color.white);
        desktopPanel.setLayout(new BorderLayout());
        desktopPanel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(5,5,5,5);
//        constraints.fill = GridBagConstraints.HORIZONTAL;


        articleTableComponent.initializeComponents(tableModel, messageSource, localeHolder);

//      main panel for table of goods
        mainPanel = articleTableComponent.getMainPanel();
        desktopPanel.add(mainPanel, BorderLayout.CENTER);

//      analyze panel for total analyses
        analyzePanel = articleTableComponent.getArticleFiltersComponent().getArticleAnalyzeComponent().getAnalyzePanel();
        analyzePanel.setPreferredSize(new Dimension(790, 30));

//      panel for search operations
        filtersPanel = articleTableComponent.getArticleFiltersComponent().getFiltersPanel();

        buttonPanel = new JPanel(new GridLayout(2,1));
//      panel for refresh and save operations with data
        refreshPanel = new JPanel();
        refreshPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(messageSource.
                getMessage("mainForm.panel.refresh.title", null, localeHolder.getLocale())),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        refreshPanel.setLayout(new BoxLayout(refreshPanel, BoxLayout.Y_AXIS));
        refreshButton = new Button(messageSource.getMessage("mainForm.button.refresh.title", null,
                localeHolder.getLocale()));
        refreshButton.setMnemonic(KeyEvent.VK_T);  // Alt+T hot keys
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handler.refreshButtonClicked();
            }
        });
        refreshPanel.add(refreshButton);

        refreshPanel.add(Box.createVerticalStrut(20));
        refreshPanel.add(new JSeparator(JSeparator.HORIZONTAL));
        refreshPanel.add(Box.createVerticalStrut(5));

        recommitButton = new Button(messageSource.getMessage("mainForm.button.recommit.title", null, localeHolder.getLocale()));
        recommitButton.setMnemonic(KeyEvent.VK_K); // Alt + K hot keys
        recommitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handler.recommitButtonClicked();
            }
        });
        refreshPanel.add(recommitButton);


//        operation panel for selling and returning goods.
        operationPanel = new JPanel();
        operationPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(messageSource.
                getMessage("mainForm.panel.operation.title", null, localeHolder.getLocale())),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        operationPanel.setLayout(new BoxLayout(operationPanel, BoxLayout.Y_AXIS));

        sellButton = new Button(messageSource.getMessage("mainForm.button.sell.title", null, localeHolder.getLocale()));
        sellButton.setMnemonic(KeyEvent.VK_S); // Alt + S hot keys
        sellButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handler.sellButtonClicked();
            }
        });
        operationPanel.add(sellButton);

        operationPanel.add(Box.createVerticalStrut(20));
        operationPanel.add(new JSeparator(JSeparator.HORIZONTAL));
        operationPanel.add(Box.createVerticalStrut(5));

        showSoldProductsButton = new Button(messageSource.getMessage("mainForm.button.show.sold.products.title", null, localeHolder.getLocale()));
        showSoldProductsButton.setMnemonic(KeyEvent.VK_X); // Alt + X hot keys
        showSoldProductsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handler.showSoldProductsButtonClicked();
            }
        });
        operationPanel.add(showSoldProductsButton);
        buttonPanel.add(refreshPanel);
        buttonPanel.add(operationPanel);

        southPanel = new JPanel(new GridBagLayout());
        southPanel.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.fill = GridBagConstraints.HORIZONTAL;
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
        constraints.weighty = 0.0;
        southPanel.add(buttonPanel, constraints);

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

//        Status bar panel
        statusBarPanel = new JPanel();
        FlowLayout flowLayout = new FlowLayout(FlowLayout.LEADING);
        statusBarPanel.setLayout(flowLayout);
        statusBarPanel.setPreferredSize(new Dimension(790, 30));
        statusBarPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(0, 5, 5, 5),
                BorderFactory.createBevelBorder(BevelBorder.RAISED)));
        postponedOperations = new JLabel();
        postponedOperations.setHorizontalAlignment(JLabel.LEFT);
        postponedOperations.setVerticalAlignment(JLabel.CENTER);
        postponedOperations.setLabelFor(postponedMessage);
        postponedOperations.setVerticalTextPosition(JLabel.CENTER);
        postponedOperations.setText(messageSource.getMessage(
                "mainForm.panel.statusBar.postponed.operations.label.title", null, localeHolder.getLocale()));

        postponedMessage = new JLabel();
        postponedMessage.setVerticalTextPosition(JLabel.CENTER);
        postponedMessage.setForeground(Color.RED);
        handler.showPostponedOperationsMessage();
        statusBarPanel.add(postponedOperations);
        statusBarPanel.add(postponedMessage);

        rootContainer.add(statusBarPanel, BorderLayout.SOUTH);
    }

    /**
     * Filters the JTable by permissions of roles (ROLE_SELLER). It removes certain columns.
     *
     * @param userRoles
     */
    public void filterTableByRoles(List<String> userRoles) {
        articleTableComponent.filterTableByRoles(userRoles);
    }

    public void filterAnalyzePanelByRoles(List<String> userRoles) {
        articleTableComponent.getArticleFiltersComponent().getArticleAnalyzeComponent().
                filterAnalyzeComponentByRoles(userRoles);
    }

    public JLabel getPostponedMessage() {
        return postponedMessage;
    }

    public MainFormHandler getHandler() {
        return handler;
    }

    public SoldProductsDialog getSoldProductsDialog() {
        return soldProductsDialog;
    }

    public ArticleTableComponent getArticleTableComponent() {
        return articleTableComponent;
    }

    private class Button extends JButton {
        private Button() {
            super();
            setHorizontalTextPosition(JButton.CENTER);
            setPreferredSize(new Dimension(150, 25));
            setMaximumSize(new Dimension(150, 25));
            setMinimumSize(new Dimension(150, 25));

        }

        private Button(String text) {
            super(text);
            setHorizontalTextPosition(JButton.CENTER);
            setPreferredSize(new Dimension(150, 25));
            setMaximumSize(new Dimension(150, 25));
            setMinimumSize(new Dimension(150, 25));
        }
    }
}

